package com.uet.launcher3.speech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uet.launcher3.R;

public class SpeechDialogActivity extends Activity implements OnClickListener,RecognitionListener{
	protected static final int REQUEST_OK = 1;
	SpeechRecognizer speech;
	private Intent recognizerIntent;
	String LOG_TAG ="cx1z1";
	TextView returnedText;
	ProgressBar progressBar;
	ImageView ivSpeech;
	TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_speech);
		findViewById(R.id.ivSpeech).setOnClickListener(this);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		returnedText = (TextView)findViewById(R.id.text1);
		ivSpeech = (ImageView)findViewById(R.id.ivSpeech);
		ivSpeech.setOnClickListener(this);
		progressBar.setIndeterminate(true);
		
		
		speech = SpeechRecognizer.createSpeechRecognizer(this);
		speech.setRecognitionListener(this);
		recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
				"en");
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
				this.getPackageName());
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		
		tts = new TextToSpeech(this, new OnInitListener() {
			
			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				tts.setLanguage(Locale.US);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ivSpeech){
			speech.startListening(recognizerIntent);
		}
		
		

//		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
//        try {
//            startActivityForResult(i, REQUEST_OK);
//        } catch (Exception e) {
//       	 Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
//        }
	}
	
//	@Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==REQUEST_OK  && resultCode==RESULT_OK) {
//        	ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//        	((TextView)findViewById(R.id.text1)).setText(thingsYouSaid.get(0));
//        	String appName = thingsYouSaid.get(0).split(" ")[1];
//        	Log.e("cxz","thing:"+thingsYouSaid.get(0)+" appname:"+appName);
//        	openAppByName(appName);
//        }
//    }

	private void openAppByName(String appName) {
		PackageManager pm;
	    pm = getPackageManager();
	    List<ApplicationInfo> packages = pm.getInstalledApplications(0);
	    String packageName = null;
	    if(appName.equalsIgnoreCase("gallery")){
	    	packageName="com.android.gallery3d";
	    }else if(appName.equalsIgnoreCase("facebook")){
	    	packageName = "com.facebook.orca";
	    }else if(appName.equalsIgnoreCase("file manager")){
	    	packageName = "com.lge.filemanager";
	    }else if(appName.equalsIgnoreCase("map")){
	    	packageName = "com.google.android.apps.maps";
	    }else if(appName.equalsIgnoreCase("contact")){
	    	packageName = "com.android.contacts";
	    }else if(appName.equalsIgnoreCase("youtube")){
	    	packageName = "com.google.android.youtube";
	    }else if(appName.equalsIgnoreCase("clock")){
	    	packageName = "com.lge.clock";
	    }else if(appName.equalsIgnoreCase("calendar")){
	    	packageName = "com.android.calendar";
	    }else if(appName.equalsIgnoreCase("chrome")){
	    	packageName = "com.android.chrome";
	    }else if(appName.equalsIgnoreCase("camera")){
	    	packageName = "com.lge.camera";
	    }else if(appName.equalsIgnoreCase("music")){
	    	packageName = "com.zing.mp3";
	    }else if(appName.equalsIgnoreCase("setting")){
	    	packageName = "com.android.settings";
	    }else if(appName.equalsIgnoreCase("calculator")){
	    	packageName = "com.android.calculator2";
	    }else if(appName.equalsIgnoreCase("gmail")){
	    	packageName = "com.google.android.gm";
	    }
//	    else if(appName.equalsIgnoreCase(""){
//	    	packageName = "";
//	    }else if(appName.equalsIgnoreCase(""){
//	    	packageName = "";
//	    }else if(appName.equalsIgnoreCase(""){
//	    	packageName = "";
//	    }else if(appName.equalsIgnoreCase(""){
//	    	packageName = "";
//	    }else if(appName.equalsIgnoreCase(""){
//	    	packageName = "";
//	    }
	    
	    if(packageName!=null){
	    	Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
    	    startActivity( LaunchIntent );
    	    
    	    tts.speak("opening "+appName, TextToSpeech.QUEUE_FLUSH, null, null);
	    }else{
	    	tts.speak("i don't understand", TextToSpeech.QUEUE_FLUSH, null, null);
	    }
	    
//	    for(ApplicationInfo ai : packages){
//	    	try {
//	    		Log.e("cxz","-"+ai.packageName);
//		        ai = pm.getApplicationInfo( ai.packageName, 0);
//		    } catch (final NameNotFoundException e) {
//		        ai = null;
//		    }
	    	
//	    	if(ai!= null){
//	    		String thisAppName = (String) pm.getApplicationLabel(ai);
//	    		if(thisAppName.equalsIgnoreCase(appName)){
//	    			Log.e("cxz","open "+thisAppName);
//	    			Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(ai.packageName);
//		    	    startActivity( LaunchIntent );
//		    	    
//		    	    tts.speak("opening app", TextToSpeech.QUEUE_FLUSH, null	, null);
//		    	    APP_FOUNDED  = true;
//	    		}
//	    	}else{
//	    		Log.e("cxz","not found package ");
//	    	}
//	    }
	    
//	    if(!APP_FOUNDED){
//	    	tts.speak("app not found", TextToSpeech.QUEUE_FLUSH, null,null);
//	    	Log.e("cxz","app not found");
//	    }
	}
	

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (speech != null) {
			speech.stopListening();
			Log.i(LOG_TAG, "stop");
		}

	}
	@Override
	public void onBeginningOfSpeech() {
		Log.i(LOG_TAG, "onBeginningOfSpeech");
		
		progressBar.setVisibility(View.VISIBLE);
		ivSpeech.setVisibility(View.GONE);
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
		Log.i(LOG_TAG, "onBufferReceived: " + buffer);
	}

	@Override
	public void onEndOfSpeech() {
		Log.i(LOG_TAG, "onEndOfSpeech");
//		progressBar.setIndeterminate(true);
//		toggleButton.setChecked(false);
		progressBar.setVisibility(View.GONE);
		ivSpeech.setVisibility(View.VISIBLE);
	}

	@Override
	public void onError(int errorCode) {
		String errorMessage = getErrorText(errorCode);
		Log.d(LOG_TAG, "FAILED " + errorMessage);
		returnedText.setText(errorMessage);
//		toggleButton.setChecked(false);
	}

	@Override
	public void onEvent(int arg0, Bundle arg1) {
		Log.i(LOG_TAG, "onEvent");
	}

	@Override
	public void onPartialResults(Bundle arg0) {
		Log.i(LOG_TAG, "onPartialResults");
	}

	@Override
	public void onReadyForSpeech(Bundle arg0) {
		Log.i(LOG_TAG, "onReadyForSpeech");
	}

	@Override
	public void onResults(Bundle results) {
		Log.i(LOG_TAG, "onResults");
		ArrayList<String> matches = results
				.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//		String text = "";
//		for (String result : matches)
//			text += result + "\n";
		
		returnedText.setText(matches.get(0));
		String[] strarr = matches.get(0).split(" ");
		String appName="";
		for(int i=1 ;i<strarr.length ; i++){
			appName += strarr + " ";
		}
		openAppByName(appName.trim());
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
//		progressBar.setProgress((int) rmsdB);
	}

	public static String getErrorText(int errorCode) {
		String message;
		switch (errorCode) {
		case SpeechRecognizer.ERROR_AUDIO:
			message = "Audio recording error";
			break;
		case SpeechRecognizer.ERROR_CLIENT:
			message = "Client side error";
			break;
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
			message = "Insufficient permissions";
			break;
		case SpeechRecognizer.ERROR_NETWORK:
			message = "Network error";
			break;
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			message = "Network timeout";
			break;
		case SpeechRecognizer.ERROR_NO_MATCH:
			message = "No match";
			break;
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
			message = "RecognitionService busy";
			break;
		case SpeechRecognizer.ERROR_SERVER:
			message = "error from server";
			break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			message = "No speech input";
			break;
		default:
			message = "Didn't understand, please try again.";
			break;
		}
		return message;
	}

}
