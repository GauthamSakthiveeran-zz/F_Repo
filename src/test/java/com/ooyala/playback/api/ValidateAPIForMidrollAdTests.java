package com.ooyala.playback.api;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayerAPIValidator;
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
public class ValidateAPIForMidrollAdTests extends PlaybackWebTest {

    private PlayValidator play;
    private PlayAction playAction;
    private PlayerAPIValidator api;
    private static final Logger logger = Logger.getLogger(ValidateAPIForMidrollAdTests.class);

    public ValidateAPIForMidrollAdTests() throws OoyalaException {
        super();
    }

    @Test(groups = "api",dataProvider = "testUrls")
    public void testApiForMidrollAd(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && play.waitForPage();
            injectScript();
            result = result && api.validateAPIForMidroll();

        }catch (Exception ex){
            logger.error(ex);
            extentTest.log(LogStatus.FAIL,ex.getMessage());
            result = false;
        }
        Assert.assertTrue(result);
    }
}
