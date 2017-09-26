package com.ooyala.playback.apps.ios.freewheelsampleapp;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.validators.AdValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.relevantcodes.extentreports.LogStatus;

public class FreewheelSampleAppTests extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(FreewheelSampleAppTests.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private AdValidator adValidator;

	@Test(groups = "freewheelsampleapp", dataProvider = "testData")
	public void testBasicPlayer(String testName, TestParameters test) throws Exception {
		boolean result = true;
		try {
			result = result && selectVideo.startAction(test.getAsset());
			
			result = result && elementValidator.validate("NOTIFICATION_AREA", 1000);
			
			result = result && elementValidator.handleLoadingSpinner();

			result = result && adValidator.validate("", 1000);

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 25000);

		} catch (Exception ex) {
			logger.error("Here is an exception" + ex);
			extentTest.log(LogStatus.FAIL, ex);
			result = false;
		}
		Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());
	}
}