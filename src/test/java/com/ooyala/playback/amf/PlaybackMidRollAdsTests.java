package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekAction seekAction;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMidRoll(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

			result = result && event.validate("videoPlaying_1", 90000);

			if (event.isVideoPluginPresent("akamai")) {
				result = result && event.validate("MidRoll_willPlayAds_1", 120000);
				result = result && event.validate("adsPlayed_1", 60000);
			} else {
				result = result && event.validate("MidRoll_willPlaySingleAd_1", 120000);
				if (event.isAdPluginPresent("pulse"))
					result = result && event.validate("singleAdPlayed_2", 60000);
				else
					result = result && event.validate("singleAdPlayed_1", 60000);
			}

			result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("played_1", 160000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}
}
