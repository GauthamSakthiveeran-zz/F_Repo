package com.ooyala.playback.alice;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackFullScreenTests extends PlaybackWebTest {

    private PlayValidator play;
    private SeekValidator seek;
    private PlayAction playAction;
    private EventValidator eventValidator;
    private FullScreenValidator fullScreenValidator;


    public PlaybackFullScreenTests() throws OoyalaException {
        super();
    }

    @Test(groups = "Player", dataProvider = "testUrls")
    public void testPlaybackFullscreen(String testName, String url) throws OoyalaException {


        boolean result = false;

		try {
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			play.waitForPage();

            injectScript(jsURL());


			play.validate("playing_1", 60);

			logger.info("video is playing");

            fullScreenValidator.validate("",60);

            seek.validate("seeked_1", 60);

            logger.info("video seeked");

			eventValidator.validate("played_1", 60);

			logger.info("video played");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result, "Playback FullScreen tests failed");
	}
}