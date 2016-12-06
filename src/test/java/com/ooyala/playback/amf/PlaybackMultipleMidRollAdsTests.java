package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMultipleMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMultipleMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private PoddedAdValidator poddedAdValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMultipleMidroll(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

			result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

			result = result && playValidator.validate("playing_1", 90000);

			result = result && seekAction.setAdPlugin("pulse").startAction();

			result = result && event.validate("videoPlayed_1", 200000);

			result = result
					&& poddedAdValidator.validate("countPoddedAds", 60000);

			if (event.isAdPlugin("pulse"))
				result = result && event.validate("seeked_1", 60000);

			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified Multiple MidRoll Ads");

	}
}
