package com.ooyala.playback.playlist;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlaylistValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by snehal on 13/01/17.
 */
public class PlaybackPlaylistTests extends PlaybackWebTest {


    private PlaylistValidator playlist;
    private PlayValidator play;

    public PlaybackPlaylistTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playlist", dataProvider = "testUrls")
    public void testPlaylistTests(String testName, String url) throws OoyalaException {
        String[] parts= testName.split(":");
        String tcName = parts[1].trim();
        String tcValue = parts[2].trim();
        boolean result = true;
        try {

            driver.get(url);
            if (!tcValue.equalsIgnoreCase("true")){
                result = result && play.waitForPage();
            }

            injectScript();

            result=result && playlist.playlistValidator(tcName, tcValue);

        } catch (Exception e) {
            e.getMessage();
            result = false;
        }
        Assert.assertTrue(result, "Playback Playlist tests failed"+testName);
    }
}
