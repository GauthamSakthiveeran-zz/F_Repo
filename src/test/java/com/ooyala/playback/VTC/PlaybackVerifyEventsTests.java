package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 25/11/16.
 */
public class PlaybackVerifyEventsTests extends PlaybackWebTest{

    private PlayValidator play;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private PauseValidator pauseValidator;
    private PlayValidator playValidator;
    private SeekValidator seelValidator;

    public PlaybackVerifyEventsTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testVerifyEvents(String testName,String url){

        logger.info("Test url for "+testName+" is : "+url);

        boolean result = false;

        try {
            driver.get(url);

            play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            playAction.startAction();

            loadingSpinner();

            eventValidator.validate("willPlaySingleAd_1",60);

            eventValidator.validate("playing_1",60);

            eventValidator.validate("videoSetInitialTime_1",60);

            eventValidator.validate("videoPlay_1",60);

            eventValidator.validate("videoWillPlay_1",60);

            eventValidator.validate("videoPlaying_1",60);

            pauseValidator.validate("videoPause_1",60);

            eventValidator.validate("videoPaused_1",60);

            play.validate("playing_2",10);

            seelValidator.validate("seeked_1",60);

            eventValidator.validate("videoPlayed_1",60);
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertTrue(result, "Playback Video Controller Event test failed");

    }
}
