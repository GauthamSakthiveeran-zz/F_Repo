package com.ooyala.playback.playerfeatures;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackClosedCaptionTests extends PlaybackWebTest {
	private static Logger logger = Logger
			.getLogger(PlaybackClosedCaptionTests.class);

	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private CCValidator ccValidator;

	public PlaybackClosedCaptionTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testClosedCaption(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		logger.info("Executing PlaybackClosedCaption test  ");
		try {
			driver.get(url);

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			sleep(1000);

			result = result && pause.validate("paused_1", 60000);

			result = result && play.validate("playing_2", 60000);

			sleep(1000);

			result = result && ccValidator.validate("cclanguage", 60000);

			sleep(1000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Closed Caption tests failed");
	}
}
