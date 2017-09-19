package com.ooyala.playback.playerParameter;


import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.VolumeValidator;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlaylistValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;


public class PlaylistAutoPlayInitialVolume extends PlaybackWebTest {

    private PlaylistValidator playlist;
    private PlayValidator play;
    private StreamValidator streamTypeValidator;
    private EventValidator eventValidator;
    private VolumeValidator volumeValidator;
    private static final Logger logger = Logger.getLogger(PlaylistAutoPlayInitialVolume.class);
    public PlaylistAutoPlayInitialVolume() throws OoyalaException {
        super();
    }

    @Test(groups = "autoplay", dataProvider = "testUrls")
    public void testPlaylistTests(String description, UrlObject url) throws OoyalaException {
       boolean result = true;
        try {
        	driver.get(url.getUrl());
        	String autoPlay = getAutoPlayFlag();
        String loop = getLoopFlag();
        String videoPluginName = getVideoPlugins()[0];
        if (autoPlay.equalsIgnoreCase("true")) {
            result = result && play.waitForPage();
            injectScript();
        } else {
            result = result && playlist.isPageLoaded();
            injectScript();
        }
       result = result && playlist.playlistValidator("Autoplay", autoPlay, videoPluginName);
       result = result && volumeValidator.validateInitialVolume(0.5);
       result = result && playlist.playlistValidator("Loop", loop, videoPluginName);

        } catch (Exception e) {
            extentTest.log(LogStatus.FAIL, e.getMessage());
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Autoplay Playlist tests failed" + description);
    }
}
