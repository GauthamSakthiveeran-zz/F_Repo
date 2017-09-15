package com.ooyala.playback.playlist;

import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.PlayerAPIAction;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlaylistValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by snehal on 13/01/17.
 */
public class PlaybackPlaylistTests extends PlaybackWebTest {

    private PlaylistValidator playlist;
    private PlayValidator play;
    private StreamValidator streamTypeValidator;
    private EventValidator eventValidator;
    private PlayerAPIAction playerAPI;
    private static final Logger logger = Logger.getLogger(PlaybackPlaylistTests.class);

    public PlaybackPlaylistTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playlist", dataProvider = "testUrls")
    public void testPlaylistTests(String description, UrlObject url) throws OoyalaException {
        //seperating the tab name from the test description
        String[] parts = description.split(":")[1].trim().split("-");
        //splitting the description using spaces
        String[] descParts = description.split(" ");
        //extracting the video plugin name from description
        String videoPluginName = descParts[descParts.length - 1];
        String tcName = parts[0].trim();
        //removing the video plugin name from the test name
        if (tcName.contains(videoPluginName))
            tcName = tcName.replaceAll(videoPluginName, "").trim();
        String tcValue = "";
        //removing the video plugin name from the test value
        if (parts.length > 1)
            tcValue = parts[1].replaceAll(videoPluginName, "").trim();

        boolean result = true;
        try {

            driver.get(url.getUrl());
            if (!(description.contains("true") || description.contains("Menustyle-tabs"))) {
                result = result && play.waitForPage();
                injectScript();
            } else {
                result = result && playlist.isPageLoaded();
                injectScript();
            }

            result = result && playlist.playlistValidator(tcName, tcValue, videoPluginName);

            if (url.getStreamType() != null && !url.getStreamType().isEmpty()) {
                if (description.contains("Autoplay-false"))
                    result = result && play.validate("playing_1", 20000);
                result = result && eventValidator.validate("videoPlayingurl", 40000);
                if (!url.getVideoPlugins().equalsIgnoreCase("adobetvsdk")) {
                    result = result
                            && streamTypeValidator.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);
                }
            }

            if (playerAPI.getErrorCode() != null) {
                extentTest.log(LogStatus.SKIP, "Skipping test as video is in error state");
                return;
            }

        } catch (Exception e) {
            extentTest.log(LogStatus.FAIL, e.getMessage());
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Playlist tests failed" + description);
    }
}
