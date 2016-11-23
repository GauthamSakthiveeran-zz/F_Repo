package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackBitrateTests extends PlaybackWebTest {

    private PlayValidator play ;
    private PauseValidator pause;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private Bitratevalidator bitratevalidator;

	public PlaybackBitrateTests() throws OoyalaException {
        super();
    }

    @Test(groups = "ABR", dataProvider = "testUrls")
    public void testBitrate(String testName, String url) throws OoyalaException {

        boolean result = false;

        try {
            driver.get(url);
            if (! driver.getCapabilities().getPlatform().toString().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();

            injectScript(jsURL());

            play.validate("playing_1", 60);
            logger.info("Verifed that video is getting playing");
            sleep(4000);

            pause.validate("paused_1", 60);
            logger.info("Verified that video is getting pause");

            bitratevalidator.validate("",60);

            sleep(1000);

            seek.validate("seeked_1", 60);
            logger.info("Verified that video is seeked");

            eventValidator.validate("videoPlayed_1", 60);
            logger.info("Verified that video is played");

            result = true;

        } catch (Exception e) {
            e.printStackTrace();

        }
        Assert.assertTrue(result, "Playback bitrate/Quality tests failed");

    }

}
