package com.ooyala.playback.api;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.OoyalaAPIValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 7/7/17.
 */
public class ValidateAPIForPrerollAdTests extends PlaybackWebTest {

    private PlayValidator play;
    private PlayAction playAction;
    private OoyalaAPIValidator api;
    private static final Logger logger = Logger.getLogger(ValidateAPIForPrerollAdTests.class);

    public ValidateAPIForPrerollAdTests() throws OoyalaException {
        super();
    }

    @Test(groups = "api",dataProvider = "testUrls")
    public void testApiForPrerollAd(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && play.waitForPage();
            injectScript();
            result = result && api.validateAPIForPreroll();

        }catch (Exception ex){
            logger.error(ex);
            extentTest.log(LogStatus.FAIL,ex.getMessage());
            result = false;
        }
        Assert.assertTrue(result);
    }
}
