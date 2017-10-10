package com.ooyala.playback.apps.ios.pulseadsampleapp;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.relevantcodes.extentreports.LogStatus;

public class PulseSampleAppTests extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(PulseSampleAppTests.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private PlayAction playAction;

	@Test(groups = "pulsesampleapp", dataProvider = "testData")
	public void testBasicPlayer(String testName, TestParameters test) throws Exception {
		boolean result = true;
		try {

			result = result && selectVideo.startAction(test.getAsset());
			result = result && elementValidator.validate("NOTIFICATION_AREA", 1000);
			result = result && elementValidator.handleLoadingSpinner();
			result = result && playAction.startAction("PLAY_PAUSE_BUTTON");
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
			result = result && elementValidator.letVideoPlayForSec(3);
			result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON");
			result = result && seekAction.seekforward().setSlider("SLIDER").startAction("SEEK_BAR");
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
			result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON");
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 25000);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			extentTest.log(LogStatus.FAIL, ex);
			result = false;
		}
		Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());
	}
}