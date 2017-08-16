package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 7/21/17.
 */
public class PlaybackSetEmbedCodeTests extends PlaybackWebTest {
    public PlaybackSetEmbedCodeTests() throws OoyalaException {
        super();
    }

    private EventValidator event;
    private PlayValidator playValidator;
    private SeekValidator seekValidator;
    private SetEmbedCodeValidator setEmbedCodeValidator;
    private SeekAction seekAction;

    private static Logger logger = Logger.getLogger(PlaybackSetEmbedCodeTests.class);

    @Test(dataProvider = "testUrls")
    public void verifySetEmbedCode(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playValidator.validate("playing_1", 60000);
            result = result && seekValidator.validate("seeked_1", 160000);
            result = result && event.validate("played_1", 160000);
            result = result &&
                    setEmbedCodeValidator.validate("setEmbedmbedCode", 6000);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Verified");
    }
}
