package com.ooyala.playback.playerSkin;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlayerSkinScrubberValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by Gautham
 */
public class PlaybackPlayerSkinAdTest extends PlaybackWebTest {
	private PlayValidator play;

	private PlayerSkinScrubberValidator adScrubberValidator;
	private PlayAction playAction;

	private static Logger logger = Logger.getLogger(PlaybackPlayerSkinAdTest.class);

	public PlaybackPlayerSkinAdTest() throws OoyalaException {
		super();
	}

	// Function to Verify ad Scrubber Bar , Countdown
	@Test(groups = "PlayerSkin", dataProvider = "testUrls")
	public void adTest(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {

			String urlLink = url.getUrl();

			driver.get(urlLink);
			
			result = result && play.waitForPage();
			
			injectScript();
			
			result = result && playAction.startAction();
			
			result = result && play.playVideoForSometime(4);

			s_assert.assertTrue(adScrubberValidator.verifyWebElementCSSColor("SCRUBBER_BAR", "background-color", "red"),
					"cssProperty Check Failed For SCRUBBER_BAR background color ");

			s_assert.assertTrue(adScrubberValidator.verifyWebElementCSSColor("PLAYED", "background-color", "yellow"),
					"cssProperty Check Failed For  Played SCRUBBER_BAR background color ");

			s_assert.assertTrue(adScrubberValidator.verifyWebElementCSSColor("BUFFERED", "background-color", "green"),
					"cssProperty Check Failed For Buffered SCRUBBER_BAR background color ");

			s_assert.assertTrue(adScrubberValidator.isCountdownNotPresent(), "Count Down is Present");

			s_assert.assertAll();

		} catch (Exception e) {
			logger.error(e);
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "PlayerSkin Localization tests failed :" + testName);
	}
}
