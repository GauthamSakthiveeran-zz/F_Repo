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
public class ValidateAPIForPreMidPostrollPoddedAdTests extends PlaybackWebTest {

    private PlayValidator play;
    private PlayerAPIValidator api;

    public ValidateAPIForPreMidPostrollPoddedAdTests() throws OoyalaException {
        super();
    }

    @Test(groups = "api",dataProvider = "testUrls")
    public void testApiForPreMidPostrollPoddedAd(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && play.waitForPage();
            injectScript();
            result = result && api.validateAPIForPreMidPostPodded();

        }catch (Exception ex){
            ex.getStackTrace();
            result = false;
        }
        Assert.assertTrue(result);
    }
}