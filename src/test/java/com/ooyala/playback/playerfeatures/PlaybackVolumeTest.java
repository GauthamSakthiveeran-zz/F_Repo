package com.ooyala.playback.playerfeatures;

import static java.lang.Thread.sleep;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
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

	public PlaybackVolumeTest() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testVolume(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

            result = result && play.waitForPage();

			injectScript();

            result = result && playAction.startAction();

			Boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver)
					.executeScript("return pp.isAdPlaying()"));
			if (isAdplaying) {
				volumeValidator.validate("VOLUME_MAX", 60000);
				logger.info("validated ad volume at full range");
				eventValidator.validate("adPodEnded_1", 200);
				logger.info("Ad played");
			}

            Thread.sleep(2000);

            result = result && volumeValidator.validate("VOLUME_MAX", 60000);

			logger.info("validated video volume at full range");

            result = result && eventValidator.validate("playing_10 ", 60000);

            logger.info("video is playing");
            sleep(4000);

            result = result && seek.validate("seeked_1", 60000);

			logger.info("video seeked");

            result = result && eventValidator.validate("played_1", 60000);

			logger.info("video played");

		} catch (Exception e) {
			e.printStackTrace();
            result = false;
		}
		Assert.assertTrue(result, "Playback Volume tests failed");
	}

}