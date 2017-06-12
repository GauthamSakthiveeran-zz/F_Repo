package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 5/4/17.
 */
public class PlaybackSetEmbedCodeTests extends PlaybackWebTest {

    PlaybackSetEmbedCodeTests() throws OoyalaException{
        super();
    }

    private EventValidator event;
    private PlayValidator playValidator;
    private SeekValidator seekValidator;
    private PoddedAdValidator poddedAdValidator;
    private SetEmbedCodeValidator setEmbedCodeValidator;

    @Test(groups = { "setEmbedCode"}, dataProvider = "testUrls")
    public void setEmbecCode(String testName, UrlObject url) throws OoyalaException {
        boolean result = true;
        try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playValidator.validate("playing_1", 90000);
            result = result && seekValidator.validate("seeked_1", 90000);
            result = result && event.validate("played_1", 90000);
            result = result && setEmbedCodeValidator.validate("", 10000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Tests failed");
    }
}
