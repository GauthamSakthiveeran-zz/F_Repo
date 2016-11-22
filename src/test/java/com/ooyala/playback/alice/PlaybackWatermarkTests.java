package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackWatermarkTests extends PlaybackWebTest {

    private PlayValidator play;
    private SeekValidator seek;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private VolumeValidator volumeValidator;
    private PauseValidator pause ;
    private WaterMarkValidator waterMarkValidator;

    public PlaybackWatermarkTests() throws OoyalaException {
        super();
    }

    @Test(groups = "PlayerSkin", dataProvider = "testUrls")
    public void testWatermarks(String testName, String url) throws OoyalaException {

        boolean result = false;
            try {
                driver.get(url);
                if (!getPlatform().equalsIgnoreCase("android")) {
                    driver.manage().window().maximize();
                }

                play.waitForPage();

                injectScript(jsURL());

                playAction.startAction();

                Boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver).executeScript("return pp.isAdPlaying()"));
                if (isAdplaying) {
                    volumeValidator.validate("VOLUME_MAX", 60);
                    eventValidator.validate("adPodEnded_1", 200);
                }

                play.validate("playing_1", 60);
                logger.info("video is playing");
                Thread.sleep(3000);

                pause.validate("paused_1",60);
                logger.info("video paused");

                waterMarkValidator.validate("WATERMARK_LOGO",60);
                logger.info("checked watermark logo");

                playAction.startAction();

                seek.validate("seeked_1", 60);

                logger.info("video seeked");

                eventValidator.validate("videoPlayed_1",60);

                logger.info("video played");

                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.assertTrue(result, "Playback Watermark tests failed");
        }
    }
