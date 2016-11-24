package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.lang.Thread.sleep;

/**
 * Created by jitendra on 24/11/16.
 */
public class PlaybackVolumeTests extends PlaybackWebTest{

    private EventValidator eventValidator;
    private PlayValidator play;
    private VolumeValidator volumeValidator;
    private PlayAction playAction;

    PlaybackVolumeTests() throws OoyalaException {
        super();
    }


    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testVolumeVTC(String testName, String url)
            throws OoyalaException {

        boolean result = false;
        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();

            injectScript();

            playAction.startAction();

            Boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver)
                    .executeScript("return pp.isAdPlaying()"));
            if (isAdplaying) {
                volumeValidator.validate("VOLUME_MAX", 60);
                logger.info("validated ad volume at full range");
                eventValidator.validate("adPodEnded_1", 200);
                logger.info("Ad played");
            }

            play.validate("playing_1", 60);

            logger.info("video is playing");
            sleep(4000);

            volumeValidator.validate("VOLUME_MAX", 60);

            logger.info("validated video volume at full range");

            eventValidator.validate("played_1", 60);

            logger.info("video played");

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Playback Volume tests failed");
    }

}
