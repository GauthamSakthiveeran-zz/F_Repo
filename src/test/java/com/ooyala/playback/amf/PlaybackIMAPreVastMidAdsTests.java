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

public class PlaybackIMAPreVastMidAdsTests extends PlaybackWebTest {

	public PlaybackIMAPreVastMidAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private AdPluginValidator adPlugin;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyIMAPreVastMidAds(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 120000);
			result = result && event.validate("adsPlayed_1", 200000);
			
			result = result && adPlugin.setAdPlugin("ima").validate("admanager_details_1", 6000);

			result = result && event.validate("playing_1", 20000);

			result = result && event.validate("MidRoll_willPlayAds_2", 100000);
			result = result && event.validate("adsPlayed_2", 200000);
			
			result = result && adPlugin.setAdPlugin("vast").validate("admanager_details_3", 6000);
			
			result = result && seekValidator.validate("seeked_1", 190000);
			
			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
