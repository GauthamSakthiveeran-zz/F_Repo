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
    public void testBasicPlaybackAlice(String testName, String url) throws OoyalaException {

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
            BrowserMobProxyHelper.startBrowserMobProxyServer();

            BrowserMobProxyHelper.startHar("DRM.har");
            Thread.sleep(2000);
            pause.validate("paused_1", 60);

            List<HarEntry> entryList = BrowserMobProxyHelper.getHarEntries();
            for(HarEntry h:entryList) {
                BrowserMobProxyHelper.getUrlFromHarEntry(h);
            }
            BrowserMobProxyHelper.endHar();
            play.validate("playing_2", 60);
            seek.validate("seeked_1", 60);
            logger.info("Verified that video is seeked");
            eventValidator.validate("played_1",60);
            logger.info("Verified that video is played");
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "DRM tests failed");
    }
}