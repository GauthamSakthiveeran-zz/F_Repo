package com.ooyala.playback.amf.preroll;

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

	@Test(groups = {"amf","preroll","podded"}, dataProvider = "testUrls")
	public void verifyPrerollPodded(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();
			injectScript();

			result = result && playValidator.validate("playing_1", 10000);

			result = result && event.validate("PreRoll_willPlayAds", 1000);
			result = result && event.validate("adsPlayed_1", 180000);

			result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 120000);

			result = result && seekValidator.validate("seeked_1", 6000);
			result = result && event.validate("played_1", 90000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
