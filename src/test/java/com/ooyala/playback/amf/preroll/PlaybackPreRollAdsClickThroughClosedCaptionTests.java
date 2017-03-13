package com.ooyala.playback.amf.preroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPreRollAdsClickThroughClosedCaptionTests extends PlaybackWebTest {

	public PlaybackPreRollAdsClickThroughClosedCaptionTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private CCValidator ccValidator;
	private AdClickThroughValidator clickThrough;

	@Test(groups = {"amf","preroll","cc","sequential", "clickThrough"}, dataProvider = "testUrls")
	public void verifyPreroll(String testName, String url)
			throws Exception {
		boolean result = true;

		try {
			
			boolean cc = testName.contains("CC");
			boolean click = testName.contains("Clickthrough");
			
			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();
			
			result = result && event.validate("PreRoll_willPlaySingleAd_1", 10000);
			
			if(result && click){
				s_assert.assertTrue(clickThrough.validate("", 120000), "Clickthrough failed.");
			}
			
			if (event.isAdPluginPresent("pulse"))
				result = result && event.validate("singleAdPlayed_2", 120000);
			else
				result = result && event.validate("singleAdPlayed_1", 120000);

			result = result && event.validate("playing_1", 30000);
			
			result = result && event.loadingSpinner();

			if(result && cc){
				s_assert.assertTrue(ccValidator.validate("cclanguage", 60000), "CC failed.");
			}
			
			result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("seeked_1", 120000);
			result = result && event.validate("played_1", 120000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		s_assert.assertTrue(result, "Playback PreRoll Ad tests failed");
		s_assert.assertAll();

	}

}
