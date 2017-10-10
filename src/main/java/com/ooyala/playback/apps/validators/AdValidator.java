package com.ooyala.playback.apps.validators;

import org.apache.log4j.Logger;

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

	private TestParameters test;
	final static Logger logger = Logger.getLogger(AdValidator.class);	

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
		ElementValidator elValidator = playBackFactory.getEventValidator();

		boolean iOS = getPlatform().equalsIgnoreCase("ios");
		logger.info("is platform iOS:"+iOS);
		logger.info("is Player skin is V4:"+isV4);
		
		String iosPlayPause="PLAY_PAUSE_BUTTON";
		String iosSlider="SLIDER";
		String iosSeekBar="SEEK_BAR";
		boolean isSmallAsset = test.getDescription().contains("small");
		
		if(isV4){
			iosPlayPause="PLAY_PAUSE_BUTTON_V4_IOS";
			iosSlider="SLIDER_V4";
			iosSeekBar="SEEK_BAR_V4";
		}
		
		if (test.getAsset().contains("PRE") || (test.getAsset().contains("MULTI") && !test.getAsset().contains("MIDROLL"))) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
			if (test.getAsset().contains("SKIPPABLE"))
				result = result && elValidator.validate("SKIP_AD", 10000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		}

		result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
		
		if(isV4 && test.getAsset().contains("OVERLAY")){
			result = result && elValidator.validate("AD_OVERLAY", 10000);
			result = result && elValidator.validateElementDisappeared("AD_OVERLAY", 20);
		}

		if (test.getAsset().contains("MULTI_MIDROLL")) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 55000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);			
		}
		
		if (test.getAsset().contains("MID") || test.getAsset().contains("MULTI")) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 55000);
			if (test.getAsset().contains("SKIPPABLE"))
				result = result && elValidator.validate("SKIP_AD", 10000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);

		}
		
		if (!isSmallAsset) {
			result = result
			        && (iOS ? pauseAction.startAction(iosPlayPause) : pauseAction.startAction("PLAY_PAUSE_ANDROID"));
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
			result = result && (iOS
			        ? ((isV4 && test.getAsset().contains("PREROLL"))
			                ? seekAction.setSlider(iosSlider).seekforward().startAction(iosSeekBar)
			                : seekAction.setSlider(iosSlider).startAction(iosSeekBar))
			        : seekAction.startAction("SEEK_BAR_ANDROID"));
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
			result = result
			        && (iOS ? pauseAction.startAction(iosPlayPause) : pauseAction.startAction("PLAY_PAUSE_ANDROID"));
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);
		}
		
		if (test.getAsset().contains("POST")) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
			if (test.getAsset().contains("SKIPPABLE"))
				result = result && elValidator.validate("SKIP_AD", 10000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		}

		return true;
	}
	
}
