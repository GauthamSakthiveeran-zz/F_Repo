package com.ooyala.playback.apps.android;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.ios.PlaybackAppsBasicTest;
import com.ooyala.playback.apps.validators.ElementValidator;

 public class PlaybackAppBasicTest extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(PlaybackAppsBasicTest.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private PauseAction pauseAction;
    private PlayAction playAction;
    private SeekAction seekAction;


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
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            Thread.sleep(10000);
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && elementValidator.validate("PLAY_PAUSE_ANDROID",3000);
            result = result && seekAction.startAction("SEEK_BAR_ANDROID");

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
