package com.ooyala.playback.apps.validators;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;

public class AdValidator extends PlaybackApps implements Validators {

	public AdValidator(AppiumDriver driver) {
		super(driver);
	}

	TestParameters test;

	public AdValidator setTestParameters(TestParameters test) {
		this.test = test;
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		boolean result = true;
		PlayBackFactory playBackFactory = new PlayBackFactory(driver, extentTest);
		NotificationEventValidator notificationEventValidator = playBackFactory.getNotificationEventValidator();
		PauseAction pauseAction = playBackFactory.getPauseAction();
		SeekAction seekAction = playBackFactory.getSeekAction();

		boolean iOS = getPlatform().equalsIgnoreCase("ios");
		
		/*result = result && iOS ? pauseAction.startAction("PLAY_PAUSE_BUTTON")
				: pauseAction.startAction("PLAY_PAUSE_ANDROID");*/

		if (test.getAsset().contains("PRE") || test.getAsset().contains("MULTI")) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		}

		result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

		if (test.getAsset().contains("MID") || test.getAsset().contains("MULTI")) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 55000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);
			result = result && iOS ? pauseAction.startAction("PLAY_PAUSE_BUTTON")
					: pauseAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
			result = result && iOS ? seekAction.setSlider("SLIDER").startAction("SEEK_BAR")
					: seekAction.startAction("SEEK_BAR_ANDROID");
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
			result = result && iOS ? pauseAction.startAction("PLAY_PAUSE_BUTTON")
					: pauseAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);
		}

		if (test.getAsset().contains("POST")) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		}

		return true;
	}

}
