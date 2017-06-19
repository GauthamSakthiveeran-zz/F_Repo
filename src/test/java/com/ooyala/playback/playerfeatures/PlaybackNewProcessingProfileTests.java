package com.ooyala.playback.playerfeatures;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ControlBarValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackNewProcessingProfileTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackNewProcessingProfileTests.class);

    private PlayValidator play;
    private SeekAction seekAction;
    private PauseValidator pause;
    private ControlBarValidator control;
    private FullScreenValidator fullScreen;
    private EventValidator eventValidator;
    private StreamValidator streamTypeValidator;

    PlaybackNewProcessingProfileTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testNewProcessingProfile(String testName, UrlObject url) {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 30000);

            if (url.getStreamType() != null && !url.getStreamType().isEmpty()) {

                result = result && eventValidator.validate("videoPlayingurl", 40000);

                result = result && streamTypeValidator.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);
            }

            result = result && pause.validate("paused_1", 30000);

            result = result && control.validate("", 60000);

            result = result && play.validate("playing_2", 30000);

            result = result && fullScreen.getFullScreen();

            result = result && fullScreen.getNormalScreen();

            result = result && seekAction.seek(10, true);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "New Processing Profile Playback Tests failed" + testName);
    }

}
