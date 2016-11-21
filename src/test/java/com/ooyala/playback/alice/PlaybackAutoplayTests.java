package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackAutoplayTests extends PlaybackWebTest {

    private EventValidator eventValidator;
    private PlayValidator play;
    private SeekValidator seek;
    private AutoplayAction autoplayAction;

    @DataProvider(name = "testUrls")
    public Object[][] getTestData() {

        return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
                nodeList);
    }

    public PlaybackAutoplayTests() throws OoyalaException {
        super();
    }

    @Test(groups = "alice", dataProvider = "testUrls")
    public void testAutoPlay(String testName, String url) throws OoyalaException {

        boolean result = false;
        /*PlayValidator play = pageFactory.getPlayValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        AutoplayAction autoplayAction = pageFactory.getAutoplay();
        EventValidator eventValidator = pageFactory.getEventValidator();*/

        if (getPlatform().equalsIgnoreCase("Android")) {
            throw new SkipException("Test PlaybackAutoplayTests Is Skipped");
        } else {
            try {
                driver.get(url);
                driver.manage().window().maximize();

                play.waitForPage();

                injectScript("http://10.11.66.55:8080/alice.js");

                autoplayAction.startAction();
                try {
                    eventValidator.validate("singleAdPlayed_1",50);
                } catch (Exception e) {
                    logger.info("No Preroll ad present in this autoplay video");
                }
                play.validate("playing_1", 60);

                logger.info("Verifed that video is getting playing");

                sleep(500);

                seek.validate("seeked_1", 60);

                logger.info("Verified that video is seeked");

                eventValidator.validate("played_1", 60);

                logger.info("Verified that video is played");

                result = true;
            } catch (Exception e) {
                e.printStackTrace();

            }
            Assert.assertTrue(result, "Alice basic playback tests failed");
        }
    }
}
