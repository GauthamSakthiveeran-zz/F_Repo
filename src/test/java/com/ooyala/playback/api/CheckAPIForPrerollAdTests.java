package com.ooyala.playback.api;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.OoyalaAPIValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 7/7/17.
 */
public class CheckAPIForPrerollAdTests extends PlaybackWebTest {

    private PlayValidator play;
    private PlayAction playAction;
    private OoyalaAPIValidator api;

    public CheckAPIForPrerollAdTests() throws OoyalaException {
        super();
    }

    @Test(groups = "api",dataProvider = "testUrls")
    public void testApiForPrerollAd(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && play.waitForPage();
            injectScript();
            result = result && api.valdidateAPIForPreroll();

        }catch (Exception ex){
            ex.getStackTrace();
            result = false;
        }

        Assert.assertTrue(result);
    }
}
