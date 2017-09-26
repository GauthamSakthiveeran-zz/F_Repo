package com.ooyala.playback.apps.android.optionssampleapp;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.AllowAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.FileEventValidator;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

 public class CuePointsAndAdControls extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(CuePointsAndAdControls.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private NotificationEventValidator notificationEventValidator;
    private PauseAction pauseAction;
    private PlayAction playAction;
    private SeekAction seekAction;
    private AllowAction allowAction;
    


    @Test(groups = "optionssampleapp", dataProvider = "testData")
    public void testCuePointsAndAdControls(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {

            result = result && selectVideo.startAction(test.getAsset());
            result = result && allowAction.startAction("ALLOW");
            result = result && playAction.createVideo("CREATE_VIDEO",20000);
            Thread.sleep(15000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
            result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED, 70000);
            result = result && seekAction.startAction("SEEK_BAR_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);       
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
            result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,  40000);
            result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 40000);
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  30000);
        }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }

}
