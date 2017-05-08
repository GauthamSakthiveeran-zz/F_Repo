package com.ooyala.playback.amf.preroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPrerollAdsDiscoveryTests extends PlaybackWebTest {

	public PlaybackPrerollAdsDiscoveryTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private DiscoveryValidator discoveryValidator;

	@Test(groups = { "amf", "preroll", "discovery", "sequential" }, dataProvider = "testUrls")
	public void verifyPrerollDiscovery(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 10000);

			result = result && event.validate("singleAdPlayed_1", 150000);

			result = result && event.validate("playing_1", 150000);

			result = result && discoveryValidator.validate("reportDiscoveryClick_1", 60000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Test failed");
	}
}
