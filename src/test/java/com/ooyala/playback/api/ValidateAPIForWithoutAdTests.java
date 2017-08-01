package com.ooyala.playback.api;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OoyalaAPIValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 29/12/16.
 */
public class ValidateAPIForWithoutAdTests extends PlaybackWebTest {

    private EventValidator eventValidator;
    private OoyalaAPIValidator ooyalaAPIValidator;
    private PlayValidator playValidator;


    public ValidateAPIForWithoutAdTests() throws OoyalaException {
        super();
    }

    @Test(groups = "api", dataProvider = "testUrls")
    public void testOoyalaAPI(String testName, UrlObject url)
            throws OoyalaException {
        boolean result = true;
        boolean  playPauseSeek = testName.contains("Play Pause Seek");
        boolean volume = testName.contains("Volume");
        boolean destroy = testName.contains("Destroy");
        boolean fullscreen = testName.contains("FullScreen");
        boolean durationAtEnd = testName.contains("DurationAtEnd");
        boolean cc = testName.contains("CC");

        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();

            if (result && playPauseSeek){
                s_assert.assertTrue(ooyalaAPIValidator.validatePlayPauseSeekAPI(),"Play Pause Seek");
            }

            if (result && volume){
                s_assert.assertTrue(ooyalaAPIValidator.validateVolumeAPI(),"Volume");
            }

            if (result && fullscreen){
                s_assert.assertTrue(ooyalaAPIValidator.validateFullScreenAPI(),"FullScreen");
            }

            if (result && durationAtEnd){
                s_assert.assertTrue(ooyalaAPIValidator.validateDurationAtEndScreen(),"DurationAtEnd");
            }

            if (result && destroy){
                driver.navigate().refresh();
                result = result && playValidator.waitForPage();
                injectScript();
                s_assert.assertTrue(ooyalaAPIValidator.validateDestroyAPI(),"Destroy");
            }

            if (result && cc){
                s_assert.assertTrue(ooyalaAPIValidator.validateCloseCaptionAPI(),"CC");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        s_assert.assertTrue(result, "API");
        s_assert.assertAll();

    }
}
