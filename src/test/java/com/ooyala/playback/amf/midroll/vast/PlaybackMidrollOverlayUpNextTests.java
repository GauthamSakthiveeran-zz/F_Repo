package com.ooyala.playback.amf.midroll.vast;

import com.ooyala.playback.page.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidrollOverlayUpNextTests extends PlaybackWebTest {

	public PlaybackMidrollOverlayUpNextTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private OverlayValidator overlayValidator;
	private DiscoveryValidator discoverValidator;
	private AdStartTimeValidator adStartTimeValidator;

	@Test(groups = {"amf","overlay","midroll","upnext","sequential"}, dataProvider = "testUrls")
	public void verifyMidrollOverlayUpNext(String testName, UrlObject url)throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 1000);

			if (url.getAdStartTime() != null && !url.getAdStartTime().isEmpty()){
				result = result && adStartTimeValidator.validateAdStartTime(url.getAdStartTime(),"MidRoll_willPlaySingleAd_1");
			}else
				result = result && event.validate("MidRoll_willPlaySingleAd_1", 20000);

			result = result && event.validate("singleAdPlayed_1", 30000);

            result = result && adStartTimeValidator.validateNonLinearAdStartTime("showNonlinearAd_1");

			result = result && overlayValidator.validate("nonlinearAdPlayed_1", 6000);

			result = result && overlayValidator.validateOverlayRenderingEvent(6000);

			result = result && discoverValidator.validate("reportDiscoveryClick_1",60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "test failed");
	}

}