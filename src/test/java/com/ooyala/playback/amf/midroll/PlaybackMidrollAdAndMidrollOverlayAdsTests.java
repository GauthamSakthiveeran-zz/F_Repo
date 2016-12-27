package com.ooyala.playback.amf.midroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidrollAdAndMidrollOverlayAdsTests extends PlaybackWebTest {

	public PlaybackMidrollAdAndMidrollOverlayAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private OverlayValidator overLayValidator;
	private AdClickThroughValidator adClicks;
	private SeekValidator seekValidator;

	@Test(groups = { "amf", "midroll", "overlay" }, dataProvider = "testUrls")
	public void verifyMidRoll(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

			result = result && event.validate("videoPlaying_1", 90000);

			result = result && event.validate("MidRoll_willPlayAds_1", 120000);
			result = result && event.validate("adsPlayed_1", 60000);

			result = result && event.validate("showNonlinearAd_1", 160000);

			result = result && adClicks.overlay().validate("", 120000);

			result = result && overLayValidator.validate("nonlinearAdPlayed_1", 160000);

			result = result && seekValidator.validate("seeked_1", 160000);

			result = result && event.validate("videoPlayed_1", 160000);
			result = result && event.validate("played_1", 160000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}

}
