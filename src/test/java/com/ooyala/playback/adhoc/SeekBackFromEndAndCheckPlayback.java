package com.ooyala.playback.adhoc;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 8/7/17.
 */
public class SeekBackFromEndAndCheckPlayback extends PlaybackWebTest {
    public SeekBackFromEndAndCheckPlayback() throws OoyalaException {
        super();
    }
    private static Logger logger = Logger.getLogger(SeekBackFromEndAndCheckPlayback.class);
    private PlayValidator playValidator;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private SeekAction seekAction;

    @Test(dataProvider = "testUrls",groups = "adhoc")
    public void seekFromEndAndPlay(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.clearCache();
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && eventValidator.validate("playing_1",10000);
            result = result && seekAction.seek(10,true);
            result = result && eventValidator.validate("seeked_1",10000);
            result = result && eventValidator.validate("played_1",30000);
            result = result && seekAction.fromLast().setTime(10).startAction();
            result = result && eventValidator.validate("playing_2",10000);
        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "SeekBack From End And Check Playback :"+testName);
    }
}
