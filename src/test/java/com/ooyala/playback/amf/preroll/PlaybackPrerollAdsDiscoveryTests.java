package com.ooyala.playback.amf.preroll;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPrerollAdsDiscoveryTests extends PlaybackWebTest {

	public PlaybackPrerollAdsDiscoveryTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private DiscoveryValidator discoveryValidator;
	private UpNextValidator upNextValidator;
	private SeekAction seekAction;

	@Test(groups = { "amf", "preroll", "discovery", "sequential" }, dataProvider = "testUrls")
	public void verifyPrerollDiscovery(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 10000);

			result = result && event.validate("singleAdPlayed_1", 150000);

			result = result && event.validate("playing_1", 150000);

			result = result && discoveryValidator.validate("reportDiscoveryClick_1", 60000);

			result = result && event.playVideoForSometime(3);

			result = result && seekAction.seek(15,true);

			result = result && event.waitOnElement(By.id("seeked_1"),20000);

			result = result && upNextValidator.validate("", 30000);

			result = result && event.waitOnElement(By.id("played_1"), 10000);

			result = result && discoveryValidator.validateDiscoveryToaster();
		} catch (Exception e) {
			logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL,e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback Pre Roll Ads Discovery Test failed");
	}
}
