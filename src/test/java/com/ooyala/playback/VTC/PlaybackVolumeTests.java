package com.ooyala.playback.VTC;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.lang.Thread.sleep;

/**
 * Created by jitendra on 24/11/16.
 */
public class PlaybackVolumeTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackVolumeTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private VolumeValidator volumeValidator;
	private PlayAction playAction;
	private SeekValidator seekValidator;
	private IsAdPlayingValidator isAdPlayingValidator;

	PlaybackVolumeTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testVolumeVTC(String testName, String url)
			throws OoyalaException {

		logger.info("Url is : " + url);

        boolean result = true;
        try {
            driver.get(url);

            result=result && play.waitForPage();

			Thread.sleep(10000);

			injectScript();

			logger.info("video is playing");

            result=result && play.validate("playing_1", 60000);


			Boolean isAdplaying = isAdPlayingValidator.validate(
					"CheckAdPlaying", 60);
			if (isAdplaying) {
				volumeValidator.validate("VOLUME_MAX", 60000);
				logger.info("validated ad volume at full range");
				eventValidator.validate("adPodEnded_1", 20000);
				logger.info("Ad played");
			}

			sleep(4000);

            result=result && volumeValidator.validate("VOLUME_MAX", 60000);

			logger.info("validated video volume at full range");

            sleep(3000);

            result=result && seekValidator.validate("seeked_1",60);

			eventValidator.validate("played_1", 60000);

			logger.info("video played");

        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result, "Playback Volume tests failed");
    }
}
