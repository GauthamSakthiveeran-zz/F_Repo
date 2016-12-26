package com.ooyala.playback.FCC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
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
    private PlayAction playAction;
    private SeekValidator seek;

    public PlaybackFCCBasicTests() throws OoyalaException {
        super();
    }

    @Test(groups = "FCC", dataProvider = "testUrls")
    public void testFCCClosedcaption(String testName, String url) throws OoyalaException {

        boolean result = true;
        try {

            driver.get(url);

            result = result && fcc.clearCache();

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1",30000);

            result = result && eventValidator.loadingSpinner();

            Thread.sleep(2000);

            result = result && pause.validate("paused_1",30000);

            //  result = result && cc.checkClosedCaptionButton();
            result = result &&fcc.validate("",30000);

            Thread.sleep(2000);

            result = result && fcc.verifyFccInFullscreen();

            Thread.sleep(2000);

            result = result && playAction.startAction();

            result = result && seek.validate("seeked_1",30000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Playback FCC CC tests failed");
    }
}
