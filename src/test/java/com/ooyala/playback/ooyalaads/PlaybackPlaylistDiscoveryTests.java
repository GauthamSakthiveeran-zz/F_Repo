package com.ooyala.playback.ooyalaads;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlaylistValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPlaylistDiscoveryTests extends PlaybackWebTest {

	private PlaylistValidator playlist;
	private PlayValidator play;
	private PlayAction playAction;
	private EventValidator event;
	private DiscoveryValidator discoveryValidator;

	public PlaybackPlaylistDiscoveryTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "playlist", "discovery", "ooyalads" }, dataProvider = "testUrls")
	public void testPlaylistTests(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {

			driver.get(url.getUrl());
			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 1000);

			result = result && event.validate("adsPlayed_1", 160000);

			if (testName.contains("Ooyala Ads")) {
				result = result && event.validate("ooyalaAds", 160000);
			}

			result = result && event.validate("playing_2", 20000);
			
			result = result && event.playVideoForSometime(5);

			if (url.getAdditionalPlugins().contains("PLAYLIST")) {
				result = result && playlist.scrollToEitherSide();
			}

			if (url.getAdditionalPlugins().contains("DISCOVERY")) {
				result = result && discoveryValidator.validate("reportDiscoveryClick_1", 60000);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Test failed");
	}
}
