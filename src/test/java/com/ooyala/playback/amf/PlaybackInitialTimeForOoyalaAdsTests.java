package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OoyalaAPIValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackInitialTimeForOoyalaAdsTests extends PlaybackWebTest {

	private PlayValidator play;
	private PlayAction playAction;
	private EventValidator event;
	private OoyalaAPIValidator api;
	private AdSkipButtonValidator skip;

	public PlaybackInitialTimeForOoyalaAdsTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "playlist", "discovery", "amf" }, dataProvider = "testUrls")
	public void testInitialTime(String testName, String url) throws OoyalaException {

		boolean result = true;
		try {

			driver.get(url);
			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 1000);
			
			result = result && skip.validate("", 120000);

			result = result && event.validate("singleAdPlayed_1", 900000);

			result = result && event.validate("ooyalaAds", 160000);

			result = result && event.validate("playing_2", 20000);

			result = result && api.validateInitailTime();

		} catch (Exception e) {
			e.getMessage();
			result = false;
		}
		Assert.assertTrue(result, "Playback Playlist tests failed" + testName);
	}

}
