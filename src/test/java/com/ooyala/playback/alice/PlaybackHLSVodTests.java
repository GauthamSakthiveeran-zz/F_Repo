package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static java.lang.Thread.sleep;
import static org.openqa.selenium.By.id;
import static org.testng.Assert.assertEquals;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackHLSVodTests extends PlaybackWebTest {

    @DataProvider(name = "testUrls")
    public Object[][] getTestData() {

        return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
                nodeList);
    }

    public PlaybackHLSVodTests() throws OoyalaException {
        super();
    }

    @Test(groups = "alice", dataProvider = "testUrls")
    public void testHLSVod(String testName, String url) throws OoyalaException {

        boolean result = false;
        PlayValidator play = pageFactory.getPlayValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        EventValidator eventValidator = pageFactory.getEventValidator();
        FullScreenValidator fullScreenValidator = pageFactory.getFullScreenValidator();
        CCValidator ccValidator = pageFactory.getCCValidator();
        ShareTabValidator shareTabValidator = pageFactory.getShareTabValidator();

        if (getBrowser().equalsIgnoreCase("safari")) {
            try {
                driver.get(url);
                if (!getPlatform().equalsIgnoreCase("android")) {
                    driver.manage().window().maximize();
                }

                play.waitForPage();

                injectScript("http://10.11.66.55:8080/alice.js");

                play.validate("playing_1", 60);

                logger.info("video is playing");

                pause.validate("paused_1", 60);

                logger.info("video paused");

                play.validate("playing_2", 60);

                logger.info("video is playing again");

                sleep(3000);

               fullScreenValidator.validate("",60);

                ccValidator.validate("cclanguage",60);

                logger.info("verified cc languages");

                shareTabValidator.validate("",60);

                seek.seek("pp.getDuration()/2");

                eventValidator.validate("played_1",60);

                logger.info("video played");

                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.assertTrue(result, "Playback HLS Vod tests failed");

        } else {
            throw new SkipException("Test PlaybackHLSVod Is Skipped");
        }
    }
}
