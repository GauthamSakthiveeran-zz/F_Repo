package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
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

    PlaybackDiscoverTests() throws OoyalaException {
        super();
    }


    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testDiscoveryVTC(String testName, String url)
            throws OoyalaException {

        boolean result = false;
        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            play.validate("playing_1", 60);

            logger.info("Verifed that video is getting playing");

            discoveryValidator.validate("reportDiscoveryClick_1", 60);
            logger.info("verified discovery");

            pageFactory.getSeekAction().setTime(25).fromLast().startAction();//seek(25, true);

            discoveryUpNext.validate("UPNEXT_CONTENT", 60);
            logger.info("Verified UpNext content");

            eventValidator.validate("played_1", 60);

            logger.info("Verified that video is played");

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Playback Volume tests failed");
    }

}
