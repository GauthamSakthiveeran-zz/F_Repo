package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SocialScreenValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 23/11/16.
 */
public class PlaybackSocialMediaTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackSocialMediaTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SocialScreenValidator social;

    public PlaybackSocialMediaTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testScialMediaSharing(String testName, String url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url);

            result = result && play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            result = result && play.validate("playing_1", 60000);

            Thread.sleep(2000);

            result = result && social.validate("", 60000);

        } catch (Exception e) {
            logger.error("Error in Social Media Test"+e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Social Media tests failed");
    }
}