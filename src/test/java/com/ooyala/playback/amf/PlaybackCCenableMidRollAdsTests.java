package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackCCenableMidRollAdsTests extends PlaybackWebTest {

	public PlaybackCCenableMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private CCValidator ccValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyCCenableMidRoll(String testName, String url)
			throws Exception {
		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("playing_1", 60000);

			result = result && event.validate("videoPlaying_1", 20000);

			result = result
					&& event.validate("MidRoll_willPlaySingleAd_1", 190000);

			Thread.sleep(14000);

			if (event.isAdPlugin("pulse"))
				result = result && event.validate("singleAdPlayed_2", 60000);
			else
				result = result && event.validate("singleAdPlayed_1", 60000);

			result = result && ccValidator.validate("cclanguage", 6000);

			result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("seeked_1", 10000);
			result = result && event.validate("played_1", 10000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result,
				"Playback CC Enabled MidRoll Ads tests failed");

	}
}
