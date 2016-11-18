package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackReplayVideoTests extends PlaybackWebTest {

    @DataProvider(name = "testUrls")
    public Object[][] getTestData() {

        return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
                nodeList);
    }

    public PlaybackReplayVideoTests() throws OoyalaException {
        super();
    }

    @Test(groups = "alice", dataProvider = "testUrls")
    public void testBasicPlaybackAlice(String testName, String url) throws OoyalaException {

        boolean result = false;
        PlayValidator play = pageFactory.getPlayValidator();
        SeekValidator seek = pageFactory.getSeekValidator();
        EventValidator eventValidator = pageFactory.getEventValidator();
        ReplayValidator replayValidator = pageFactory.getReplayValidator();

        try {
            driver.get(url);

            play.waitForPage();

            injectScript("http://10.11.66.55:8080/alice_full.js");

            play.validate("playing_1", 60);

            Thread.sleep(2000);

            seek.validate("seeked_1", 60);

            eventValidator.validate("played_1",200);

            replayValidator.validate("replay_1",60);

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result, "Alice basic playback tests failed");
    }
}
