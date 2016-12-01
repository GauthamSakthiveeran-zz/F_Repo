package com.ooyala.playback.alice;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SocialScreenValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by jitendra on 23/11/16.
 */
public class PlaybackSocialMediaTests extends PlaybackWebTest{

	private static Logger logger = Logger.getLogger(PlaybackSocialMediaTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SocialScreenValidator social;

    public PlaybackSocialMediaTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testScialMediaSharing(String testName, String url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url);

            result = result && play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            result = result && play.validate("playing_1", 60000);

            Thread.sleep(2000);

            result = result && social.validate("SHARE_BTN",60000);

            logger.info("Verified Social Media Sharing functionality for Facebook,Twitter and ");

            result = result && pause.validate("paused_1", 60000);

            logger.info("Verified that video is getting pause");

        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Basic playback tests failed");
    }
}
