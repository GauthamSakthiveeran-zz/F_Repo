package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdPluginValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackVastPreIMAMidlAdsTests extends PlaybackWebTest {

	public PlaybackVastPreIMAMidlAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seek;
	private AdPluginValidator adPlugin;

	@Test(groups = { "amf", "preroll", "midroll" }, dataProvider = "testUrls", enabled=false)
	public void verifyVastPreIMAMidlAds(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 5000);

			result = result && event.validate("adsPlayed_1", 200000);

			result = result && adPlugin.setAdPlugin("vast").validate("admanager_details_2", 6000);
			
			result = result && event.validate("playing_1", 10000);

			result = result && event.validate("MidRoll_willPlayAds", 100000);

			result = result && event.validate("adsPlayed_2", 200000);

			result = result && adPlugin.setAdPlugin("ima").validate("admanager_details_4", 6000);
			
			result = result && seek.validate("seeked_1",200000);
			
			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		Assert.assertTrue(result, "Test failed");

	}

}
