package com.ooyala.playback.apps.validators;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;

public class PoddedAdValidator extends PlaybackApps implements Validators {

	public PoddedAdValidator(AppiumDriver driver) {
		super(driver);
	}

	TestParameters test;

	public PoddedAdValidator setTestParameters(TestParameters test) {
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
		
		boolean premidpost = test.getAsset().contains("PREMIDPOST");
		
		if(getPlatform().equalsIgnoreCase("ios")) {
			
			if (test.getAsset().contains("PREROLL") || premidpost) {
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			}
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
			
			if (test.getAsset().contains("MIDROLL") || premidpost) {
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);
				result = result && notificationEventValidator.letVideoPlayForSec(4);
				result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
				result = result && seekAction.setSlider("SLIDER").startAction("SEEK_BAR"); // SeekBack
				result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
				result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
				result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);
			}
			
			if (test.getAsset().contains("POSTROLL") || premidpost) {
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			}
			
		}
		return false;
	}

}
