package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OoyalaAPIValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
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

	@Test(groups = { "playlist", "discovery", "amf" }, dataProvider = "testUrls", enabled = false) // TODO once the bug is fixed have to re-enable this
	public void testInitialTime(String testName, String url) throws OoyalaException {

		boolean result = true;
		try {

			driver.get(url);
			result = result && play.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 1000);
			
			if(!getBrowser().equalsIgnoreCase("MicrosoftEdge")) // TODO - skip button does not come up in edge
				result = result && skip.validate("", 120000);

			result = result && event.validate("singleAdPlayed_1", 900000);

			result = result && event.validate("ooyalaAds", 160000);

			result = result && event.validate("playing_2", 20000);

			result = result && api.validateInitailTime();
			
			Thread.sleep(10000); // play the video for sometime
			
			result = result && seek.validate("seeked_1", 20000);
			
			result = result && event.validate("played_1", 20000);

		} catch (Exception e) {
			e.getMessage();
			result = false;
		}
		Assert.assertTrue(result, "Playback Playlist tests failed" + testName);
	}

}
