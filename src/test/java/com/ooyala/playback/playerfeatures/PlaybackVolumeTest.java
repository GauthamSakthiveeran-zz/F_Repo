package com.ooyala.playback.playerfeatures;


import static java.lang.Thread.sleep;

import com.ooyala.playback.page.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackVolumeTest extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackVolumeTest.class);
	private PlayValidator play;
	private SeekValidator seek;
	private PlayAction playAction;
	private EventValidator eventValidator;
	private VolumeValidator volumeValidator;
	private IsAdPlayingValidator isAdPlayingValidator;
	private SeekValidator seekValidator;

	public PlaybackVolumeTest() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testVolume(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

			play.waitForPage();

			Thread.sleep(10000);

			injectScript();

			loadingSpinner();

			playAction.startAction();

			loadingSpinner();

			Boolean isAdplaying = isAdPlayingValidator.validate("CheckAdPlaying",60);
			if (isAdplaying) {
				volumeValidator.validate("VOLUME_MAX", 60000);
				logger.info("validated ad volume at full range");
				eventValidator.validate("adPodEnded_1", 20000);
				logger.info("Ad played");
			}

			sleep(4000);

			logger.info("validated video volume at full range");

            result = result && eventValidator.validate("playing_1", 60000);

			volumeValidator.validate("VOLUME_MAX", 60000);

			logger.info("validated video volume at full range");

			seekValidator.validate("seeked_1",60);

			eventValidator.validate("played_1", 60000);

			logger.info("video played");
		} catch (Exception e) {
			e.printStackTrace();
            result = false;
		}
		Assert.assertTrue(result, "Playback Volume tests failed");
	}

}
