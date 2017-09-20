package com.ooyala.playback.apps.validators;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class PauseValidator extends PlaybackApps implements Validators {

	public PauseValidator(AppiumDriver driver) {
		super(driver);
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		if (new PlayBackFactory(driver, extentTest).getPlayAction().startAction(element)) {
			if(getPlatform().equalsIgnoreCase("ios")) {
				return new PlayBackFactory(driver, extentTest).getNotificationEventValidator()
						.verifyEvent(Events.PLAYBACK_PAUSED, timeout);
			}
		}
		extentTest.log(LogStatus.FAIL, "click on PLAY_PAUSE_BUTTON failed.");
		return false;
	}

}
