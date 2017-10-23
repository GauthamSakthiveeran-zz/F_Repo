package com.ooyala.playback.apps.android.completesampleapp;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.*;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class OptionsSampleAppTests extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(OptionsSampleAppTests.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private NotificationEventValidator notificationEventValidator;
    private PauseAction pauseAction;
    private PlayAction playAction;
    private SeekAction seekAction;
    private AllowAction allowAction;
    private AndroidKeyCodeAction androidAction;


    @Test(groups = "completesampleapp", dataProvider = "testData")
    public void testCuePointsAndAdControls(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {

            result = result && selectVideo.startAction("PLAYER_CONFIGURATION_WITH_OPTIONS");
            result = result && selectVideo.startAction(test.getAsset());
            result = result && allowAction.startAction("ALLOW");
            result = result && androidAction.startAction("BACK");
            result = result && selectVideo.startAction(test.getAsset());
            result = result && playAction.createVideo("CREATE_VIDEO",20000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
            result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
            result = result && seekAction.startAction("SEEK_BAR_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
            result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,  40000);
            result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 40000);
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  80000);
        }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }


    @Test(groups = "completesampleapp", dataProvider = "testData")
    public void preloadWithInitialTime(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
            result = result && selectVideo.startAction("PLAYER_CONFIGURATION_WITH_OPTIONS");
            result = result && selectVideo.startAction(test.getAsset());
            result = result && allowAction.startAction("ALLOW");
            result = result && androidAction.startAction("BACK");
            result = result && selectVideo.startAction(test.getAsset());
            result = result && playAction.createVideo("CREATE_VIDEO",20000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  80000);
        }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }

    @Test(groups = "completesampleapp", dataProvider = "testData")
    public void serverSideTvRatings(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
            result = result && selectVideo.startAction("PLAYER_CONFIGURATION_WITH_OPTIONS");
            result = result && selectVideo.startAction(test.getAsset());
            result = result && allowAction.startAction("ALLOW");
            result = result && androidAction.startAction("BACK");
            result = result && selectVideo.startAction(test.getAsset());
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
            result = result && seekAction.startAction("SEEK_BAR_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  80000);
        }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }


    @Test(groups = "completesampleapp", dataProvider = "testData")
    public void  tvratingsConfig(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
            result = result && selectVideo.startAction("PLAYER_CONFIGURATION_WITH_OPTIONS");
            result = result && selectVideo.startAction(test.getAsset());
            result = result && allowAction.startAction("ALLOW");
            result = result && androidAction.startAction("BACK");
            result = result && selectVideo.startAction(test.getAsset());
            result = result && playAction.createVideo("CREATE_VIDEO",20000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
            result = result && seekAction.startAction("SEEK_BAR_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 80000);
        }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }


}
