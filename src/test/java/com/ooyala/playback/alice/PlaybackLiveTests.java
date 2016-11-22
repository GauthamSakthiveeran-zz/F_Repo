package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackLiveTests extends PlaybackWebTest {

    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private ControlBarValidator controlBarValidator;
    private FullScreenValidator fullScreenValidator;
    private LiveAction liveAction;

    public PlaybackLiveTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testHLSLive(String testName, String url) throws OoyalaException {

        boolean result = false;


        if (getBrowser().equalsIgnoreCase("safari")) {
            try {
                driver.get(url);
                if (!getPlatform().equalsIgnoreCase("android")) {
                    driver.manage().window().maximize();
                }

                play.waitForPage();

                injectScript(jsURL());


                play.validate("playing_1", 60);

                logger.info("video is playing");

                pause.validate("paused_1", 60);

                logger.info("video paused");

                controlBarValidator.validate("", 60);
                // to-do add ooyala logo to the test page

                fullScreenValidator.validate("FULLSCREEN_BTN_1", 60);

                logger.info("playing video in full screen");

                pause.validate("paused_2", 60);

                logger.info("video paused in fullscreen");

                play.validate("playing_2", 60);

                logger.info("video playing in fullscreen");

                liveAction.startAction();

                eventValidator.validate("played_1", 60);

                logger.info("video played");
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.assertTrue(result, "Playback HLSLive tests failed");
        } else {
            throw new SkipException("Test PlaybackHLSLive Is Skipped");
        }
    }
}
