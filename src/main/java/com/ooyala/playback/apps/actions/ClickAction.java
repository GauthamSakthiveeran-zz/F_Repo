package com.ooyala.playback.apps.actions;

import com.ooyala.playback.apps.PlaybackApps;

import io.appium.java_client.AppiumDriver;

public class ClickAction extends PlaybackApps implements Actions  {

	public ClickAction(AppiumDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean startAction(String element) throws Exception {
		return clickOnIndependentElement(element);
	}

}
