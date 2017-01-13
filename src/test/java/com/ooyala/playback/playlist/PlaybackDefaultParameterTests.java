package com.ooyala.playback.playlist;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlaylistValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 13/01/17.
 */
public class PlaybackDefaultParameterTests  extends PlaybackWebTest {


    private PlaylistValidator playlist;

    private static Logger logger = Logger
            .getLogger(PlaybackDefaultParameterTests.class);

    public PlaybackDefaultParameterTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playlist", dataProvider = "testUrls")
    public void testDefaultParameterPlaylistTests(String testName, String url) throws OoyalaException {

        boolean result = true;
        try {

            driver.get(url);

            injectScript();

            result=result && playlist.validate("",20000);


        } catch (Exception e) {
            logger.error(e);
            result = false;
        }
        Assert.assertTrue(result, "Playback Default Parameter Playlist tests failed");
    }
}
