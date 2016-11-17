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
    public void testBasicPlaybackAlice(String testName, String url) throws OoyalaException {

       /* boolean result = false;
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

                injectScript("http://10.11.66.55:8080/alice.js");

                play.validate("playing_1", 60);

                pause.validate("paused_1", 60);
                play.validate("playing_2", 60);

                seek.seek("pp.getDuration()/2");

                controlBarValidator.validate("",60);
                //to-do add ooyala logo to the test page

                fullScreenValidator.validate("fullScreenBtn1",60);

                pause.validate("paused_2", 60);



                liveAction.startAction();

                //   assertEquals(seek.validate("seeked_1", 60), false, "We are able to seek live asset");

                eventValidator.validate("played_1", 60);


                // Verify Forward and Backward seek
                ((JavascriptExecutor) webDriver).executeScript("pp.seek(pp.getDuration()-pp.getDuration()/2);");
                sleep(3000);
                assertEquals(true, (webDriver.findElement(id("seeked_1")).isDisplayed()), "Not able to seek the video forward");
                Log.info("Seek the video forward");
                sleep(1000);
                ((JavascriptExecutor) webDriver).executeScript("pp.seek(5);");
                sleep(3000);
                assertEquals(true, (webDriver.findElement(id("seeked_2")).isDisplayed()), "Not able to seek the video backword");
                Log.info("Seek the video backword");
                test.log(PASS, "verified seek");

                seleniumActions.clickOnElement("fullScreenBtn");

                assertEquals(true, (webDriver.findElement(locator.getobjectLocator("normalScreen")).isDisplayed()), "Normal screen button is not shwoing after clicking on full screen It means video is not switched to fullscreen");

                test.log(PASS, "Verified Fullscreen");

                seleniumActions.waitForElement("played_1", 200);

                test.log(PASS, "verified HLS vod test");
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.assertTrue(result, "Alice basic playback tests failed");

        } else {

            throw new SkipException("Test PlaybackHLSLive Is Skipped");
        }*/
    }
}
