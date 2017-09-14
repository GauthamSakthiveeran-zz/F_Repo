package com.ooyala.playback.apps;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

public class PlaybackAppsBasicTest extends PlaybackAppsTest {
	
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	

	@Test(groups = "basicplaybacksampleapp", dataProvider = "testData")
    public  void testPluginPlayer(String testName, TestParameters test) throws Exception {
		Reporter.log("Executing:"+test.getApp()+"->Asset:"+test.getAsset());
		boolean result = true;
		
		try {
			result = result && selectVideo.startAction(test.getAsset());
			result = result && elementValidator.validate("NOTIFICATION_AREA", 1000);
			result = result && elementValidator.handleLoadingSpinner();
			result = result && notificationEventValidator.validate("AD_STARTED", 25000);

		} catch(Exception ex) {
			logger.error("Here is an exception"+ex);
			result = false;
		}

		Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());
	
    }
}
