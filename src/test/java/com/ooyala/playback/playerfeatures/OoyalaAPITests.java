package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OoyalaAPIValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 29/12/16.
 */
public class OoyalaAPITests extends PlaybackWebTest {

    private EventValidator eventValidator;
    private OoyalaAPIValidator ooyalaAPIValidator;


    public OoyalaAPITests() throws OoyalaException {
        super();
    }

    @Test(groups = "api", dataProvider = "testUrls")
    public void testOoyalaAPI(String testName, UrlObject url)
            throws OoyalaException {
        boolean result = true;

        try {
            driver.get(url.getUrl());
            result = result && eventValidator.loadingSpinner();
            injectScript();
            result = result && ooyalaAPIValidator.validate("", 30000);
            result = result && eventValidator.validate("played_1",60000);

        } catch (Exception e) {
        	extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Ooyala API test failed");

    }
}
