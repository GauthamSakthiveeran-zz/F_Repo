package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 25/11/16.
 */
public class PlaybackVideoControllerEventTests extends PlaybackWebTest {

    private EventValidator eventValidator;
    private PlayValidator play;
    private VolumeValidator volumeValidator;
    private PlayAction playAction;
    private SeekAction seekAction;

    public PlaybackVideoControllerEventTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testVideoControllerEvents(String testName,String url){

        boolean result = false;

        try {
            driver.get(url);

            play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            ((JavascriptExecutor) driver).executeScript("pp.setEmbedCode('htcmtjczpHnIEJLJUrZ8YUs0CW0pyi2R')");

            Assert.assertEquals(eventValidator.validateElementPresence("CreateVideo_1"),true,"Create Video event is not triggered after setting Embed new code");

            Assert.assertEquals(eventValidator.validateElementPresence("videoCreated_1"),true,"Video is not Created for new embed code");

            Assert.assertEquals(eventValidator.validateElementPresence("disposeVideo_1"),true,"Video Dispose event is not triggered");

            Assert.assertEquals(eventValidator.validateElementPresence("videoElementDisposed_1"),true,"Not getting Video element dispose");

            play.validate("playing_1",60);

            Assert.assertEquals(eventValidator.validateElementPresence("focusVideo_1"),true,"Focus video event is not triggering");

            Assert.assertEquals(eventValidator.validateElementPresence("videoInFocus_1"),true,"Not able to focus on new embedded video");

            seekAction.startAction();

            eventValidator.validate("videoLostFocus_1",60);

            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertTrue(result, "Playback Video Controller Event test failed");

    }
}
