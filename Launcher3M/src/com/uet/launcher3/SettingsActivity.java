/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uet.launcher3;

import com.uet.launcher3.fragment.AssistiveTouchSettingFragment;
import com.uet.launcher3.fragment.SwipeAdminActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

/**
 * Settings activity for Launcher. Currently implements the following setting: Allow rotation
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new LauncherSettingsFragment())
                .commit();
    }

    /**
     * This fragment shows the launcher preferences.
     */
    public static class LauncherSettingsFragment extends PreferenceFragment
            implements OnPreferenceChangeListener, OnPreferenceClickListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.launcher_preferences);

            SwitchPreference pref = (SwitchPreference) findPreference(
                    Utilities.ALLOW_ROTATION_PREFERENCE_KEY);
            pref.setPersistent(false);

            Bundle extras = new Bundle();
            extras.putBoolean(LauncherSettings.Settings.EXTRA_DEFAULT_VALUE, false);
            Bundle value = getActivity().getContentResolver().call(
                    LauncherSettings.Settings.CONTENT_URI,
                    LauncherSettings.Settings.METHOD_GET_BOOLEAN,
                    Utilities.ALLOW_ROTATION_PREFERENCE_KEY, extras);
            pref.setChecked(value.getBoolean(LauncherSettings.Settings.EXTRA_VALUE));

            pref.setOnPreferenceChangeListener(this);
            
            Preference pref_AT = (Preference)findPreference(Utilities.ASSISTIVE_SETTING);
            pref_AT.setOnPreferenceClickListener(this);
            
            Preference prefSwipe = (Preference)findPreference(Utilities.SWIPE_UP_SETTING);
            prefSwipe.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
        	
        	
            Bundle extras = new Bundle();
            extras.putBoolean(LauncherSettings.Settings.EXTRA_VALUE, (Boolean) newValue);
            getActivity().getContentResolver().call(
                    LauncherSettings.Settings.CONTENT_URI,
                    LauncherSettings.Settings.METHOD_SET_BOOLEAN,
                    preference.getKey(), extras);
            
            return true;
        }

		@Override
		public boolean onPreferenceClick(Preference arg0) {
			//add on click listener by HuyLV
			if(arg0.getKey().equals(Utilities.ASSISTIVE_SETTING)){
				AssistiveTouchSettingFragment assistiveTouchSettingFragment = new AssistiveTouchSettingFragment();
				getFragmentManager().beginTransaction().replace(android.R.id.content,assistiveTouchSettingFragment).addToBackStack(null).commit();
			}else if(arg0.getKey().equals(Utilities.SWIPE_UP_SETTING)){
				Intent i = new Intent(getActivity(),SwipeAdminActivity.Controller.class);
				startActivity(i);
			}
			return true;
		}
    }
}
