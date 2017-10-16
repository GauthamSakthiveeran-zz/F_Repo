package com.ooyala.playback.dynamicfilters;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 10/6/17.
 */
public class PlaybackSingleDynamicFilterTests extends PlaybackWebTest {

    public PlaybackSingleDynamicFilterTests() throws OoyalaException {
    }

    private final static Logger logger = Logger.getLogger(PlaybackSingleDynamicFilterTests.class);
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private Bitratevalidator bitratevalidator;
    private PlayAction playAction;

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testSingleDynamicFilter(String testName, UrlObject url) throws OoyalaException {

        boolean result = true;
        String dynamicFilter = url.getDynamicFilter();

        try {
            driver.get(url.getUrl());

            result = result && play.clearCache();

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            result = result && eventValidator.loadingSpinner();

            result = result && eventValidator.validate("playing_1", 10000);

            result = result && pause.validate("paused_1", 10000);

            result = result && bitratevalidator.validateSingleDynamicFilter(dynamicFilter);

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Single Dynamic Filter Tests" + testName);
    }
}
