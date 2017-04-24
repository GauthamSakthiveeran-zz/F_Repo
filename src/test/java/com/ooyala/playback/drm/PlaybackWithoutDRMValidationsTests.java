package com.ooyala.playback.drm;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by suraj on 3/31/17.
 */
public class PlaybackWithoutDRMValidationsTests extends PlaybackWebTest {

    private EventValidator eventValidator;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private StreamValidator stream;

    public PlaybackWithoutDRMValidationsTests() throws OoyalaException{
        super();
    }

    @Test(groups = "drm", dataProvider = "testUrls")
    public void testPlayback(String testName, UrlObject url)
            throws OoyalaException {
        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && pause.validate("paused_1", 60000);

            if (url.getStreamType()!=null && !url.getStreamType().isEmpty()){
                result = result && stream.verifyStreamType(url.getStreamType());
            }

            result = result && play.validate("playing_2", 60000);

            result = result && seek.validate("seeked_1",20000);

            result = result && eventValidator.validate("played_1", 60000);

        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        Assert.assertTrue(result, "DRM tests failed : " + testName);
    }
}
