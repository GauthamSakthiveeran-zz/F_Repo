package com.ooyala.playback.amf.midroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.MidrollAdValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidrollAdAndMidrollOverlayAdsTests extends PlaybackWebTest {

	public PlaybackMidrollAdAndMidrollOverlayAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private OverlayValidator overlayValidator;
	private SeekValidator seekValidator;
	private MidrollAdValidator midrollAdValidator;

	@Test(groups = { "amf", "midroll", "overlay", "sequential" }, dataProvider = "testUrls")
	public void verifyMidrollAdAndMidrollOverlayAdsTests(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		
		if(!testName.contains("IMA:Main Midroll (10) and Overlay (15) with Discovery Up Next"))
			return;

		try {
			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

			result = result && event.validate("videoPlaying_1", 90000);

			if (midrollAdValidator.isAdPlayTimePresent(url)) {
				result = result && midrollAdValidator.setTime(url.getAdStartTime())
						.validateAdStartTime("MidRoll_willPlayAds_1");
			} else
				result = result && event.validate("MidRoll_willPlayAds_1", 1000);

			result = result && event.validate("adsPlayed_1", 60000);

			if (midrollAdValidator.isOverlayPlayTimePresent(url)) {
				result = result && midrollAdValidator.setTime(url.getOverlayPlayTime())
						.validateAdStartTime("showNonlinearAd_1");
			} else
				result = result && event.validate("showNonlinearAd_1", 1000);

			result = result && seekValidator.validate("seeked_1", 160000);

			// because google puts in an iframe that isnt reachable
			if (!event.isAdPluginPresent("ima")) {
				result = result && overlayValidator.validate("nonlinearAdPlayed_1", 160000);
				result = result && overlayValidator.validateOverlayRenderingEvent(6000);
			}

			result = result && event.validate("played_1", 160000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}
		Assert.assertTrue(result, "Verified");
	}
}
