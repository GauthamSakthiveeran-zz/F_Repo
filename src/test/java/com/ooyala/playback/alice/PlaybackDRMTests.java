package com.ooyala.playback.alice;


import com.ooyala.facile.proxy.browsermob.BrowserMobProxyHelper;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import net.lightbody.bmp.core.har.HarEntry;
import org.openqa.selenium.Proxy;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.List;

public class PlaybackDRMTests  extends PlaybackWebTest {

    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;

    public PlaybackDRMTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playback", dataProvider = "testUrls")
    public void testPlaybackDRM(String testName, String url) throws OoyalaException {

        boolean result = true;
        if((testName.split(":")[1].toLowerCase()).contains("Fairplay".toLowerCase())&& !(getBrowser().equalsIgnoreCase("safari")) ){
            throw new SkipException("Fairplay DRM runs in Safari browser only - Test Skipped");
        }

            try {
                driver.get(url);

                //need to add logic for verifying description
                result = result && play.waitForPage();
                Thread.sleep(10000);

                injectScript();

                result = result && play.validate("playing_1", 60000);

                Thread.sleep(2000);

                result = result && pause.validate("paused_1", 60000);

                logger.info("Verified that video is getting pause");

                result = result && play.validate("playing_2", 60000);

                result = result && seek.validate("seeked_1", 60000);

                logger.info("Verified that video is seeked");

                result = result && eventValidator.validate("played_1", 60000);

                logger.info("Verified that video is played");

            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
            Assert.assertTrue(result, "DRM tests failed");
    }
}