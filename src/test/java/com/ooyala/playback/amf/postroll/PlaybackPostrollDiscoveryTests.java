package com.ooyala.playback.amf.postroll;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostrollDiscoveryTests extends PlaybackWebTest {

	public PlaybackPostrollDiscoveryTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private DiscoveryValidator discoveryValidator;
	private SeekAction seekAction;
	private UpNextValidator upNextValidator;

	@Test(groups = {"amf","postroll","discovery","upnext","sequential"}, dataProvider = "testUrls")
	public void verifyPostrollDiscovery(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 150000);
			
			if (!event.isAdPluginPresent("pulse"))
				result = result && seekAction.fromLast().setTime(30).startAction();
			
			result = result && event.loadingSpinner();
			
			result = result && upNextValidator.validate("", 60000);
			
			result = result && event.validate("PostRoll_willPlaySingleAd_1", 90000);

			if (event.isAdPluginPresent("pulse"))
				result = result && event.validate("singleAdPlayed_2", 90000);
			else
				result = result && event.validate("singleAdPlayed_1", 90000);
			
			((JavascriptExecutor) driver).executeScript("pp.pause();");
			
			result = result && discoveryValidator.validateDiscoveryToaster();

			result = result && discoveryValidator.validateLeftRightButton();

			result = result && discoveryValidator.clickOnDiscoveryCloseButton();
			
			result = result && discoveryValidator.clickOnDiscoveryButton();

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
