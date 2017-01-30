package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

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

			injectScript();

			logger.info("video is playing");

            result=result && playAction.startAction();

			result=result && eventValidator.loadingSpinner();

			Boolean isAdplaying = isAdPlayingValidator.validate(
					"CheckAdPlaying", 2000);
			if (isAdplaying) {
				logger.info("Checking volume for Ad");
				volumeValidator.validate("VOLUME_MAX", 20000);
				eventValidator.validate("adPodEnded_1", 20000);
				logger.info("Ad played");
				eventValidator.loadingSpinner();
			}

			result=result && eventValidator.validate("playing_1", 60000);

			Thread.sleep(1000);

            result=result && volumeValidator.validate("VOLUME_MAX", 60000);

			logger.info("validated video volume at full range");

            result=result && seekValidator.validate("seeked_1",20000);

			eventValidator.validate("played_1", 60000);

			logger.info("video played");

        } catch (Exception e) {
            logger.error("Error in volume test"+e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Volume tests failed");
    }
}
