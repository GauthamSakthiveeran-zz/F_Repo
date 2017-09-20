package com.ooyala.playback.apps.ios;

import org.apache.log4j.Logger;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.CCAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.ios.SeekAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

public class PlaybackAppsBasicTest extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(PlaybackAppsBasicTest.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private NotificationEventValidator notificationEventValidator;
    private PlayAction playAction;
    private SeekAction seekAction;
    private CCAction ccAction;


    @Test(groups = "basicplaybacksampleapp", dataProvider = "testData")
    public  void testPluginPlayer(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:"+test.getApp()+"->Asset:"+test.getAsset());
        logger.info("Executing:"+test.getApp()+"->Asset:"+test.getAsset());
        boolean result = true;
        try{
            result = result && selectVideo.startAction(test.getAsset());
            result = result && elementValidator.validate("NOTIFICATION_AREA", 1000);
            //result = result && elementValidator.handleLoadingSpinner();
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED,"Playback has been started", 25000);
            result = result && playAction.startAction("PLAY_PAUSE_BUTTON");
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, "Playback has been paused", 35000);
            if(test.getAsset().contains("CC")){
            		result = result && ccAction.enableCC(); //Default English
            		result = result && notificationEventValidator.verifyEvent(Events.CC_ENABLED,"Language has been changed", 15000);
            }

            result = result && seekAction.seekforward().setSlider("SLIDER").startAction("SEEK_BAR");
            result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, "Video seek has been started", 40000);
            result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, "Video seek has been Completed", 40000);
            result = result && playAction.startAction("PLAY_PAUSE_BUTTON");
            result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, "Video playback has been completed", 50000);

        } catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());


    }

}
