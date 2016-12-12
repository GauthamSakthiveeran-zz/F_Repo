package com.ooyala.playback.amf.preroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackCCenabledPreRollAdsTests extends PlaybackWebTest {

	public PlaybackCCenabledPreRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private CCValidator ccValidator;

	@Test(groups = {"amf","preroll","cc"}, dataProvider = "testUrls")
	public void verifyCCenabledPreroll(String testName, String url)
			throws Exception {
		boolean result = true;

		try {
			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();
			
			result = result && event.validate("PreRoll_willPlaySingleAd_1", 6000);
			
			if (event.isAdPluginPresent("pulse"))
				result = result && event.validate("singleAdPlayed_2", 60000);
			else
				result = result && event.validate("singleAdPlayed_1", 60000);

			result = result && event.validate("playing_1", 5000);

			result = result && ccValidator.validate("cclanguage", 60000);

			result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("seeked_1", 5000);
			result = result && event.validate("played_1", 60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback CC Enabled PreRoll Ad tests failed");

	}

}
