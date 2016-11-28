package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 25/11/16.
 */
public class PlaybackNewStreamTests extends PlaybackWebTest {

    private PlayValidator play;
    private PauseValidator pause;
    private DiscoveryValidator discoveryValidator;
    private EventValidator eventValidator;
    private PlayAction playAction;

    public PlaybackNewStreamTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testLoadingNewStream(String testName, String url) throws OoyalaException {
        boolean result = false;
        try {
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();

            injectScript();

            playAction.startAction();

            eventValidator.validate("playing_1",60);
            logger.info("Video starts playing");

            pause.validate("paused_1",60);

            ((JavascriptExecutor) driver).executeScript("pp.setEmbedCode('Vmd2VmeDq6-92C-kPkkZGoOkTCeSZq4e')");

            eventValidator.validate("setEmbedCode_1",10);
            logger.info("Loaded New Asset");

            ((JavascriptExecutor) driver).executeScript("pp.play()");

            eventValidator.validate("playing_2",60);
            logger.info("Played new asset");

            pause.validate("paused_1", 60);
            logger.info("verified new asset is paused");

            discoveryValidator.validate("reportDiscoveryClick_1", 60);

            result = true;

        }catch(Exception e){
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Playback new stream tests failed");
    }
}



