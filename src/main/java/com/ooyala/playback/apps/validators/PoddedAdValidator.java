package com.ooyala.playback.apps.validators;

import org.apache.log4j.Logger;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.DiscoveryAction;
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
	private boolean isOoyalaSkinSampleApp = false;
	final static Logger logger = Logger.getLogger(PoddedAdValidator.class);	

	public PoddedAdValidator setTestParameters(TestParameters test) {
		this.test = test;
		return this;
	}

	public PoddedAdValidator setNoOfAds(String noOfAds) {
		this.noOfAds = Integer.parseInt(noOfAds);
		return this;
	}
	
	public PoddedAdValidator isOoyalaSkinSampleAsset(boolean isSkinSampleApp) {
		this.isOoyalaSkinSampleApp = isSkinSampleApp;
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
		DiscoveryAction clickDiscoveryAction = playBackFactory.getClickDiscoveryButtonAction();
		ElementValidator elementValidator = playBackFactory.getEventValidator();

		boolean iOS = getPlatform().equalsIgnoreCase("ios");

		logger.info("is platform iOS:"+iOS);
		logger.info("is Player skin V4:"+isV4);
		
		String iosPlayPause="PLAY_PAUSE_BUTTON";
		String iosSlider="SLIDER";
		String iosSeekBar="SEEK_BAR";
		
		if(isV4){
			iosPlayPause="PLAY_PAUSE_BUTTON_V4_IOS";
			iosSlider="SLIDER_V4";
			iosSeekBar="SEEK_BAR_V4";
		}
		
		if(test.getAsset().contains("QUAD")) {
			noOfAds = 4;
		}

		if (test.getAsset().contains("PRE")) {
			for (int i = 0; i < noOfAds; i++) {
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			}
		}
		result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

		if (test.getAsset().contains("MID")) {
			for (int i = 0; i < noOfAds; i++) {
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			}

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);

		}
		if(isOoyalaSkinSampleApp)
		{
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekToEnd("SEEKBAR_ANDROID");
			result = result && elementValidator.handleLoadingSpinner();
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
		}
		else
		{
		result = result && iOS ? pauseAction.startAction(iosPlayPause)
				: pauseAction.startAction("PLAY_PAUSE_ANDROID");
		result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
		result = result && iOS ? seekAction.seekforward().setSlider(iosSlider).startAction(iosSeekBar)
				: seekAction.startAction("SEEK_BAR_ANDROID");
		result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
		result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
		result = result && iOS ? pauseAction.startAction(iosPlayPause)
				: pauseAction.startAction("PLAY_PAUSE_ANDROID");
		result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);
		}
		if (test.getAsset().contains("POST")) {
			for (int i = 0; i < noOfAds; i++) {
				result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 30000);
				result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
			}
		}

		return result;
	}

}
