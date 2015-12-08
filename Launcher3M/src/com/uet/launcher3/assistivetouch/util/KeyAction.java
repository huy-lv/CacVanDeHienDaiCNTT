package com.uet.launcher3.assistivetouch.util;

import java.lang.reflect.Method;

import android.os.IBinder;
import android.view.KeyEvent;

public class KeyAction {

	public static void doHomeAction() {
		doKeyAction(KeyEvent.KEYCODE_HOME);
	}

	public static void doBackAction() {
		doKeyAction(KeyEvent.KEYCODE_BACK);
	}
	
	public static void doRecentAction() {
		try {
			Class ServiceManager = Class.forName("android.os.ServiceManager");
			Method getService = ServiceManager
					.getMethod("getService", new Class[]{String.class});
			Object[] statusbarObj = new Object[]{"statusbar"};
			IBinder binder = (IBinder) getService.invoke(ServiceManager,
					statusbarObj);
			Class IStatusBarService = Class.forName(
					"com.android.internal.statusbar.IStatusBarService")
					.getClasses()[0];
			Method asInterface = IStatusBarService.getMethod("asInterface",
					new Class[]{IBinder.class});
			Object obj = asInterface.invoke(null, new Object[]{binder});
			IStatusBarService.getMethod("toggleRecentApps", new Class[0]).invoke(
					obj, new Object[0]);
		} catch (Exception e) {
			doKeyAction(KeyEvent.KEYCODE_APP_SWITCH);
		}
	}

	public static void doMenuAction() {
		doKeyAction(KeyEvent.KEYCODE_MENU);
	}

	public static void doSearchAction() {
		doKeyAction(KeyEvent.KEYCODE_SEARCH);
	}
	
	public static void doPowerAction() {
		doKeyAction(KeyEvent.KEYCODE_POWER);
	}

	public static void doKeyAction(int keycode) {
		RootContext.getInstance().runCommand("input keyevent " + keycode);
	}
}
