package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 23/11/16.
 */
public class PlaybackSocialMediaTests extends PlaybackWebTest{

    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SocailScreenValidator social;

    public PlaybackSocialMediaTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testScialMediaSharing(String testName, String url) throws OoyalaException {

        boolean result = false;

        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            play.validate("playing_1", 60);

            logger.info("Verifed that video is getting playing");

            Thread.sleep(2000);

            social.validate("SHARE_BTN",60);

            logger.info("Verified Social Media Sharing functionality for Facebook,Twitter and ");

            pause.validate("paused_1", 60);

            logger.info("Verified that video is getting pause");

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Basic playback tests failed");
    }
}
