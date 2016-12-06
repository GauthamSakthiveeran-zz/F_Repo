package com.ooyala.playback.playerfeatures;


import com.ooyala.playback.page.action.PauseAction;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackWatermarkTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackWatermarkTests.class);
	private PlayValidator play;
	private SeekValidator seek;
	private PlayAction playAction;
	private EventValidator eventValidator;
	private VolumeValidator volumeValidator;
	private PauseValidator pause;
	private WaterMarkValidator waterMarkValidator;
    private PauseAction pauseAction;

	public PlaybackWatermarkTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testWatermarks(String testName, String url)
			throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url);

            result = result && play.waitForPage();

			Thread.sleep(5000);

			injectScript();

            result = result && play.validate("playing_1", 60000);
			logger.info("video is playing");
			Thread.sleep(3000);

            result = result && pause.validate("paused_1",60000);

            result = result && waterMarkValidator.validate("WATERMARK_LOGO", 60000);
			logger.info("checked watermark logo");

			result = result && playAction.startAction();

            result = result && seek.validate("seeked_1", 60000);

			logger.info("video seeked");

            result = result && eventValidator.validate("videoPlayed_1", 60000);

			logger.info("video played");

		} catch (Exception e) {
			e.printStackTrace();
            result = false;
		}

		Assert.assertTrue(result, "Playback Watermark tests failed");
	}
}
