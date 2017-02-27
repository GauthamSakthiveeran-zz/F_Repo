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
 * Created by snehal on 26/12/16.
 */

public class PlaybackFCCDefaultSettingTests extends PlaybackWebTest {

    private static Logger logger = Logger
            .getLogger(PlaybackFCCDefaultSettingTests.class);

    private PlayValidator play;
    private PauseValidator pause;
    private EventValidator eventValidator;
    private FCCValidator fcc;
    private PlayAction playAction;
    private SeekValidator seek;
    private FullScreenValidator fullScreenValidator;

    public PlaybackFCCDefaultSettingTests() throws OoyalaException {
        super();
    }

    @Test(groups = "FCC", dataProvider = "testUrls")
    public void testFCCDefaultSetting(String testName, String url) throws OoyalaException {

        boolean result = true;

        try{
            driver.get(url);

            result = result && play.clearCache();
            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1",30000);

            result = result && eventValidator.loadingSpinner();

            result = result && pause.validate("paused_1",30000);

            if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("internet explorer")
                    || getBrowser().equalsIgnoreCase("MicrosoftEdge")
                    || (getBrowser().equalsIgnoreCase("firefox") && getPlatform().equalsIgnoreCase("mac")))){
                result = result && fullScreenValidator.getFullScreen();
            }

            result = result && fcc.closedCaptionMicroPanel();

            result = result && fcc.beforeRefreshCCSetting();

            driver.navigate().refresh();

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1",30000);

            result = result && eventValidator.loadingSpinner();

            result = result && pause.validate("paused_1",30000);

            fcc.switchToControlBar();

            if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("internet explorer")
                    || getBrowser().equalsIgnoreCase("MicrosoftEdge")
                    || (getBrowser().equalsIgnoreCase("firefox") && getPlatform().equalsIgnoreCase("mac")))){
                result = result && fullScreenValidator.getFullScreen();
            }

            result = result && fcc.closedCaptionMicroPanel();

            result = result && fcc.afterRefreshCCSettings();

            result = result && fcc.closeCCPanel();

            if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("internet explorer")
                    || getBrowser().equalsIgnoreCase("MicrosoftEdge")
                    || (getBrowser().equalsIgnoreCase("firefox") && getPlatform().equalsIgnoreCase("mac")))){
                result = result && fullScreenValidator.getNormalScreen();
            }

            result = result && fcc.closeCCPanel();

            result = result && playAction.startAction();

            result = result && seek.validate("seeked_1",40000);

            result = result && eventValidator.validate("played_1", 60000);

        }catch(Exception e){
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback FCC CC Default Setting tests failed");
    }
}
