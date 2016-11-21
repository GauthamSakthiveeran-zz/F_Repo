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

/**
 * Created by jitendra on 21/11/16.
 */
public class PlaybackLiveSSITests extends PlaybackWebTest{

    private PlayValidator play;
    private PauseValidator pause ;
    private PlayAction playAction ;
    private PauseAction pauseAction ;
    private EventValidator eventValidator;
    private FullScreenValidator fullScreenValidator;


    public PlaybackLiveSSITests() throws OoyalaException {
        super();
    }

    @Test(groups = "alice", dataProvider = "testUrls")
    public void testLiveSSI(String testName, String url) throws OoyalaException {

        boolean result = false;
        /*PlayValidator play = pageFactory.getPlayValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
        PlayAction playAction = pageFactory.getPlayAction();
        PauseAction pauseAction = pageFactory.getPauseAction();
        EventValidator eventValidator = pageFactory.getEventValidator();
        FullScreenValidator fullScreenValidator = pageFactory.getFullScreenValidator();*/

        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            boolean isPageLoaded = play.waitForPage();

            Thread.sleep(15000);

            injectScript("http://10.10.9.96:8080/alice_full.js");

            play.validate("playing_1", 60);

            logger.info("Verifed that video is getting playing");

            Thread.sleep(2000);

            pause.validate("paused_1", 60);

            logger.info("Verified that video is getting pause");

            play.validate("playing_2", 60);

            fullScreenValidator.validate("FULLSCREEN_BTN",60);


            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Alice basic playback tests failed");
    }

}
