package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackHLSLiveTests extends PlaybackWebTest {

    @DataProvider(name = "testUrls")
    public Object[][] getTestData() {

        return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
                nodeList);
    }

    public PlaybackHLSLiveTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testHLSLive(String testName, String url) throws OoyalaException {

        boolean result = false;
        PlayValidator play = pageFactory.getPlayValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        EventValidator eventValidator = pageFactory.getEventValidator();
        ControlBarValidator controlBarValidator = pageFactory.getControlBarValidator();
        FullScreenValidator fullScreenValidator = pageFactory.getFullScreenValidator();
        LiveAction liveAction = pageFactory.getLiveAction();

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

                controlBarValidator.validate("",60);
                //to-do add ooyala logo to the test page

                fullScreenValidator.validate("FULLSCREEN_BTN_1",60);

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
