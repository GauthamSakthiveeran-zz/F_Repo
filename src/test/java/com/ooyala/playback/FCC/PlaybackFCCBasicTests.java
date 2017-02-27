package com.ooyala.playback.FCC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

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
    private FCCValidator fcc;
    private FullScreenValidator fullscreen;
    private PlayAction playAction;
    private SeekValidator seek;
    private IsAdPlayingValidator isAdPlaying;

    public PlaybackFCCBasicTests() throws OoyalaException {
        super();
    }

    @Test(groups = "FCC", dataProvider = "testUrls")
    public void testFCCClosedcaption(String testName, String url) throws OoyalaException {

        boolean result = true;
        try {

            driver.get(url);

            result = result && play.clearCache();

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            boolean isAdplaying= isAdPlaying.validate("",50000);

            if (isAdplaying) {
                result = result && eventValidator.validate("singleAdPlayed_1", 30000);
                logger.info("Ad is played");
            }

            result = result && eventValidator.validate("playing_1",60000);

            result = result && eventValidator.loadingSpinner();

            result = result && pause.validate("paused_1",30000);

            result = result && fcc.discoveryCheck();

            result = result && fcc.validate("",30000);

            if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("internet explorer")
                    || getBrowser().equalsIgnoreCase("MicrosoftEdge")
                    || (getBrowser().equalsIgnoreCase("firefox") && getPlatform().equalsIgnoreCase("mac")))){
                result = result && fullscreen.getFullScreen();
                Thread.sleep(2000);
                result = result && fcc.validate("",30000);
                result = result && fullscreen.getNormalScreen();
            }

            result = result && play.validate("playing_2",30000);

            result = result && seek.validate("seeked_1",40000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            logger.error(e);
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback FCC CC tests failed :"+testName);
    }
}
