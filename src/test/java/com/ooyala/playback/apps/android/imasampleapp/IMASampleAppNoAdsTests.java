package com.ooyala.playback.apps.android.imasampleapp;

import com.ooyala.playback.apps.actions.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.validators.AdValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.relevantcodes.extentreports.LogStatus;

public class IMASampleAppNoAdsTests extends PlaybackAppsTest {
	
	private static Logger logger = Logger.getLogger(IMASampleAppNoAdsTests.class);

	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private PlayAction playAction;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private NotificationEventValidator notificationEventValidator;
	private AllowAction allowAction;
	private AndroidKeyCodeAction androidAction;
	private SwipeUpDownAppAssetsAction appAssetsSelection;

	@Test(groups = "imasampleapp", dataProvider = "testData")
	public void testBasicPlayer(String testName, TestParameters test) throws Exception {

		boolean result = true;

		try {
			if(test.getAsset().contains("IMA_NO_ADS")) {
				result = result && appAssetsSelection.swipeAsset("APP_ASSETS_ANDROID");
			}
			result = result && selectVideo.startAction(test.getAsset());
			result = result && allowAction.startAction("ALLOW");
			result = result && androidAction.startAction("BACK");
			result = result && selectVideo.startAction(test.getAsset());

			result = result && elementValidator.validate("PLAY_PAUSE_ANDROID", 30000);
			result = result && playAction.startAction("PLAY_PAUSE_ANDROID");

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
			
			result = result && notificationEventValidator.letVideoPlayForSec(2);
			
			result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED_ANDRD, 25000);
			result = result && seekAction.startAction("SEEK_BAR_ANDROID");
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
			result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED_ANDRD, 30000);

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 95000);

		} catch (Exception ex) {
			logger.error("Here is an exception" + ex);
			extentTest.log(LogStatus.FAIL, ex);
			result = false;
		}
		Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());

	}

}