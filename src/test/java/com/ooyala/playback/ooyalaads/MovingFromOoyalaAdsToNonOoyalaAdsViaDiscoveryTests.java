package com.ooyala.playback.ooyalaads;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 7/4/17.
 */
public class MovingFromOoyalaAdsToNonOoyalaAdsViaDiscoveryTests extends PlaybackWebTest {
    public MovingFromOoyalaAdsToNonOoyalaAdsViaDiscoveryTests() throws OoyalaException {
        super();
    }

    private static Logger logger = Logger.getLogger(MovingFromOoyalaAdsToNonOoyalaAdsViaDiscoveryTests.class);
    private PlayValidator playValidator;
    private PlayAction playAction;
    private DiscoveryValidator discoveryValidator;
    private EventValidator eventValidator;

    @Test(groups = {"amf", "ooyalaads", "discovery"}, dataProvider = "testUrls")
    public void verifyOoyalaAdsViaDiscovery(String testName, UrlObject url) throws Exception {
        boolean result = true;

        try {

            driver.get(url.getUrl());

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            result = result && eventValidator.validate("ooyalaAds",60000);

            result = result && eventValidator.validate("playing_1",10000);

            result = result && discoveryValidator.clickOnDiscoveryButton();

            result = result && discoveryValidator.selectAssetFormDiscoveryTray("Non Ooyala Ad asset with discovery.mp4");

            if (eventValidator.checkIsAdPlaying())
                result = false;

            result = result && eventValidator.validate("playing_2",10000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL,e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Test failed");
    }
}
