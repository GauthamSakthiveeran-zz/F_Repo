package com.ooyala.playback.VTC;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DifferentElementValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by snehal on 28/11/16.
 */
public class PlaybackAdVideoSamePluginDiffElementTests extends PlaybackWebTest {
	private static Logger logger = Logger.getLogger(PlaybackAdVideoSamePluginDiffElementTests.class);
	private PlayValidator play;
	private EventValidator eventValidator;
	private PlayAction playAction;
	private SeekValidator seekValidator;
	private DifferentElementValidator elementValidator;

	PlaybackAdVideoSamePluginDiffElementTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testSamePluginsDiffElementTests(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.validate("playing_1", 60000);

			result = result && elementValidator.validate("VIDEO_PATH", 30000);

			result = result && seekValidator.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback Same plugins Different Element test failed");
	}

}
