package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class PlaybackPlayerControlsTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackPlayerControlsTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private FullScreenValidator fullScreenValidator;
    private ControlBarValidator controlBarValidator;
    private PlayAction playAction;

    public PlaybackPlayerControlsTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testBasicPlaybackAlice(String testName, String url)
            throws OoyalaException {

        boolean result = true;

        try{
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            result = result && eventValidator.loadingSpinner();

            result = result && eventValidator.validate("playing_1",20000);

            result = result && pause.validate("paused_1", 60000);

            if (!(getBrowser().equalsIgnoreCase("safari")
                    || getBrowser().equalsIgnoreCase("firefox")
                    || getBrowser().equalsIgnoreCase("internet explorer") || getPlatform()
                    .equalsIgnoreCase("Android"))) {
                result = result && fullScreenValidator.validate("", 60000);
            }

            result = result && controlBarValidator.validate("", 60000);

            result = result && playAction.startAction();

            result = result && eventValidator.validate("playing_2", 60000);

            result = result && seek.validate("seeked_1", 60000);

            logger.info("Verified that video is seeked");

            result = result && eventValidator.validate("played_1", 60000);

            logger.info("Verified that video is played");

        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Alice basic playback tests failed");
    }
}
