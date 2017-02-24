package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.WaterMarkValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackWatermarkTests extends PlaybackWebTest {

	private PlayValidator play;
	private SeekValidator seek;
	private PlayAction playAction;
	private EventValidator eventValidator;
	private PauseValidator pause;
	private WaterMarkValidator waterMarkValidator;

	public PlaybackWatermarkTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testWatermarks(String testName, String url) throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url);

			result = result && play.waitForPage();

			Thread.sleep(5000);

			injectScript();

			result = result && play.validate("playing_1", 60000);

			Thread.sleep(3000);

			result = result && pause.validate("paused_1", 60000);

			result = result && waterMarkValidator.validate("", 60000);

			Thread.sleep(10000);

			result = result && playAction.startAction();

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("videoPlayed_1", 60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Watermark tests failed");
	}
}
