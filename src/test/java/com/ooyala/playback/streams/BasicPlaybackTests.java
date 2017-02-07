package com.ooyala.playback.streams;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/21/16.
 */
public class BasicPlaybackTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(BasicPlaybackTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;

    public BasicPlaybackTests() throws OoyalaException {
        super();
    }

    @Test(groups = "streams", dataProvider = "testUrls")
    public void testBasicPlaybackAlice(String testName, String url)
            throws OoyalaException {

        boolean result = true;

        logger.info("Test Description : " + testName.split(":")[1].toLowerCase() + "\n" + url);

        try {
            driver.get(url);

            result = result && play.waitForPage();

            Thread.sleep(5000);

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && eventValidator.loadingSpinner();

            Thread.sleep(2000);

            result = result && pause.validate("paused_1", 60000);

            logger.info("Verified that video is getting pause");

            result = result && play.validate("playing_2", 60000);

            result = result && seek.validate("seeked_1", 60000);

            logger.info("Verified that video is seeked");

            result = result && eventValidator.validate("played_1", 60000);

            logger.info("Verified that video is played");

        }catch (Exception e) {
            logger.error("Exception while checking basic playback "+e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Basic playback tests failed"+testName);
    }
}