package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackAutoplayTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackAutoplayTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private SeekValidator seek;
    private AutoplayAction autoplayAction;
    private SeekAction seekAction;

    public PlaybackAutoplayTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testAutoPlay(String testName, String url)
            throws OoyalaException {
        boolean result = true;

        if (getPlatform().equalsIgnoreCase("Android")) {
            throw new SkipException("Test PlaybackAutoplayTests Is Skipped");
        } else {
            try {
                driver.get(url);

                injectScript();

                result = result && eventValidator.loadingSpinner();

                autoplayAction.startAction();

                try {
                    eventValidator.validate("singleAdPlayed_1", 5000);
                } catch (Exception e) {
                    logger.info("No Preroll ad present in this autoplay video");
                }
                result = result && eventValidator.validate("playing_1", 60000);

                sleep(500);

                result = result && seekAction.seek(10,true);

                result = result && eventValidator.validate("seeked_1",20000);

                result = result && eventValidator.validate("played_1", 60000);

            } catch (Exception e) {
                logger.error("Exception while checking autplay  "+e.getMessage());
                extentTest.log(LogStatus.FAIL, e.getMessage());
                result = false;
            }
            Assert.assertTrue(result, "Playback Autoplay tests failed");
        }
    }
}
