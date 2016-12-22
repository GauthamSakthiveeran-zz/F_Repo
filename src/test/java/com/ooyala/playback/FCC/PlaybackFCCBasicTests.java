package com.ooyala.playback.FCC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 21/12/16.
 */
public class PlaybackFCCBasicTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackFCCBasicTests.class);

    private PlayValidator play;
    private PauseValidator pause;
    private EventValidator eventValidator;
    private CCValidator cc;
    private FCCValidator fcc;
    private FullScreenValidator fullscreen;

    public PlaybackFCCBasicTests() throws OoyalaException {
        super();
    }

    @Test(groups = "FCC", dataProvider = "testUrls")
    public void testFCCClosedcaption(String testName, String url) throws OoyalaException {
        logger.info("Url is : " + url);
        boolean result = true;
        try {

            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1",30000);

            result = result && eventValidator.loadingSpinner();

            Thread.sleep(2000);

            result = result && pause.validate("paused_1",30000);

          //  result = result && cc.checkClosedCaptionButton();
            logger.info("......after cc:");

            Thread.sleep(1000);

            result = result && cc.closedCaptionMicroPanel();
            Thread.sleep(2000);

            result = result &&  fcc.checkArrows();

            Thread.sleep(2000);

            //add fullscreen validations here...
            //    result = result && fullscreen.validate("", 60000);

            // Verify Closed caption Panel Elements
            result = result && fcc.verifyCCPanelElements();

            // CC Languages
            result = result && fcc.verifyClosedCaptionLanguages();

            // CC Color Selection
            result = result && fcc.verifyCCColorSelectionPanel();


        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Playback FCC CC tests failed");
    }
}
