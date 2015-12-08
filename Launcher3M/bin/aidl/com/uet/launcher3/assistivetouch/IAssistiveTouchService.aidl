package com.uet.launcher3.assistivetouch;

interface IAssistiveTouchService {
	boolean isRunning();
	void start();
	void stop();
	void refresh();
}