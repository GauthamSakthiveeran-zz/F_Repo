package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;

import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackAspectRatioTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackAspectRatioTests.class);

	private EventValidator eventValidator;
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private PlayAction playAction;
	private AspectRatioValidator aspectRatioValidator;

	public PlaybackAspectRatioTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testAspectRation(String testName, String url)
			throws OoyalaException {
		boolean result = true;

		try {
			driver.get(url);

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			sleep(2000);

    		result = result && pause.validate("paused_1", 60000);

			result = result && playAction.startAction();

			result = result && seek.validate("seeked_1", 60000);

			result = result && aspectRatioValidator.validate("assetDimension_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);


		} catch (Exception e) {
			logger.error(e);
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Aspect ratio tests failed");

	}

}
