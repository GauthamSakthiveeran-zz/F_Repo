package com.ooyala.playback.amf.postroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostRollPoddedAdsTests extends PlaybackWebTest {

	public PlaybackPostRollPoddedAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;

	@Test(groups = {"amf","postroll","podded"}, dataProvider = "testUrls")
	public void verifyPostrollPodded(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

			result = result && playValidator.validate("playing_1", 150000);
			result = result && seekValidator.validate("seeked_1", 180000);
			result = result && event.validate("videoPlayed_1", 180000);
			result = result && event.validate("played_1", 180000);

			result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds", 160000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
