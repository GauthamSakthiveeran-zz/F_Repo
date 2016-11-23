package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PlaybackPlayerControlsTests extends PlaybackWebTest {

    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private FullScreenValidator fullScreenValidator;
    private ControlBarValidator controlBarValidator;

    public PlaybackPlayerControlsTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testBasicPlaybackAlice(String testName, String url) throws OoyalaException {

        boolean result = false;

        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();
            Thread.sleep(10000);

            injectScript(jsURL());

            play.validate("playing_1", 60);

            logger.info("Verifed that video is getting playing");

            Thread.sleep(2000);

            pause.validate("paused_1", 60);

            logger.info("Verified that video is getting pause");

            play.validate("playing_2", 60);

            fullScreenValidator.validate("",60);

            controlBarValidator.validate("",60);

            seek.validate("seeked_1", 60);

            logger.info("Verified that video is seeked");

            eventValidator.validate("played_1",60);

            logger.info("Verified that video is played");

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Alice basic playback tests failed");
    }
}
