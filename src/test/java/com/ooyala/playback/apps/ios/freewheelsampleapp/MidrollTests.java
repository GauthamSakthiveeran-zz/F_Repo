package com.ooyala.playback.apps.ios.freewheelsampleapp;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.ios.SeekAction;
import com.ooyala.playback.apps.validators.AdEventValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.ooyala.playback.apps.validators.PauseValidator;
import com.ooyala.playback.apps.validators.SeekValidator;
import com.relevantcodes.extentreports.LogStatus;

public class MidrollTests extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(MidrollTests.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private SeekAction seekAction;
	private PlayAction playAction;
	private AdEventValidator adEventValidator;
	private SeekValidator seekValidator;
	private PauseValidator pause;

	@Test(groups = "freewheelsampleapp", dataProvider = "testData")
	public void testBasicPlayer(String testName, TestParameters test) throws Exception {
		boolean result = true;
		try {
			result = result && selectVideo.startAction(test.getAsset());
			
			result = result && elementValidator.validate("NOTIFICATION_AREA", 1000);
			
			result = result && elementValidator.handleLoadingSpinner();

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

			result = result && notificationEventValidator.letVideoPlayForSec(3);
			
			result = result && pause.validate("PLAY_PAUSE_BUTTON", 35000);

			result = result && seekAction.setSlider("SLIDER").startAction("SEEK_BAR");

			result = result && seekValidator.validate("", 1000);

			result = result && adEventValidator.validate("", 1000);

			result = result && playAction.startAction("PLAY_PAUSE_BUTTON");

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 25000);

		} catch (Exception ex) {
			logger.error("Here is an exception" + ex);
			extentTest.log(LogStatus.FAIL, ex);
			result = false;
		}
		Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());
	}
}
