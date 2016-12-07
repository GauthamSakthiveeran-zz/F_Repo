package com.ooyala.playback.amf.VAST;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidrollOverlayUpNextTests extends PlaybackWebTest {

	public PlaybackMidrollOverlayUpNextTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private OverlayValidator overLayValidator;
	private DiscoveryValidator discoverValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMidrollOverlayUpNext(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 1000);

			result = result
					&& event.validate("MidRoll_willPlaySingleAd_1", 20000);

			result = result && event.validate("singleAdPlayed_1", 10000);

			result = result && event.validate("showNonlinearAd_1", 60000);

			result = result
					&& overLayValidator.validate("nonlinearAdPlayed_1", 2000);

			result = result
					&& discoverValidator.validate("reportDiscoveryClick_1",
							60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "test failed");
	}

}
