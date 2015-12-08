package com.uet.launcher3.assistivetouch.util;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

public class VibratorHelper { 
	
	public static void Vibrate(final Context context, long milliseconds) {
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	
	public static void Vibrate(final Context context, long[] pattern,boolean isRepeat) {
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}
	
	public static void KeyVibrate (final Context context) {
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(20);
	}
}
