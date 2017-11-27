package com.ooyala.playback.apps.android.completesampleapp;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.validators.AdValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FreewheelSampleAppTests extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(com.ooyala.playback.apps.android.freewheelsampleapp.FreewheelSampleAppTests.class);

    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private PlayAction playAction;
    private AdValidator adValidator;
    private NotificationEventValidator notificationEventValidator;

    @Test(groups = "completesampleapp", dataProvider = "testData")
    public void testBasicPlayer(String testName, TestParameters test) throws Exception {

        boolean result = true;

        try {
            result = result && selectVideo.startAction("FREEWHEEL_INTEGRATION");
            result = result && selectVideo.startAction(test.getAsset());

            result = result && elementValidator.validate("PLAY_PAUSE_ANDROID", 30000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");

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