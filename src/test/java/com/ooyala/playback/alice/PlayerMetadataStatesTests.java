package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlayerMetadataStatesTests extends PlaybackWebTest {

        @DataProvider(name = "testUrls")
        public Object[][] getTestData() {

        return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
                nodeList);
    }

        public PlayerMetadataStatesTests() throws OoyalaException {
        super();
    }

        @Test(groups = "alice", dataProvider = "testUrls")
        public void testBasicPlaybackAlice(String testName, String url) throws OoyalaException {

        boolean result = false;
        PlayValidator play = pageFactory.getPlayValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        PlayAction playAction = pageFactory.getPlayAction();
        EventValidator eventValidator = pageFactory.getEventValidator();
        VolumeValidator volumeValidator = pageFactory.getVolumeValidator();
        PauseValidator pause = pageFactory.getPauseValidator();
       FullScreenValidator fullScreenValidator = pageFactory.getFullScreenValidator();
            EndScreenValidator endScreenValidator = pageFactory.getEndScreenValidator();

        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();

            injectScript("http://10.11.66.55:8080/alice.js");

            playAction.startAction();

            play.validate("playing_1", 60);
            Thread.sleep(2000);

            pause.validate("videoPause_1",60);

            play.validate("playing_2", 60);

            seek.validate("seeked_1", 60);

            eventValidator.validate("played_1",60);


           /* TestUtilities.verifyEndScreen(webDriver);

            seleniumActions.clickOnElement("fullScreenBtn1");

            seleniumActions.waitForElement("fullscreenChangedtrue",50);

            TestUtilities.verifyEndScreen(webDriver);
            seleniumActions.clickOnElement("fullScreenBtn1");
            TestUtilities.verifyEndScreen(webDriver);*/

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Alice basic playback tests failed");
    }
    }

