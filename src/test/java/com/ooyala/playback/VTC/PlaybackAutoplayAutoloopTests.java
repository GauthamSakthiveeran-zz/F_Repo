package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.qe.common.exception.OoyalaException;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 25/11/16.
 */
public class PlaybackAutoplayAutoloopTests extends PlaybackWebTest {

    private PlayValidator play;
    private EventValidator eventValidator;
    private SeekValidator seekValidator;
    private ReplayValidator replayValidator;
    private IsAdPlayingValidator isAdPlayingValidator;

    public PlaybackAutoplayAutoloopTests() throws OoyalaException {
        super();
    }
    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testAutoplayAutoloop(String testName, String url) throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url);

            injectScript();

            String autoplay = ((JavascriptExecutor) driver).executeScript("return pp.parameters.autoPlay").toString();
            Assert.assertEquals(autoplay, "true", "verified autoplay");

            result = result && eventValidator.validate("videoPlaying_1",20000);
            logger.info("Autoplayed the asset.");

            result = result && seekValidator.validate("seeked_1",60000);

            result = result && eventValidator.validate("replay_1",60000);

            boolean isAdplaying = isAdPlayingValidator.validate("CheckAdPlaying",60000);

            Assert.assertEquals(isAdplaying, true, "Verified that ad is played after auto replay");

            logger.info("Video Played");

        }catch(Exception e){
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Playback Autoplay Autoloop test failed");
    }
}