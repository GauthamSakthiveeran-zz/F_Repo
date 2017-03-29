package com.ooyala.playback.streams;

import com.ooyala.playback.page.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
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
    private StreamTypeValidator streamTypeValidator;
    public String stream;

    public BasicPlaybackTests() throws OoyalaException {
        super();
    }

    @Test(groups = "streams", dataProvider = "testUrls")
    public void testBasicPlaybackAlice(String testName, String url)
            throws OoyalaException {
        String description = testName.split("-")[1].trim();

        boolean result = true;

        try {
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && eventValidator.validate("videoPlayingurl",40000);

            result = result && streamTypeValidator.validateStream("videoPlayingurl",description);

            result = result && pause.validate("paused_1", 60000);

            result = result && play.validate("playing_2", 60000);

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("played_1", 60000);

        }catch (Exception e) {
            logger.error("Exception while checking basic playback "+e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Basic playback tests failed"+testName);
    }
}
