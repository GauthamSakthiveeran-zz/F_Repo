package com.ooyala.playback.adhoc;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ShareTabValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

/**
 * Created by suraj on 8/8/17.
 */
public class PageLevelOverrideForShareScreenTests extends PlaybackWebTest {
    public PageLevelOverrideForShareScreenTests() throws OoyalaException {
        super();
    }
    private static Logger logger = Logger.getLogger(PageLevelOverrideForShareScreenTests.class);
    private PlayValidator playValidator;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private ShareTabValidator share;
    private EncodingValidator encodingValidator;


    @Test(dataProvider = "testUrls",groups = "adhoc")
    public void pageLevelOverrideForShareScreenTest(String testName, UrlObject url){
        boolean result = true;
        String paramShareTab = "{\"shareScreen\":{\"shareContent\":[\"social\"],\"socialContent\":[\"twitter\",\"facebook\",\"google+\",\"email\"]}}";
        String paramEmbedTab = "{\"shareScreen\":{\"shareContent\": [\"embed\"],\"embed\": {\"source\": \"<iframe width='640' height='480' frameborder='0' allowfullscreen src='//player.ooyala.com/static/v4/candidate/latest/skin-plugin/iframe.html?ec=<ASSET_ID>&pbid=<PLAYER_ID>&pcode=<PUBLISHER_ID>'></iframe>\"}}}";
        try {
            driver.get(url.getUrl());
            result = result && playValidator.clearCache();
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && eventValidator.validate("playing_1",10000);
            driver.executeScript("pp.pause()");
            result = result && share.clickOnShareButton();
            result = result && share.validateBothShareTabAndEmbedTab();
            encodingValidator.getNewUrl(paramShareTab,browser);
            injectScript();
            result = result && playValidator.clearCache();
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && eventValidator.validate("playing_1",10000);
            driver.executeScript("pp.pause()");
            result = result && share.clickOnShareButton();
            if(result) {
                s_assert.assertTrue(share.validateOnlySocialShareTab(), "Social");
            }
            encodingValidator.getNewUrl(paramEmbedTab,browser);
            injectScript();
            result = result && playValidator.clearCache();
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && eventValidator.validate("playing_1",10000);
            driver.executeScript("pp.pause()");
            result = result && share.clickOnShareButton();
            if (result) {
                s_assert.assertTrue(share.validateOnlyEmbedOption(), "Embed");
            }
        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        s_assert.assertTrue(result, "PageLevel Override For Share Screen Tests :"+testName);
        s_assert.assertAll();
    }
}
