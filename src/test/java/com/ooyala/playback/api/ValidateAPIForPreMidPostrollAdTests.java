package com.ooyala.playback.api;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlayerAPIValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by jitendra on 7/7/17.
 */
public class ValidateAPIForPreMidPostrollAdTests extends PlaybackWebTest {

    private PlayValidator play;
    private PlayerAPIValidator api;

    public ValidateAPIForPreMidPostrollAdTests() throws OoyalaException {
        super();
    }

    @Test(groups = "api",dataProvider = "testUrls")
    public void testApiForPreMidPostrollAd(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && play.waitForPage();
            injectScript();
            result = result && api.validateAPIForPreMidPost();

        }catch (Exception ex){
            ex.getStackTrace();
            result = false;
        }
        Assert.assertTrue(result);
    }
}
