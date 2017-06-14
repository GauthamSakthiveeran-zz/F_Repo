package com.ooyala.playback.streams;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/21/16.
 */
public class BasicPlaybackTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(BasicPlaybackTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private StreamValidator streamTypeValidator;
    private SeekAction seekAction;

    public BasicPlaybackTests() throws OoyalaException {
        super();
    }

    @Test(groups = "streams", dataProvider = "testUrls")
    public void testBasicPlaybackStreams(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && eventValidator.playVideoForSometime(3);

            result = result && pause.validate("paused_1", 60000);

            if (!url.getVideoPlugins().equalsIgnoreCase("ADOBETVSDK")) {
                if (url.getStreamType() != null && !url.getStreamType().isEmpty()) {
                    result = result && eventValidator.validate("videoPlayingurl", 40000);
                    result = result
                            && streamTypeValidator.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);
                }
            }

            result = result && play.validate("playing_2", 60000);

            if (!testName.contains("Main Akamai HLS Remote Asset")
                    && !testName.contains("Bitmovin Akamai HLS Remote Asset")) {
                // live video
                if (testName.contains("Bitmovin Elemental Delta DASH Remote Asset")) {
                    result = result && seekAction.setTime(100).startAction();
                    result = result && eventValidator.validate("seeked_1", 60000);
                } else {
                    result = result && seek.validate("seeked_1", 60000);
                }
                result = result && eventValidator.validate("played_1", 120000);
            }

        } catch (Exception e) {
            logger.error("Exception while checking basic playback " + e.getMessage());
            extentTest.log(LogStatus.FAIL, e);
            result = false;
        }
        Assert.assertTrue(result, "Basic playback tests failed" + testName);
    }
}
