package com.ooyala.playback.amf.postroll;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostrollDiscoveryTests extends PlaybackWebTest {

	public PlaybackPostrollDiscoveryTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private DiscoveryValidator discoveryValidator;
	private UpNextValidator upNextValidator;

	@Test(groups = { "amf", "postroll", "discovery", "upnext", "sequential" }, dataProvider = "testUrls")
	public void verifyPostrollDiscovery(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		
		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 150000);

			result = result && event.validate("seeked_1", 60000);

			result = result && upNextValidator.validate("", 300000);

			result = result && event.validate("PostRoll_willPlaySingleAd_1", 90000);

			result = result && (event.isAdPluginPresent("pulse") ? event.validate("singleAdPlayed_2", 90000)
					: event.validate("singleAdPlayed_1", 90000));

			((JavascriptExecutor) driver).executeScript("pp.pause();");

			result = result && discoveryValidator.validateDiscoveryToaster();

			result = result && discoveryValidator.validateLeftRightButton();

			result = result && discoveryValidator.clickOnDiscoveryCloseButton();
			
			result = result && event.validate("played_1", 10000);

			result = result && discoveryValidator.clickOnDiscoveryButton();

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Tests failed");
	}
}
