package com.ooyala.playback.apps.validators;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;

public class SeekValidator extends PlaybackApps implements Validators {

	public SeekValidator(AppiumDriver driver) {
		super(driver);
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		NotificationEventValidator notificationEventValidator = new PlayBackFactory(driver, extentTest).getNotificationEventValidator();
		return notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 25000)
				&& notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 25000);
	}

}
