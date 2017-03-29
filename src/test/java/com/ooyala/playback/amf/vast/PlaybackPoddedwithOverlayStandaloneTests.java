package com.ooyala.playback.amf.vast;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPoddedwithOverlayStandaloneTests extends PlaybackWebTest {

	public PlaybackPoddedwithOverlayStandaloneTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private OverlayValidator overlayValidator;

	@Test(groups = {"amf","podded","overlay"}, dataProvider = "testUrls", enabled=false)
	public void verifyPoddedStandaloneOverlay(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("willPlaySingleAd_1", 10000);

			result = result && event.validate("singleAdPlayed_1", 150000);

			result = result && event.validate("willPlaySingleAd_2", 160000);

			result = result && event.validate("singleAdPlayed_2", 160000);

			result = result && event.validate("playing_1", 6000);

			result = result && overlayValidator.validate("nonlinearAdPlayed_1", 90000);

			result = result && seekValidator.validate("seeked_1", 5000);

			result = result && event.validate("played_1", 5000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
