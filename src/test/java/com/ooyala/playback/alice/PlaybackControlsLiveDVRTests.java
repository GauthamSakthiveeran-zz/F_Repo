package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackControlsLiveDVRTests extends PlaybackWebTest {

    private PlayValidator play;
    private PauseValidator pause;
    private LiveAction live;


    public PlaybackControlsLiveDVRTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Player", dataProvider = "testUrls")
    public void testControlLiveDVR(String testName, String url) throws OoyalaException {


        boolean result = false;
        if (getBrowser().equalsIgnoreCase("safari")) {
            try {
                driver.get(url);
                if (!getPlatform().equalsIgnoreCase("android")) {
                    driver.manage().window().maximize();
                }

                play.waitForPage();

                injectScript();

                play.validate("playing_1", 60);

                logger.info("Verified video playing");

                pause.validate("paused_1", 60);

                logger.info("verified video paused");

                play.validate("playing_2", 60);

                logger.info("verified video playing again");

                live.startAction();

                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.assertTrue(result, "PlabackControls Live DVR tests failed");
        } else {
            throw new SkipException("Test PlaybackLiveDVR Is Skipped");
        }
    }
}
