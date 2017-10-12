package com.ooyala.playback.apps.android.ooyalaskinsamplespp;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.AllowAction;
import com.ooyala.playback.apps.actions.AndroidKeyCodeAction;
import com.ooyala.playback.apps.actions.CCAction;
import com.ooyala.playback.apps.actions.ClickAction;
import com.ooyala.playback.apps.actions.ClickDiscoveryButtonAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.SwipeUpDownAppAssetsAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

public class OoyalaSkinSampleAppUtils extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(OoyalaSkinSampleAppUtils.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private ClickDiscoveryButtonAction clickDiscoveryAction;
	private PlayAction playAction;
	private AndroidKeyCodeAction androidKeyCode;
	private AllowAction allowAction;
	private SwipeUpDownAppAssetsAction appAssetsSelection;
	// pageFactory = new pageFactory((AppiumDriver) driver, extentTest);

	public boolean freeWheelTests(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();

		try {
			result = result && appAssetsSelection.startAction("Freewheel Integration");
			Thread.sleep(3000);
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(3000);
			result = result && allowAction.startAction("ALLOW");
			result = result && androidKeyCode.startAction("BACK");
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(5000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("PREROLL") || test.getAsset().contains("PREMIDPOST")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			if (test.getAsset().contains("MULTIMID")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekForward("SEEKBAR_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("MIDROLL") || test.getAsset().contains("POSTROLL")
					|| test.getAsset().contains("PREMIDPOST") || test.getAsset().contains("MULTIMID")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean testIMAAdRules(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();

		try {
			result = result && appAssetsSelection.startAction("Google IMA Integration");
			Thread.sleep(3000);
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(3000);
			result = result && allowAction.startAction("ALLOW");
			result = result && androidKeyCode.startAction("BACK");
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(5000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("PREROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekForward("SEEKBAR_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("MIDROLL") || test.getAsset().contains("POSTROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean testIMAPoddedAds(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();

		try {
			result = result && appAssetsSelection.startAction("Google IMA Integration");
			Thread.sleep(3000);
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(3000);
			result = result && allowAction.startAction("ALLOW");
			result = result && androidKeyCode.startAction("BACK");
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(5000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("PREROLL") || test.getAsset().contains("PREMIDPOST")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekForward("SEEKBAR_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("PREMIDPOST")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			if (test.getAsset().contains("MIDROLL") || test.getAsset().contains("POSTROLL")
					|| test.getAsset().contains("PREMIDPOST")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
			}

			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean testIMASkippableAds(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();

		try {
			result = result && appAssetsSelection.startAction("Google IMA Integration");
			Thread.sleep(3000);
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(3000);
			result = result && allowAction.startAction("ALLOW");
			result = result && androidKeyCode.startAction("BACK");
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(5000);
			result = result && clickDiscoveryAction.clickPlayButton();
			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
			result = result && elementValidator.validate("SKIPAD_ANDROID", 10000);
			result = result && elementValidator.clickOnElement("SKIPAD_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.AD_SKIPPPED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekForward("SEEKBAR_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("PREMIDPOST")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && elementValidator.validate("SKIPAD_ANDROID", 20000);
				result = result && elementValidator.clickOnElement("SKIPAD_ANDROID");
				result = result && notificationEventValidator.validateEvent(Events.AD_SKIPPPED, 20000);
			}
			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
			result = result && elementValidator.validate("SKIPAD_ANDROID", 10000);
			result = result && elementValidator.clickOnElement("SKIPAD_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.AD_SKIPPPED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean testIMANonAdRules(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();

		try {
			result = result && appAssetsSelection.startAction("Google IMA Integration");
			Thread.sleep(3000);
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(3000);
			result = result && allowAction.startAction("ALLOW");
			result = result && androidKeyCode.startAction("BACK");
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(5000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("PREROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekForward("SEEKBAR_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("QUADMID")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
			}
			if (test.getAsset().contains("MIDROLL") || test.getAsset().contains("PREMIDMIDPOST")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
			}
			if (test.getAsset().contains("PREMIDMIDPOST")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
			}
			if (test.getAsset().contains("PREMIDMIDPOST") || test.getAsset().contains("POSTROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean SwipeAndselectAsset(TestParameters test) throws Exception {
		boolean result = true;

		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		allowAction = pageFactory.getAllow();

		try {
			result = result && appAssetsSelection.startAction("Skin Playback");
			Thread.sleep(3000);
			result = result && appAssetsSelection.swipeAsset("APP_ASSETS_ANDROID");
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(3000);
			result = result && allowAction.startAction("ALLOW");
			result = result && androidKeyCode.startAction("BACK");
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(3000);
			result = result && clickDiscoveryAction.clickPlayButton();
			result = result && elementValidator.handleLoadingSpinner();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean selectAsset(TestParameters test) throws Exception {
		boolean result = true;

		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		allowAction = pageFactory.getAllow();

		try {
			result = result && appAssetsSelection.startAction("Skin Playback");
			Thread.sleep(3000);
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(3000);
			result = result && allowAction.startAction("ALLOW");
			result = result && androidKeyCode.startAction("BACK");
			result = result && selectVideo.startAction(test.getAsset());
			Thread.sleep(3000);
			result = result && clickDiscoveryAction.clickPlayButton();
			result = result && elementValidator.handleLoadingSpinner();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean ooyalaAdsTest(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();

		try {
			if (test.getAsset().contains("PREROLL") || test.getAsset().contains("PREMIDPOST")
					|| test.getAsset().contains("WRAPPER")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			if (test.getAsset().contains("MIDROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekToEnd("SEEKBAR_ANDROID");
			result = result && elementValidator.handleLoadingSpinner();
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("POSTROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;

	}

	public boolean multiAdsTest(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		try {
			if (test.getAsset().contains("PREROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			if (test.getAsset().contains("MIDROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 30000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 30000);
			}
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekToEnd("SEEKBAR_ANDROID");
			result = result && elementValidator.handleLoadingSpinner();
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean vastAdsTest(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		try {
			if (test.getAsset().contains("PREROLL") || test.getAsset().contains("PREMIDPOST")
					|| test.getAsset().contains("WRAPPER")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			if (test.getAsset().contains("MIDROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			}
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekToEnd("SEEKBAR_ANDROID");
			result = result && elementValidator.handleLoadingSpinner();
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			if (test.getAsset().contains("POSTROLL")) {
				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 70000);
				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 70000);
			}
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean vast3AdsPoddedTest(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		try {
			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekToEnd("SEEKBAR_ANDROID");
			result = result && elementValidator.handleLoadingSpinner();
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;

	}

	public boolean vast3AdsTest(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		try {
			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 35000);
			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 35000);
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			result = result && clickDiscoveryAction.clickPauseButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && clickDiscoveryAction.seekToEnd("SEEKBAR_ANDROID");
			result = result && elementValidator.handleLoadingSpinner();
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && clickDiscoveryAction.clickPlayButton();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 80000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}
}
