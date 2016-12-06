package com.ooyala.playback.amf.VAST;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackVastPreIMAMidlAdsTests extends PlaybackWebTest{

	public PlaybackVastPreIMAMidlAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPrerollOverlay(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();
			
			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 120000);

            result = result && event.validate("adsPlayed_1", 200000);

            result = result && event.validate("adPodEnd_vast_2_1", 180000);

            result = result && event.validate("playing_1", 90000);

            result = result && seekAction.seekTillEnd().startAction();

            result = result && event.validate("MidRoll_willPlayAds", 100000);

            result = result && event.validate("adsPlayed_2", 200000);

            result = result &&  event.validate("AD_POD_END_IMA", 20000);

            result = result &&  event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
