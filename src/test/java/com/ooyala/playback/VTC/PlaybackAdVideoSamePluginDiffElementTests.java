package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DifferentElementValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 28/11/16.
 */
public class PlaybackAdVideoSamePluginDiffElementTests  extends PlaybackWebTest {
    private PlayValidator play;
    private EventValidator eventValidator;
    private PlayAction playAction;
    private SeekValidator seekValidator;
    private DifferentElementValidator elementValidator;

    PlaybackAdVideoSamePluginDiffElementTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testSamePluginsDiffElementTests(String testName, String url)
            throws OoyalaException {

        boolean result = false;

        try{
            driver.get(url);
            if (!getPlatform().equalsIgnoreCase("android")) {
                driver.manage().window().maximize();
            }

            play.waitForPage();

            injectScript();
            playAction.startAction();

            eventValidator.validate("adPodEnded_1",60);
            logger.info("Ad played");

            elementValidator.validate("VIDEO_PATH",60);
            logger.info( "Two different elements created for ad and main video");

            eventValidator.validate("Playing_1",60);
            logger.info("Video starts playing");

            seekValidator.validate("seeked_1",60);

            eventValidator.validate("played_1",60);

            Thread.sleep(15000);
            result = true;

        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Playback Same plugins Different Element test failed");
    }

}
