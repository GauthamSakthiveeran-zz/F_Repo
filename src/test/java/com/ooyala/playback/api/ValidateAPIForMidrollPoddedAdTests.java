package com.ooyala.playback.api;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayerAPIValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 7/7/17.
 */
public class ValidateAPIForMidrollPoddedAdTests extends PlaybackWebTest {

    private PlayValidator play;
    private PlayAction playAction;
    private PlayerAPIValidator api;

    public ValidateAPIForMidrollPoddedAdTests() throws OoyalaException {
        super();
    }

    @Test(groups = "api",dataProvider = "testUrls")
    public void testApiForMidrollPoddedAd(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && play.waitForPage();
            injectScript();
            result = result && api.validateAPIForMidrollPodded();

        }catch (Exception ex){
            ex.getStackTrace();
            result = false;
        }
        Assert.assertTrue(result);
    }
}
