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

        boolean result = true;

        driver.get(urlLink);

        try {

            result = result && play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            result = result &&  multiplePlayerValidator.validate("player1_play", 5000);

            result = result && multiplePlayerValidator.validate("player1_pause", 20000);

            result = result && multiplePlayerValidator.validate("player2_play", 20000);

            result = result && multiplePlayerValidator.validate("player2_pause", 20000);

            result = result &&  multiplePlayerValidator.validate("seek1", 20000);

            result = result && multiplePlayerValidator.validate("seek2", 20000);

            result = result && eventValidator.validate("player1_playing_1", 20000);

            result = result && eventValidator.validate("player1_pause_1", 20000);

            result = result && eventValidator.validate("player2_playing_1", 20000);

            result = result && eventValidator.validate("player2_pause_1", 20000);

            result = result && eventValidator.validate("player1_seeked_1", 20000);

            result = result && eventValidator.validate("player2_seeked_1", 20000);

        } catch (Exception e){
            e.printStackTrace();
            result = false;
        }

        Assert.assertTrue(result,"MultipleV4Player failed");

    }
}
