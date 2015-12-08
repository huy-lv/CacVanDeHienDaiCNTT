package com.uet.launcher3.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.uet.launcher3.R;
import com.uet.launcher3.assistivetouch.AssistiveTouchService;
import com.uet.launcher3.assistivetouch.IAssistiveTouchService;
import com.uet.launcher3.assistivetouch.SettingsTouchDotActivity;
import com.uet.launcher3.assistivetouch.SettingsTouchPanelActivity;
import com.uet.launcher3.assistivetouch.util.L;
import com.uet.launcher3.assistivetouch.util.Settings;

public class AssistiveTouchSettingFragment extends Fragment implements OnClickListener {
	
	SwitchPreference switchAT;
	private Button mServiceBtn;
	private Button mSettingDotBtn;
	private Button mSettingPanelBtn;
	private IAssistiveTouchService mService;
	private boolean isRunning;
	private Settings mSetting;
	private Handler mHandler = new Handler();
	private static final int REQUEST_CODE_SETTING = 100;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_assistive_touch_setting, container,false);
		init();
        initView(v);
        mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				connect ();
			}
		}, 1500);
        
		return v;
	}
	
	
	private void init () {
//		app = (AssistiveTouchApplication)getActivity().getApplication();
    	mSetting = Settings.getInstance(getActivity());
    }
    
    private void initView (View v) {
    	mServiceBtn = (Button) v.findViewById(R.id.service_start_stop_btn);
    	mServiceBtn.setEnabled(false);
    	mServiceBtn.setOnClickListener(this);

    	mSettingDotBtn = (Button) v.findViewById(R.id.main_settings_touch_dot_btn);
    	mSettingDotBtn.setEnabled(false);
    	mSettingDotBtn.setOnClickListener(this);
    	
    	mSettingPanelBtn = (Button) v.findViewById(R.id.main_settings_touch_panel_btn);
    	mSettingPanelBtn.setEnabled(false);
    	mSettingPanelBtn.setOnClickListener(this);
    	
    	CheckBox box = (CheckBox)v.findViewById(R.id.enable_boot_checkbox);
    	boolean enable = mSetting.isEnableBootStart();
    	box.setChecked(enable);
    	box.setOnCheckedChangeListener(mOnCheckedChangeListener);
    	
    	CheckBox auto_update_box = (CheckBox) v.findViewById(R.id.enable_update_checkbox);
    	enable = mSetting.isEnableAutoUpdate();
    	auto_update_box.setChecked(enable);
    	auto_update_box.setOnCheckedChangeListener(mOnCheckedChangeListener);
    	
    	TextView version = (TextView) v.findViewById(R.id.version_textview);
//    	String name = AssistiveTouchApplication.getVersionName(getActivity());
//		String ver = getString(R.string.version_code, name);
//		version.setText(ver);
    }


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.service_start_stop_btn:
			if (mService != null) {
				try {
					if (isRunning) {
						Log.e("cxz","stop()");
						mService.stop();
					} else {
						Log.e("cxz","start()");
						mService.start();
					}
					isRunning = mService.isRunning();
					changeButtonStatus(isRunning);
				} catch (RemoteException e) {
					L.e("cxz", "", e);
				}
			} else {
				L.d("cxz", "mService == null");
			}
			break;
		case R.id.main_settings_touch_dot_btn:
			Intent i = new Intent(getActivity(), SettingsTouchDotActivity.class);
			startActivityForResult(i, REQUEST_CODE_SETTING);
			break;
		case R.id.main_settings_touch_panel_btn:
			Intent ii = new Intent(getActivity(), SettingsTouchPanelActivity.class);
			startActivityForResult(ii, REQUEST_CODE_SETTING);
			break;
		}
	}
    
	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView.getId() == R.id.enable_boot_checkbox) {
				mSetting.setEnableBootStart(isChecked);
			} 
//			else if (buttonView.getId() == R.id.enable_update_checkbox) {
//				mSetting.setEnableAutoUpdate(isChecked);
//				AssistiveTouchApplication app = (AssistiveTouchApplication) getApplication();
//				if (isChecked) {
//					app.setupAutoUpdate();
//				} else {
//					app.cancelAutoUpdate();
//				}
//			}
		}
		
	};
	
	
	private void connect () {
		L.d("cxz", "connect ...");
		Intent i = new Intent(AssistiveTouchService.ASSISTIVE_TOUCH_START_ACTION);
		i.setPackage(getActivity().getPackageName());
		getActivity().bindService(i, mServiceConn, Context.BIND_AUTO_CREATE);
	}
	
	private ServiceConnection mServiceConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			L.d("cxz", "onServiceDisconnected");
			mService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			L.d("cxz", "onServiceConnected");
			mServiceBtn.setEnabled(true);
			mSettingDotBtn.setEnabled(true);
			mSettingPanelBtn.setEnabled(true);
			mService = IAssistiveTouchService.Stub.asInterface(service);
			try {
				isRunning = mService.isRunning();
				L.d("cxz", "service is running:" + isRunning);
				changeButtonStatus (isRunning);
			} catch (RemoteException e) {
				L.e("cxz", "", e);
			}
		}
	};
	
	private void changeButtonStatus (boolean isStart) {
    	mServiceBtn.setText(isStart ? R.string.stop_service : R.string.start_service);
    }
	
    
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		addPreferencesFromResource(R.xml.fragment_assistive_touch_setting);
//			
//		switchAT = (SwitchPreference) findPreference(Utilities.TURN_ON_AT_KEY);
//		
//		Bundle extras = new Bundle();
//        extras.putBoolean(LauncherSettings.Settings.EXTRA_DEFAULT_VALUE, false);
//        Bundle value = getActivity().getContentResolver().call(
//                LauncherSettings.Settings.CONTENT_URI,
//                LauncherSettings.Settings.METHOD_GET_BOOLEAN,
//                Utilities.TURN_ON_AT_KEY, extras);
//        switchAT.setChecked(value.getBoolean(LauncherSettings.Settings.EXTRA_VALUE));
//
//        switchAT.setOnPreferenceChangeListener(this);
//		
//	}
//
//	@Override
//	public boolean onPreferenceChange(Preference preference, Object newValue) {
//		Bundle extras = new Bundle();
//        extras.putBoolean(LauncherSettings.Settings.EXTRA_VALUE, (Boolean) newValue);
//        getActivity().getContentResolver().call(
//                LauncherSettings.Settings.CONTENT_URI,
//                LauncherSettings.Settings.METHOD_SET_BOOLEAN,
//                preference.getKey(), extras);
//        
//        if(preference.getKey().equals(Utilities.TURN_ON_AT_KEY)){
//        	if((boolean)newValue == true){
//        		if (mService != null) {
//    				try {
//    					if (isRunning) {
//    						mService.stop();
//    					} else {
//    						mService.start();
//    					}
//    					isRunning = mService.isRunning();
//    					changeButtonStatu(isRunning);
//    				} catch (RemoteException e) {
//    					L.e(TAG, "", e);
//    				}
//    			} else {
//    				L.d(TAG, "mService == null");
//    			}
//    			break;
//        	}else{
//        		
//        	}
//        }
//        return true;
//	}
}
