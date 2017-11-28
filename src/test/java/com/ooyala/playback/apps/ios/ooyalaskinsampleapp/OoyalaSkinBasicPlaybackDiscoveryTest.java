package com.ooyala.playback.apps.ios.ooyalaskinsampleapp;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.CCAction;
import com.ooyala.playback.apps.actions.DiscoveryAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.QAModeSwitchAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.validators.AdValidator;
import com.ooyala.playback.apps.validators.DiscoveryValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.ooyala.playback.apps.validators.PoddedAdValidator;

import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;

public class OoyalaSkinBasicPlaybackDiscoveryTest extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(OoyalaSkinBasicPlaybackDiscoveryTest.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private QAModeSwitchAction qaModeSwitchAction;
	private PlayAction playAction;
	private PauseAction pauseAction;
	private DiscoveryAction discoveryAction;
	private DiscoveryValidator discoveryValidator;
	

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
			result = result && discoveryAction.startAction("DISCOVERYBUTTON_IOS");
			result = result && discoveryValidator.validate("", 2000);
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());

	}


}
