package com.ooyala.playback.alice;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ControlBarValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPlayerControlsTests extends PlaybackWebTest {


    public PlaybackPlayerControlsTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testBasicPlaybackAlice(String testName, String url) throws OoyalaException {

        boolean result = false;
        PlayValidator play = pageFactory.getPlayValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        EventValidator eventValidator = pageFactory.getEventValidator();
        FullScreenValidator fullScreenValidator = pageFactory.getFullScreenValidator();
        ControlBarValidator controlBarValidator = pageFactory.getControlBarValidator();

        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();
            Thread.sleep(10000);

            injectScript();

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
