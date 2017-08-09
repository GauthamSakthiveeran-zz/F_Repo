package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.StateScreenValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlayerStatePauseScreenTests extends PlaybackWebTest {

	private PlayValidator play;
	private EventValidator eventValidator;
	private PauseValidator pauseValidator;
	private StateScreenValidator screenValidator;

	public PlayerStatePauseScreenTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testDiscovery(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && eventValidator.playVideoForSometime(5);

			result = result && pauseValidator.validate("paused_1", 10000);

			result = result && screenValidator.validateTitle("test7.mp4")
					&& screenValidator.validateDescription("Test description 1234 ");

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "PlayerStatePauseScreenTests failed");
	}
}