package com.ooyala.playback.playlist;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 13/01/17.
 */
public class PlaybackDefaultParameterTests  extends PlaybackWebTest {


    private PlaylistValidator playlist;
    private PlayValidator play;
    private PauseValidator pause;
    private SeekValidator seek;
    private EventValidator eventValidator;

    private static Logger logger = Logger
            .getLogger(PlaybackDefaultParameterTests.class);

    public PlaybackDefaultParameterTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playlist", dataProvider = "testUrls")
    public void testDefaultParameterPlaylistTests(String testName, String url) throws OoyalaException {
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

            //result=result && play.validate("playing_1",30000);

            result=result && playlist.playlistValidator(tcName, tcValue);

            //result = result && playlist.validate("dgrdf",20000);

           /* result = result && seek.validate("seeked_1",40000);

            result = result && eventValidator.validate("played_1", 60000);*/

        } catch (Exception e) {
            e.getMessage();
            result = false;
        }
        Assert.assertTrue(result, "Playback Default Parameter Playlist tests failed"+testName);
    }
}
