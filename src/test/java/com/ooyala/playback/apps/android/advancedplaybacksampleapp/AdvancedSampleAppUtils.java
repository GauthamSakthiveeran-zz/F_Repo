package com.ooyala.playback.apps.android.advancedplaybacksampleapp;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.CCAction;
import com.ooyala.playback.apps.actions.ClickAction;
import com.ooyala.playback.apps.actions.ClickDiscoveryButtonAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

public class AdvancedSampleAppUtils extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(AdvancedSampleAppUtils.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private CCAction ccAction;
	private ClickAction clickAction;
	private ClickDiscoveryButtonAction clickDiscoveryAction;
	private PlayAction playAction;
	//pageFactory = new pageFactory((AppiumDriver) driver, extentTest);

	public boolean performAssetSpecificTest(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		ccAction = pageFactory.getCcAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickAction = pageFactory.getClickAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();

		try {
			result = result && selectVideo.startAction(test.getAsset());
			result = result && elementValidator.handleLoadingSpinner();
			
			if (test.getAsset().contains("MULTIPLE") || test.getAsset().contains("INSERT_AD")
					|| test.getAsset().contains("CHANGE_VIDEO")
					|| test.getAsset().contains("CUSTOM_OVERLAY")
					|| test.getAsset().contains("UNBUNDLED_HLS")
					|| test.getAsset().contains("NOTIFICATION_SAMPLE")
					|| test.getAsset().contains("CUSTOM_VIDEO_PLAYER_SAMPLE")
					|| test.getAsset().contains("CUSTOM_PLUGIN_SAMPLE")) {
				
				result = result && elementValidator.validate("PLAY_PAUSE_ANDROID",10000);
				result = result && playAction.startAction("PLAY_PAUSE_ANDROID"); }
				
			if (test.getAsset().contains("CUSTOM_PLUGIN_SAMPLE"))
				adpodEventValidator();
			
			if (test.getAsset().contains("CUSTOM_CONTROLS"))
				result = result && clickAction.startAction("CUSTOM_PLAY_PAUSE_BUTTON_ANDROID");
			
			result = result && selectVideo.letVideoPlayForSec(3);
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 50000);

			if (test.getAsset().contains("CHANGE_VIDEO")) {
				result = result && selectVideo.letVideoPlayForSec(2);
				clickDiscoveryAction.tapOnScreen();
				result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 50000);
				result = result && clickAction.startAction("ANDROID_PLAY_VIDEO_1");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 90000);
				result = result && clickAction.startAction("ANDROID_PLAY_VIDEO_2");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
				result = result && selectVideo.letVideoPlayForSec(2);
			}

			if (test.getAsset().contains("INSERT_AD")) {
				result = result && clickAction.startAction("ANDROID_INSERT_OOYALA_AD");
				result = adEventValidator();
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);
				result = result && clickAction.startAction("ANDROID_INSERT_OOYALA_AD");
				result = adEventValidator();
			}

			if (!test.getAsset().contains("CUSTOM_CONTROLS") && !test.getAsset().contains("CHANGE_VIDEO") && !test.getAsset().contains("INITIALTIME")
					&& !test.getAsset().contains("CUSTOM_PLUGIN_SAMPLE")) {
				
				Thread.sleep(10000);
				clickDiscoveryAction.tapOnScreen();	
				result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
				
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
				if (test.getAsset().contains("CUSTOM_OVERLAY")
						|| test.getAsset().contains("NOTIFICATION_SAMPLE"))
					result = result && seekActionEventValidator(true);
				else
					result = result && seekActionEventValidator(false);
				result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
			}

			if (test.getAsset().contains("CUSTOM_CONTROLS")) {
				result = result && selectVideo.letVideoPlayForSec(2);
				clickDiscoveryAction.tapOnScreen();	
				result = result && clickAction.startAction("CUSTOM_PLAY_PAUSE_BUTTON_ANDROID");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
				result = result && clickAction.startAction("CUSTOM_PLAY_PAUSE_BUTTON_ANDROID");
			}

			if (!test.getAsset().contains("CHANGE_VIDEO") && !test.getAsset().contains("CUSTOM_PLUGIN_SAMPLE")
					&& !test.getAsset().contains("INITIALTIME"))
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);


			if (test.getAsset().contains("CUSTOM_PLUGIN_SAMPLE")) {
				adpodEventValidator();
				adpodEventValidator(); }
			
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 90000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	private boolean adEventValidator() {
		boolean result = true;
		result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
		result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		return result;
	}

	private boolean adpodEventValidator() {
		boolean result = true;
		result = result && notificationEventValidator.verifyEvent(Events.AD_POD_STARTED, 25000);
		result = result && notificationEventValidator.verifyEvent(Events.AD_POD_COMPLETED, 25000);
		return result;
	}
	
	private boolean seekActionEventValidator(boolean isSeekForward) {
		boolean result = true;
		try {
			if (isSeekForward)
				result = result && seekAction.setSlider("SLIDER").seekforward().startAction("SEEK_BAR_ANDROID"); // SeekForward
			else
				result = result && seekAction.setSlider("SLIDER").startAction("SEEK_BAR_ANDROID"); // SeekBack
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
		} catch (Exception e) {
			logger.error("Here is an exception" + e);
			result = false;
		}
		return result;

	}

}
