package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SocialScreenValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 23/11/16.
 */
public class PlaybackSocialMediaTests extends PlaybackWebTest {

    private PlayValidator play;
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

            result = result && social.softAssert(s_assert).validate("", 60000);

        } catch (Exception e) {
            extentTest.log(LogStatus.FAIL, "Error in Social Media Test"+e.getMessage());
            result = false;
        }
        s_assert.assertAll();
        Assert.assertTrue(result, "Social Media tests failed");
    }
}