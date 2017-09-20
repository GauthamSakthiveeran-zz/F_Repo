package com.ooyala.playback.apps.validators;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;

public class AdEventValidator extends PlaybackApps implements Validators {

	public AdEventValidator(AppiumDriver driver) {
		super(driver);
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		if(getPlatform().equalsIgnoreCase("ios")) {
			NotificationEventValidator notificationEventValidator = new PlayBackFactory(driver, extentTest)
					.getNotificationEventValidator();
			return notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000)
					&& notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		}
		// TODO need to add for android.
		return true;
	}

}
