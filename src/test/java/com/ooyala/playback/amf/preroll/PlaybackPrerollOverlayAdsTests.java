package com.ooyala.playback.amf.preroll;

import com.ooyala.playback.page.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPrerollOverlayAdsTests extends PlaybackWebTest {

	public PlaybackPrerollOverlayAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private OverlayValidator overlayValidator;
	private SeekValidator seekValidator;

	@Test(groups = {"amf","preroll","overlay"}, dataProvider = "testUrls")
	public void verifyPrerollOverlay(String testDescription, UrlObject url) throws OoyalaException {

		boolean result = true;

		String adManager = testDescription.split(":")[0].split("-")[1].trim();

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();
			
			result = result && event.validate("willPlayNonlinearAd_1", 5000);
			
			if (!event.isAdPluginPresent("ima")){
				result = result && overlayValidator.validateClickThrough(adManager,7000);
                result = result && overlayValidator.validate("nonlinearAdPlayed_1", 160000);
				result = result && overlayValidator.validateOverlayRenderingEvent(6000);
			}
			
			result = result && event.validate("videoPlaying_1", 90000);

			result = result && seekValidator.validate("seeked_1", 6000);

			result = result && event.validate("played_1", 190000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Test failed");

	}

}
