package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreRollPoddedAdsTests extends PlaybackWebTest {

	public PlaybackPreRollPoddedAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPrerollOverlay(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

			result = result && playValidator.validate("playing_1", 120000);

			result = result && event.validate("adsPlayed_1", 180000);

			result = result
					&& poddedAdValidator.validate("countPoddedAds", 120000);

			result = result && seekValidator.validate("seeked_1", 180000);
			result = result && event.validate("played_1", 180000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
