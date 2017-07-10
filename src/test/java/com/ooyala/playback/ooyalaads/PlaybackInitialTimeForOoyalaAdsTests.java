package com.ooyala.playback.ooyalaads;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OoyalaAPIValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackInitialTimeForOoyalaAdsTests extends PlaybackWebTest {

	private PlayValidator play;
	private PlayAction playAction;
	private EventValidator event;
	private OoyalaAPIValidator api;
	private AdSkipButtonValidator skip;
	private SeekValidator seek;

	public PlaybackInitialTimeForOoyalaAdsTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "ooyalads" }, dataProvider = "testUrls") 
	public void testInitialTime(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url.getUrl());
			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 1000);
			
			if(!getBrowser().equalsIgnoreCase("MicrosoftEdge")) // TODO - skip button does not come up in edge
				result = result && skip.validate("", 120000);

			result = result && event.validate("singleAdPlayed_1", 900000);

			result = result && event.validate("ooyalaAds_1", 160000);

			result = result && event.validate("playing_2", 90000);

			result = result && api.validateInitailTime();
			
			result = result && seek.validate("seeked_1", 90000);
			
			result = result && event.validate("played_1", 90000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback Playlist tests failed" + testName);
	}
}
