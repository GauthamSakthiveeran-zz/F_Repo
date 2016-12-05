package com.ooyala.playback.amf.VAST;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPoddedSkippbableInsideTests extends PlaybackWebTest {

	public PlaybackPoddedSkippbableInsideTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private AdSkipButtonValidator skipValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPoddedInsideSkipButton(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

	        result = result && event.validate("willPlaySingleAd_1", 150000);
	        result = result && event.validate("singleAdPlayed_1", 150000);

	        result = result && event.validate("willPlaySingleAd_2", 150000);

	        result = result && skipValidator.validate("", 120000);
	        
	        result = result && event.validate("willPlaySingleAd_3", 150000);

	        result = result && event.validate("singleAdPlayed_3", 150000);

	        result = result && event.validate("playing_1", 190000);

	        result = result && seekValidator.validate("seeked_1", 190000);
	        result = result && event.validate("played_1", 190000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}


}
