package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.MultiplePlayerValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 28/11/16.
 */
public class PlaybackMultipleV4PlayerTests extends PlaybackWebTest{

    private PlayValidator play;
    private MultiplePlayerValidator multiplePlayerValidator;
    private EventValidator eventValidator;

    public PlaybackMultipleV4PlayerTests() throws OoyalaException {
        super();
    }


    @Test(groups = "multiplePlayer", dataProvider = "testUrls")
    public void verifyMultipleV4Player(String testName , String url) throws Exception {

        String urlLink = "http://shared.ooyala.com/RCTTestAssets/tushar/Multiple_Player.html";

        boolean result = false;

        driver.get(urlLink);

        try {

            play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            multiplePlayerValidator.validate("player1_play", 20);

            multiplePlayerValidator.validate("player1_pause", 20);

            multiplePlayerValidator.validate("player2_play", 20);

            multiplePlayerValidator.validate("player2_pause", 20);

            multiplePlayerValidator.validate("seek1", 20);

            multiplePlayerValidator.validate("seek2", 20);

            Assert.assertTrue(eventValidator.validate("player1_playing_1", 20));

            Assert.assertTrue(eventValidator.validate("player1_pause_1", 20));

            Assert.assertTrue(eventValidator.validate("player2_playing_1", 20));

            Assert.assertTrue(eventValidator.validate("player2_pause_1", 20));

            Assert.assertTrue(eventValidator.validate("player1_seeked_1", 20));

            Assert.assertTrue(eventValidator.validate("player2_seeked_1", 20));

            result = true;

        } catch (Exception e){
            e.printStackTrace();
            result = false;
        }

        Assert.assertTrue(result,"MultipleV4Player failed");

    }
}
