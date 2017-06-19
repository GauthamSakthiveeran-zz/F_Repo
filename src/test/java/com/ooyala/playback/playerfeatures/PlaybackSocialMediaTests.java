package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SocialScreenValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 23/11/16.
 */
public class PlaybackSocialMediaTests extends PlaybackWebTest {

    private PlayValidator play;
    private SocialScreenValidator social;
    private EventValidator event;

    public PlaybackSocialMediaTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testScialMediaSharing(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && event.playVideoForSometime(2);

            result = result && social.softAssert(s_assert).validate("", 60000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, "Error in Social Media Test" + e.getMessage());
            result = false;
        }
        s_assert.assertAll();
        Assert.assertTrue(result, "Social Media tests failed");
    }
}