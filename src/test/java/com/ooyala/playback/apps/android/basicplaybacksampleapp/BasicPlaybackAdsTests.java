package com.ooyala.playback.apps.android.basicplaybacksampleapp;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.*;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class BasicPlaybackAdsTests extends PlaybackAppsTest {
    private static Logger logger = Logger.getLogger(BasicPlaybackAdsTests.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private PlayAction playAction;
    private PauseAction pauseAction;
    private SeekAction seekAction;
    private CCAction ccAction;
    private NotificationEventValidator notificationEventValidator;


    @Test(groups = "basicplaybacksampleapp", dataProvider = "testData")
    public void testPluginPlayer(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
            result = result && selectVideo.startAction(test.getAsset());

            result = result && elementValidator.validate("PLAY_PAUSE_ANDROID", 30000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");

            if (test.getAsset().contains("VAST_AD_PRE_ROLL") || test.getAsset().contains("OOYALA_AD_PRE_ROLL")) {
                result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
                result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
            }

            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

            result = result && notificationEventValidator.letVideoPlayForSec(2);

            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED_ANDRD, 25000);
            result = result && seekAction.startAction("SEEK_BAR_ANDROID");
            result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
            result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);

            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED_ANDRD, 30000);
            if (test.getAsset().contains("VAST_AD_MID_ROLL") || test.getAsset().contains("VAST_AD_POST_ROLL") || test.getAsset().contains("OOYALA_AD_MID_ROLL") || test.getAsset().contains("OOYALA_AD_POST_ROLL")) {
                result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
                result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
            }
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 35000);

        } catch (Exception ex) {
            logger.error("Here is an exception" + ex);
            extentTest.log(LogStatus.FAIL, ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());
    }
}
