package com.ooyala.playback.apps.validators;

import org.apache.log4j.Logger;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.DiscoveryAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.factory.PlayBackFactory;

import io.appium.java_client.AppiumDriver;

public class AdValidator extends PlaybackApps implements Validators {

	public AdValidator(AppiumDriver driver) {
		super(driver);
	}

	private TestParameters test;
	private boolean isOoyalaSkinSampleApp = false;
	final static Logger logger = Logger.getLogger(AdValidator.class);	

	public AdValidator setTestParameters(TestParameters test) {
		this.test = test;
		return this;
	}
	
	public AdValidator isOoyalaSkinSampleAsset(boolean isSkinSampleApp) {
		this.isOoyalaSkinSampleApp = isSkinSampleApp;
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
		DiscoveryAction clickDiscoveryAction = playBackFactory.getClickDiscoveryButtonAction();
		ElementValidator elementValidator = playBackFactory.getEventValidator();
		
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
		
		if (test.getAsset().contains("PRE") || (test.getAsset().contains("MULTI") && !test.getAsset().contains("MIDROLL") && !test.getAsset().contains("FW_MULTIMID"))) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
			if (test.getAsset().contains("SKIPPABLE"))
				if(isOoyalaSkinSampleApp)
				{
				result = result && elValidator.validate("SKIP_AD_OOYALASKINAPP", 20000);
				result = result && elValidator.clickOnElement("SKIP_AD_OOYALASKINAPP");	
				}
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
		 if(test.getAsset().contains("QUADMID")) {
 			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
 			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
 			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
 			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
 			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
 			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
		}
		if (test.getAsset().contains("MID") || test.getAsset().contains("MULTI")) {
			if(test.getAsset().contains("LONG_ASSET") &&  isOoyalaSkinSampleApp)
			{
				result = result && clickDiscoveryAction.clickPauseButton();
				result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
				result = result && clickDiscoveryAction.seekToMiddle("SEEKBAR_ANDROID");
				result = result && elementValidator.handleLoadingSpinner();
				result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
				result = result && clickDiscoveryAction.clickPlayButton();	
			}
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 70000);
			if (test.getAsset().contains("SKIPPABLE"))
			if(isOoyalaSkinSampleApp)
			{
			result = result && elValidator.validate("SKIP_AD_OOYALASKINAPP", 20000);
			result = result && elValidator.clickOnElement("SKIP_AD_OOYALASKINAPP");	
			}
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 70000);
			if(iOS)
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 70000);
			else
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED_ANDRD, 70000);	

		}
		
		
		if (!isSmallAsset && !isOoyalaSkinSampleApp) {
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
		} else if (!iOS) {
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekToEnd("SEEKBAR_ANDROID");
			result = result && elementValidator.handleLoadingSpinner();
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
		}
		
		if (test.getAsset().contains("POST")) {
			result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 70000);
			if (test.getAsset().contains("SKIPPABLE"))
				result = result && elValidator.validate("SKIP_AD", 10000);
			result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 70000);
		}

		return true;
	}
	
}
