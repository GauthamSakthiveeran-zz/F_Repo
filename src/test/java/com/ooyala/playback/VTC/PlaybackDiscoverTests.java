package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.lang.Thread.sleep;

/**
 * Created by jitendra on 24/11/16.
 */
public class PlaybackDiscoverTests extends PlaybackWebTest {

    private EventValidator eventValidator;
    private PlayValidator play;
    private UpNextValidator discoveryUpNext;
    private DiscoveryValidator discoveryValidator;
    private PlayAction playAction;
    private SeekAction seekAction;
    private SeekValidator seek;

    PlaybackDiscoverTests() throws OoyalaException {
        super();
    }


    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testDiscoveryVTC(String testName, String url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            result = result && play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            play.validate("playing_1", 60);

            logger.info("Verifed that video is getting playing");

            Assert.assertTrue(discoveryValidator.validate("reportDiscoveryClick_1", 60),"Discovery is not enabled");

            logger.info("verified discovery");

            result = result && play.waitForPage();

            playAction.startAction();

            loadingSpinner();

            Assert.assertTrue(eventValidator.validate("playing_2",20));

            logger.info("Verified that 2nd video is playing");

            Thread.sleep(5000);

            seek.validate("seeked_1",20);

            Thread.sleep(5000);

            Assert.assertTrue(discoveryUpNext.validate("UPNEXT_CONTENT", 60),"UpNext is not present");

            logger.info("Verified UpNext content");

            result = result && eventValidator.validate("played_1", 60);

            logger.info("Verified that video is played");

            result = true;

        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Playback Discovery tests failed");
    }

}
