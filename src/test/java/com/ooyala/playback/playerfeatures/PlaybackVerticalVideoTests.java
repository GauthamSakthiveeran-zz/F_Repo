package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;
import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackVerticalVideoTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackVerticalVideoTests.class);
	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private PlayAction playAction;
	private EventValidator eventValidator;
	private AspectRatioValidator aspectRatioValidator;

	public PlaybackVerticalVideoTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testVerticalVideo(String testName, String url)
			throws OoyalaException {

		boolean result = true;
		try {
			driver.get(url);

			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.loadingSpinner();

			if(!result){
				logger.info("skipping test");
				throw new SkipException("Failed to load TestPage");
			}

			result = result && eventValidator.validate("playing_1", 60000);

			sleep(2000);

			result = result && aspectRatioValidator.setVerticalVideo().validate("assetDimension_1", 60000);

			result = result && pause.validate("paused_1", 60000);

			result = result && playAction.startAction();

			result = result && pause.validate("paused_1", 60000);

			logger.info("video paused");

			result = result && playAction.startAction();

			result = result && seek.validate("seeked_1", 60000);

			logger.info("video seeked");

			result = result
					&& aspectRatioValidator.setVerticalVideo().validate(
							"assetDimension_1", 60000);

			logger.info("validated vertical video dimention");

			result = result && eventValidator.validate("videoPlayed_1", 60000);

			logger.info("video played");

		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof SkipException){
				throw new SkipException("Test Skipped");
			}else
				result = false;
		}
		Assert.assertTrue(result, "Vertical Video tests failed");
	}

}