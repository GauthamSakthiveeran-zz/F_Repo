package com.ooyala.playback.ooyalaads;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.report.ExtentManager;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 7/4/17.
 */
public class MovingFromNonOoyalaAdsToOoyalaAdsViaDiscoveryTests extends PlaybackWebTest {
    public MovingFromNonOoyalaAdsToOoyalaAdsViaDiscoveryTests() throws OoyalaException {
        super();
    }

    private static Logger logger = Logger.getLogger(MovingFromNonOoyalaAdsToOoyalaAdsViaDiscoveryTests.class);
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

            //Ooyala ad should not play
            if (eventValidator.checkIsAdPlaying())
                result = false;

            result = result && eventValidator.validate("playing_1",10000);

            result = result && discoveryValidator.clickOnDiscoveryButton();

            //Give the name of the asset which you want to select and play
            result = result && discoveryValidator.selectAssetFormDiscoveryTray("Ooyala Ads asset with Disocvery.mp4");

            //Ooyala Ad should play
            result = result && eventValidator.validate("singleAdPlayed_1",90000);
            result = result && eventValidator.validate("ooyalaAds_1",60000);

            result = result && eventValidator.validate("playing_2",10000);

            result = result && discoveryValidator.clickOnDiscoveryButton();

            //Give the name of the asset which you want to select and play
            result = result && discoveryValidator.selectAssetFormDiscoveryTray("Non Ooyala Ad asset with discovery.mp4");

            //Ooyala ad should not play
            if (eventValidator.checkIsAdPlaying())
                result = false;

            result = result && eventValidator.validate("playing_3",10000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL,e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Test failed");
    }
}
