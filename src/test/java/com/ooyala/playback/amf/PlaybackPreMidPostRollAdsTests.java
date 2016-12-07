package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreMidPostRollAdsTests extends PlaybackWebTest {

	public PlaybackPreMidPostRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;

	@Test(groups = {"amf","preroll","midroll","postroll"}, dataProvider = "testUrls")
	public void verifyPreMidPostroll(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 150000);

			result = result && event.validate("adsPlayed_1", 200000);

			result = result && event.validate("playing_1", 150000);

			result = result && seekAction.setTime(15).fromLast().startAction();

			result = result && event.validate("MidRoll_willPlayAds", 150000);
			result = result && event.validate("adsPlayed_2", 150000);

			result = result && seekAction.setTime(30).fromLast().startAction();

			result = result && event.validate("PostRoll_willPlayAds", 150000);

			if (event.isAdPluginPresent("pulse")) {
				result = result && event.validate("singleAdPlayed_6", 60000);
				result = result && event.validate("seeked_1", 60000);
			} else
				result = result && event.validate("adsPlayed_3", 60000);

			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified Pre Mid Post Roll Ads failed.");

	}

}
