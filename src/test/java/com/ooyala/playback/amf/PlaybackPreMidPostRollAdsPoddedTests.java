package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreMidPostRollAdsPoddedTests extends PlaybackWebTest {

	public PlaybackPreMidPostRollAdsPoddedTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private PoddedAdValidator poddedAdValidator;
	private SeekAction seekAction;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPreMidPostrollPodded(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 60000);

			result = result && event.validate("adsPlayed_1", 200000);

			result = result && poddedAdValidator.validate("countPoddedAds_1", 60000);

			result = result && event.validate("playing_1", 90000);
			result = result && seekAction.setFactor(2).fromLast().setTime(10).startAction();
			result = result && event.validate("MidRoll_willPlayAds", 200000);
			result = result && event.validate("adsPlayed_2", 200000);

			result = result && poddedAdValidator.validate("countPoddedAds_2", 600000);
			result = result && event.validate("PostRoll_willPlayAds", 180000);
			result = result && event.validate("adsPlayed_3", 200000);

			result = result && event.validate("PostRoll_willPlayAds", 180000);
			result = result && event.validate("adsPlayed_3", 200000);

			result = result && poddedAdValidator.validate("countPoddedAds_3", 600000);

			result = result && event.validate("played_1", 180000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}
}
