package com.ooyala.playback.streams;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;


public class BasicPlaybackTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(BasicPlaybackTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekAction seekAction;
    private StreamValidator streamTypeValidator;
    private ReplayValidator replayValidator;
    private PlayAction playAction;

    public BasicPlaybackTests() throws OoyalaException {
        super();
    }

    @Test(groups = "streams", dataProvider = "testUrls")
    public void testBasicPlaybackStreams(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;
        
        try {
            driver.get(url.getUrl());
            streamTypeValidator.loadScriptForAdobe();
            result = result && play.waitForPage();
            injectScript();
            result = result && play.validate("playing_1", 60000);
            result = result && eventValidator.playVideoForSometime(3);
            result = result && pause.validate("paused_1", 60000);
            streamTypeValidator.setStreamType(url.getStreamType()).validate("", 1000);
            result = result && playAction.startAction();
            result = result && seekAction.seekTillEnd().startAction();
        	result = result && eventValidator.validate("playing_3", 10000);
            result = result && eventValidator.validate("played_1", 120000);
            result = result && replayValidator.validate("replay_1", 30000);
            eventValidator.validatePlayStartTimeFromBeginningofVideo();
            result = result && eventValidator.playVideoForSometime(3);
            result = result && seekAction.seekToMid().startAction();
            result = result && eventValidator.validate("playing_5", 10000);
            result = result && seekAction.setTime(2).startAction();
            result = result && eventValidator.validate("playing_6", 10000);
            result = result && eventValidator.playVideoForSometime(5);
            result = result && seekAction.seekTillEnd().startAction();
            result = result && eventValidator.validate("played_3", 150000);

        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Exception while checking basic playback " + e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "Basic playback tests failed" + testName);
    }
}
