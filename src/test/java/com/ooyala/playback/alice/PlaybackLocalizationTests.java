package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayPauseAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackLocalizationTests extends PlaybackWebTest {

    public static Logger logger = Logger.getLogger(PlaybackLocalizationTests.class);

    @DataProvider(name = "testUrls")
    public Object[][] getTestData() {

        return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
                nodeList);
    }

    public PlaybackLocalizationTests() throws OoyalaException {
        super();
    }

    @Test(groups = "PlayerSkin", dataProvider = "testUrls")
    public void testPlaybackLocalization(String testName, String url) throws OoyalaException {

        boolean result = false;
        PlayValidator play = pageFactory.getPlayValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        PlayPauseAction playPauseAction = pageFactory.getPlayPauseAction();
        EventValidator eventValidator = pageFactory.getEventValidator();
        ShareTabValidator shareTabValidator = pageFactory.getShareTabValidator();

        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();

            injectScript(jsURL());

            play.validate("playing_1", 60);

            logger.info("video playing");

            pause.validate("paused_1", 60);

            logger.info("video paused");

            play.validate("playing_2", 60);

            logger.info("video paying again");

            shareTabValidator.validate("",60);

            eventValidator.eventAction("FULLSCREEN_BTN");

            logger.info("checked fullscreen");

            shareTabValidator.validate("",60);

            eventValidator.eventAction("NORMAL_SCREEN");

            playPauseAction.startAction();

            seek.validate("seeked_1", 60);

            logger.info("video seeked");

            eventValidator.validate("played_1",60);

            logger.info("video played");

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Playback Localization tests failed");
    }
}

