package com.ooyala.playback.apps.validators;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class PoddedAdValidator extends PlaybackApps implements Validators {

	public PoddedAdValidator(AppiumDriver driver) {
		super(driver);
	}

	private TestParameters test;
	private int noOfAds = 0;

	public PoddedAdValidator setTestParameters(TestParameters test) {
		this.test = test;
		return this;
	}

	public PoddedAdValidator setNoOfAds(int noOfAds) {
		this.noOfAds = noOfAds;
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		if (noOfAds == 0) {
			extentTest.log(LogStatus.FATAL, "noOfAds needs to be set.");
			assert false;
		}

		boolean result = true;
		PlayBackFactory playBackFactory = new PlayBackFactory(driver, extentTest);
		NotificationEventValidator notificationEventValidator = playBackFactory.getNotificationEventValidator();
		PauseAction pauseAction = playBackFactory.getPauseAction();
		SeekAction seekAction = playBackFactory.getSeekAction();

		boolean premidpost = test.getAsset().contains("PREMIDPOST");
		boolean iOS = getPlatform().equalsIgnoreCase("ios");
		
		if(test.getAsset().contains("QUAD")) {
			noOfAds = 4;
		}

		if (test.getAsset().contains("PREROLL") || premidpost) {
			for (int i = 0; i < noOfAds; i++) {
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			}
		}
		result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

		if (test.getAsset().contains("MIDROLL") || premidpost) {
			for (int i = 0; i < noOfAds; i++) {
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			}

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);

		}

		result = result && notificationEventValidator.letVideoPlayForSec(4);
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

		if (test.getAsset().contains("POSTROLL") || premidpost) {
			for (int i = 0; i < noOfAds; i++) {
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			}
		}

		return false;
	}

}
