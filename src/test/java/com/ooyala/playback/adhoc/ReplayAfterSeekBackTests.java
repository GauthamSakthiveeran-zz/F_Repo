package com.ooyala.playback.adhoc;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 8/9/17.
 */
public class ReplayAfterSeekBackTests extends PlaybackWebTest {
    public ReplayAfterSeekBackTests() throws OoyalaException {
        super();
    }
    private static Logger logger = Logger.getLogger(ReplayAfterSeekBackTests.class);
    private PlayValidator playValidator;
    private EventValidator eventValidator;
    private SeekValidator seek;
    private SeekAction seekAction;
    private ReplayValidator replay;

    @Test(dataProvider = "testUrls",groups = "adhoc")
    public void replayAfterSeekBack(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playValidator.validate("playing_1",10000);
            result = result && eventValidator.playVideoForSometime(5);
            result = result && seek.validate("seeked_1",40000);
            result = result && eventValidator.validate("PostRoll_willPlayAds",30000);
            result = result && eventValidator.validate("played_1",50000);
            result = result && seekAction.seek(20,true);
            result = result && eventValidator.validate("played_2",60000);
            result = result && replay.validate("replay_1",20000);

        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Replay After Seek Back :"+testName);
    }
}