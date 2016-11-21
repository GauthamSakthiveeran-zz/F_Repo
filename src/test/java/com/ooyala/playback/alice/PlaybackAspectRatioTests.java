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

import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackAspectRatioTests extends PlaybackWebTest {

    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private PlayAction playAction;
    private AspectRatioValidator aspectRatioValidator;

    @DataProvider(name = "testUrls")
    public Object[][] getTestData() {

        return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
                nodeList);
    }

    public PlaybackAspectRatioTests() throws OoyalaException {
        super();
    }

    @Test(groups = "alice", dataProvider = "testUrls")
    public void testAspectRation(String testName, String url) throws OoyalaException {

        boolean result = false;
        /*PlayValidator play = pageFactory.getPlayValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        PlayAction playAction = pageFactory.getPlayAction();
        EventValidator eventValidator = pageFactory.getEventValidator();
        AspectRatioValidator aspectRatioValidator = pageFactory.getAspectRatioValidator();*/

        try {
            driver.get(url);

            play.waitForPage();

            injectScript("http://10.11.66.55:8080/alice.js");

            play.validate("playing_1", 60);

            logger.info("Verified that video is playing");
            sleep(2000);

           aspectRatioValidator.validate("assetDimension_1",60);

            pause.validate("paused_1", 60);

            logger.info("Verirfied that video is getting paused");

            playAction.startAction();
            //add fullscreen functionality

            seek.validate("seeked_1", 60);

            logger.info("Verified that video is seeked");

            aspectRatioValidator.validate("assetDimension_1",60);

            eventValidator.validate("videoPlayed_1", 60);

            logger.info("Verified that video is played");

    } catch (Exception e) {
        e.printStackTrace();

    }
    Assert.assertTrue(result, "Aspect ratio tests failed");

}

}
