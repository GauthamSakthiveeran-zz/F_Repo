package com.ooyala.playback.playerlifecycle;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPlayerDestroyTests extends PlaybackWebTest {

	private PlayValidator play;
	private EventValidator eventValidator;

	public PlaybackPlayerDestroyTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerLifecycle", dataProvider = "testUrls", enabled = false)
	public void testPlayerDestroy(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			executeScript("pp.destroy();");

			result = result && eventValidator.validate("destroy_1", 50000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Player Destroy tests failed");
	}
}
