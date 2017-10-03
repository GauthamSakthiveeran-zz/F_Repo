package com.ooyala.playback.adhoc;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 8/17/17.
 */
public class SeekTillEndTests extends PlaybackWebTest {
    public SeekTillEndTests() throws OoyalaException {
        super();
    }
    private static Logger logger = Logger.getLogger(SeekTillEndTests.class);
    private PlayValidator playValidator;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private PlayerAPIAction playerAPI;

    @Test(dataProvider = "testUrls",groups = "adhoc")
    public void seekTillEnd(String testName, UrlObject url){
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.clearCache();
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && eventValidator.validate("playing_1",10000);
            playerAPI.seek(playerAPI.getDurationFixed());
            result = result && eventValidator.validate("seeked_1",20000);
        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Seek till end :"+testName);
    }
}
