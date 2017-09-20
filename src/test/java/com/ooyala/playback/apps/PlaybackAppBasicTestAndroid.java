package com.ooyala.playback.apps;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.actions.*;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

 public class PlaybackAppBasicTestAndroid extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(PlaybackAppsBasicTest.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private PauseAction pauseAction;
    private NotificationEventValidator notificationEventValidator;
    private PlayAction playAction;
    private SeekAction seekAction;
    private CCAction ccAction;
    private AllowAction allowAction;


    @Test(groups = "basicplaybacksampleapp", dataProvider = "testData")
    public void testPluginPlayer(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
            result = result && elementValidator.validate("HLS",20);
            result = result && selectVideo.startAction(test.getAsset());
            //result = result && allowAction.startAction("ALLOW");
            result = result && elementValidator.validate("PLAY_PAUSE_ANDROID",30000);
            result = result && playAction.startAction_Android("PLAY_PAUSE_ANDROID");
            Thread.sleep(10000);
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && elementValidator.validate("PLAY_PAUSE_ANDROID",3000);
            result = result && seekAction.seekVideoAndroid("SEEK_BAR_Android");

            result = result && elementValidator.validate("PLAY_PAUSE_ANDROID",3000);
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");


        }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }

}
