package com.ooyala.playback.syndicationrules;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SaasPortValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 14/02/17.
 */
public class PlaybackDeviceRegistrationTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackDeviceRegistrationTests.class);
    private SaasPortValidator sasport;
    private EventValidator eventValidator;
    private PlayValidator play;
    private SeekValidator seek;

    public PlaybackDeviceRegistrationTests() throws OoyalaException {
        super();
    }

    @Test(groups = "syndicationRules", dataProvider = "testUrls")
    public void testDeviceRegistration(String url) {
        boolean result = true;
        try {
            driver.get(url);

            result = result && sasport.getProperties();

            result = result && sasport.searchEntitlement();

            result = result && sasport.deleteEntitlement();

            result = result && sasport.createEntitlement("");

            result = result && sasport.deleteDevices();

            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("played_1", 60000);

            result = result && sasport.searchEntitlement();

            result = result && sasport.checkAccountDeviceRegistration();

        } catch (Exception e) {
            logger.error("Error while checking device registration" + e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Device Registration Test failed");

    }
}
