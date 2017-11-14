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
import com.ooyala.playback.apps.actions.DiscoveryAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.ResizablePlayerAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.SwipeUpDownAppAssetsAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.validators.AdValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.FullScreenOrientationValidator;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.ooyala.playback.apps.validators.PoddedAdValidator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

public class OoyalaSkinSampleAppSkinPlaybackUtils extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(OoyalaSkinSampleAppSkinPlaybackUtils.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private DiscoveryAction clickDiscoveryAction;
	private PlayAction playAction;
	private AndroidKeyCodeAction androidKeyCode;
	private AllowAction allowAction;
	private SwipeUpDownAppAssetsAction appAssetsSelection;
	private PoddedAdValidator poddedAdValidator;
	private AdValidator adValidator;
	private ResizablePlayerAction resizablePlayerAction;
	private FullScreenOrientationValidator orientationValidation;
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

	public boolean selectResizablePlayer() throws Exception {
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
		resizablePlayerAction = pageFactory.getResizablePlayerAction();
		orientationValidation = pageFactory.getFullScreenOrientationValidator();

		try {
			
			result = result && appAssetsSelection.startAction("Resizable Skin Player");
			Thread.sleep(3000);
			result = result && clickDiscoveryAction.clickPlayButton();
			result = result && resizablePlayerAction.startAction("RESIZE_TALL");
			result = result && resizablePlayerAction.validateTallPlayer();
			result = result && orientationValidation.changeOrientation("LANDSCAPE");
			result = result && orientationValidation.isOrientationCorrect("LANDSCAPE");
			result = result && orientationValidation.changeOrientation("PORTRAIT");
			result = result && orientationValidation.isOrientationCorrect("PORTRAIT");
			result = result && androidKeyCode.startAction("BACK");
			result = result && appAssetsSelection.startAction("Resizable Skin Player");
			Thread.sleep(3000);
			result = result && clickDiscoveryAction.clickPlayButton();
			result = result && resizablePlayerAction.startAction("RESIZE_WIDE");
			result = result && resizablePlayerAction.validateWidePlayer();
			result = result && orientationValidation.changeOrientation("LANDSCAPE");
			result = result && orientationValidation.isOrientationCorrect("LANDSCAPE");
			result = result && orientationValidation.changeOrientation("PORTRAIT");
			result = result && orientationValidation.isOrientationCorrect("PORTRAIT");
			result = result && androidKeyCode.startAction("BACK");
			result = result && appAssetsSelection.startAction("Resizable Skin Player");
			Thread.sleep(3000);
			result = result && clickDiscoveryAction.clickPlayButton();
			result = result && resizablePlayerAction.startAction("RESIZE_FILLSCREEN");
			result = result && resizablePlayerAction.validateFillScreenPlayer();
			result = result && orientationValidation.changeOrientation("LANDSCAPE");
			result = result && orientationValidation.isOrientationCorrect("LANDSCAPE");
			result = result && orientationValidation.changeOrientation("PORTRAIT");
			result = result && orientationValidation.isOrientationCorrect("PORTRAIT");

			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}
	public boolean ooyalaAdsTest(TestParameters test) throws Exception {
		boolean result = true;
		adValidator	 = pageFactory.getAdEventValidator();
		try {
			result = result && adValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).validate("", 2000);
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
		adValidator	 = pageFactory.getAdEventValidator();
		try {
			result = result && adValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).validate("", 2000);
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
		adValidator	 = pageFactory.getAdEventValidator();
		try {
			result = result && adValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).validate("", 2000);
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
		poddedAdValidator = pageFactory.getPoddedAdValidator();
		try {
			result = result && poddedAdValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).setNoOfAds("2").validate("", 2000);
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
		adValidator	 = pageFactory.getAdEventValidator();
		try {
			result = result && adValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).validate("", 2000);
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 80000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}
	
	public boolean vampAdsTest(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		adValidator	 = pageFactory.getAdEventValidator();
		try {
			result = result && adValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).validate("", 2000);
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}
}
