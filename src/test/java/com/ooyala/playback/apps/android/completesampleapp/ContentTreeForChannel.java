package com.ooyala.playback.apps.android.completesampleapp;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.*;
import com.ooyala.playback.apps.android.ooyalapisampleapp.PlaylistAPI;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class ContentTreeForChannel extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(ContentTreeForChannel.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private PlayAction playAction;
    private PauseAction pauseAction;
    private SeekAction seekAction;
    private CCAction ccAction;
    private NotificationEventValidator notificationEventValidator;

    @Test(groups = "completesampleapp", dataProvider = "testData")
    public void testPluginPlayer(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
            result = result && selectVideo.startAction("OOYALA_API_SAMPLE");
            result = result && selectVideo.startAction("CONTENT_TREE_FOR_CHANNEL");
            result = result && selectVideo.startAction(test.getAsset());

            result = result && elementValidator.validate("PLAY_PAUSE_ANDROID", 30000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");

            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

            result = result && notificationEventValidator.letVideoPlayForSec(4);

            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
            result = result && seekAction.startAction("SEEK_BAR_ANDROID");
            result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
            result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);

            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 25000);

        } catch (Exception ex) {
            logger.error("Here is an exception" + ex);
            extentTest.log(LogStatus.FAIL, ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());
    }
}