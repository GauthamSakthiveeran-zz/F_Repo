package com.ooyala.playback.apps.android.ooyalapisampleapp;

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
import org.testng.annotations.*;


public class PlaylistAPI extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(PlaylistAPI.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private PlayAction playAction;
    private PauseAction pauseAction;
    private SeekAction seekAction;
    private CCAction ccAction;
    private NotificationEventValidator notificationEventValidator;
    private AllowAction allowAction;
    private AndroidKeyCodeAction androidAction;

    @Test(groups = "ooyalaapisampleapp", dataProvider = "testData")
    public void testPluginPlayer(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
            result = result && selectVideo.startAction("PLAYLIST_API");
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
/*            if(test.getAsset().contains("VOD_WITH_COLSEDCAPTION")) {
                result = result && ccAction.enableCC(); // Default English
                result = result && notificationEventValidator.verifyEvent(Events.CC_ENABLED, 15000);
            }*/
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED_ANDRD, 30000);

            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 250000);

        } catch (Exception ex) {
            logger.error("Here is an exception" + ex);
            extentTest.log(LogStatus.FAIL, ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:" + test.getApp() + "->Asset:" + test.getAsset());
    }
}

