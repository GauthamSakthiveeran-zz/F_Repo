package com.ooyala.playback.apps.ios.ooyalaskinsampleapp;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.CCAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.QAModeSwitchAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.validators.AdValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.ooyala.playback.apps.validators.PoddedAdValidator;

public class OoyalaSkinBasicPlaybackTest extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(OoyalaSkinBasicPlaybackTest.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private QAModeSwitchAction qaModeSwitchAction;
	private PlayAction playAction;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private CCAction ccAction;
	private AdValidator adValidator;
	private PoddedAdValidator poddedAdValidator;

	@Test(groups = "ooyalaskinsampleapp", dataProvider = "testData")
	public void testBasicPlayer(String testName, TestParameters test) throws Exception {
		Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
		logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
		boolean result = true;
		selectVideo.isAppV4(test.getApp());
		try {
			result = result && selectVideo.startAction("BASIC_PLAYBACK");
			result = result && qaModeSwitchAction.startAction("QA_MODE_SWITCH");
			result = result && selectVideo.startAction(test.getAsset());
			result = result && playAction.startAction("PLAY_BUTTON_V4_IOS");
			result = result && elementValidator.validate("NOTIFICATION_AREA", 1000);
			result = result && elementValidator.handleLoadingSpinner();		
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
			result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON_V4_IOS");
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 35000);
			if (test.getAsset().contains("CC")) {
				result = result && ccAction.enableCC(); // Default English
				result = result && notificationEventValidator.verifyEvent(Events.CC_ENABLED, 15000);
			}
			if (test.getAsset().contains("VERTICAL"))
				result = result && seekAction.setSlider("SLIDER_V4").seekforward().startAction("SEEK_BAR_V4"); // SeekFrwd
			else
				result = result && seekAction.setSlider("SLIDER_V4").startAction("SEEK_BAR_V4"); // SeekBack
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
			result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON_V4_IOS"); // ResumePLay
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 90000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());

	}

	@Test(groups = "basicplaybacksampleapp", dataProvider = "testData")
	public void testAdPlayer(String testName, TestParameters test) throws Exception {
		Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
		logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
		boolean result = true;
		selectVideo.isAppV4(test.getApp());
		try {
			result = result && selectVideo.startAction("BASIC_PLAYBACK");
			result = result && qaModeSwitchAction.startAction("QA_MODE_SWITCH");
			result = result && selectVideo.startAction(test.getAsset());
			result = result && elementValidator.validate("NOTIFICATION_AREA", 1000);
			result = result && elementValidator.handleLoadingSpinner();
			result = result && playAction.startAction("PLAY_BUTTON_V4_IOS");
			if(test.getAsset().contains("PODDED"))
				result = result && poddedAdValidator.setNoOfAds(test.getDescription().split(" ")[0]).setTestParameters(test).validate("", 1000);
			else
				result = result && adValidator.setTestParameters(test).validate("", 1000);
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 90000);
			
		} catch (Exception ex) {
			logger.error("Here is an exception" + ex);
			result = false;
		}
		Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());

	}

}
