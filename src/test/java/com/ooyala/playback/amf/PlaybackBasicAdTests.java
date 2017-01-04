package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackBasicAdTests extends PlaybackWebTest {

	public PlaybackBasicAdTests() throws OoyalaException {
		super();
	}

	private EventValidator eventValidator;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;

	@Test(groups = "amf", dataProvider = "testUrls", enabled = false)
	public void verifyBasicAd(String testName, String url) throws Exception {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.loadingSpinner();

			result = result
					&& eventValidator.validate("willPlaySingleAd_1", 150000);

			result = result
					&& eventValidator.validate("singleAdPlayed_1", 150000);

			result = result && eventValidator.validate("playing_1", 120000);

			result = result && seekValidator.validate("seeked_1", 190000);

			result = result && eventValidator.validate("played_1", 190000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Ad Wrapper tests failed");

	}

}
