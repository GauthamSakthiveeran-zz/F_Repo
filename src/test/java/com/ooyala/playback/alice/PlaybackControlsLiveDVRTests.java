package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackControlsLiveDVRTests extends PlaybackWebTest {

    private PlayValidator play;
    private PauseValidator pause;
    private LiveAction live;

    @DataProvider(name = "testUrls")
    public Object[][] getTestData() {

        return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
                nodeList);
    }

    public PlaybackControlsLiveDVRTests() throws OoyalaException {
        super();
    }

    @Test(groups = "alice", dataProvider = "testUrls")
    public void testControlLiveDVR(String testName, String url) throws OoyalaException {

        boolean result = false;
        /*PlayValidator play = pageFactory.getPlayValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        LiveAction live = pageFactory.getLiveAction();*/

        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();

            injectScript("http://10.11.66.55:8080/alice.js");

            play.validate("playing_1", 60);

            logger.info("Verified video playing");

            pause.validate("paused_1", 60);

            logger.info("verified video paused");

            play.validate("playing_2", 60);

            logger.info("verified video playing again");

            live.startAction();

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Alice basic playback tests failed");
    }
}
