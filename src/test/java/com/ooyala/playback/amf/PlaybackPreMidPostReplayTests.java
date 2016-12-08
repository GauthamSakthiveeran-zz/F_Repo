package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreMidPostReplayTests extends PlaybackWebTest {

	public PlaybackPreMidPostReplayTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private ReplayValidator replayValidator;
	private AdClickThroughValidator adClickThrough;

	@Test(groups = {"amf","preroll","midroll","postroll","replay"}, dataProvider = "testUrls")
	public void verifyPreMidPostcontrols(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

			result = result && playValidator.validate("playing_1", 120000);
			result = result && event.validate("PreRoll_willPlayAds", 120000);

			result = result && event.validate("MidRoll_willPlayAds", 120000);

			result = result && event.validate("PostRoll_willPlayAds", 120000);

			result = result && seekValidator.validate("seeked_1", 120000);
			result = result && event.validate("played_1", 190000);

			result = result && replayValidator.validate("replay_1", 120000);

			result = result
					&& event.validate("PreRoll_willPlayAds_OnReplay", 120000);
			
			result = result && playValidator.waitForPage();

			if (!getPlatform().equalsIgnoreCase("Android")) {
				result = result && adClickThrough.validate("", 120000);
			}

			result = result && event.validate("singleAdPlayed_4", 120000);

			result = result
					&& event.validate("MidRoll_willPlayAds_OnReplay", 120000);

			if (!getPlatform().equalsIgnoreCase("Android")) {
				result = result && adClickThrough.validate("", 120000);
			}

			result = result && event.validate("singleAdPlayed_5", 120000);

			result = result
					&& event.validate("PostRoll_willPlayAds_OnReplay", 120000);

			if (!getPlatform().equalsIgnoreCase("Android")) {
				result = result && adClickThrough.validate("", 120000);
			}

			result = result && event.validate("singleAdPlayed_6", 120000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
