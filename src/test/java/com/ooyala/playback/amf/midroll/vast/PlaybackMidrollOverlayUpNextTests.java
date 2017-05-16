package com.ooyala.playback.amf.midroll.vast;

import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.PlayAction;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidrollOverlayUpNextTests extends PlaybackWebTest {

	public PlaybackMidrollOverlayUpNextTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private OverlayValidator overlayValidator;
	private DiscoveryValidator discoverValidator;
	private MidrollAdValidator adStartTimeValidator;
	PlayAction playAction;

	@Test(groups = {"amf","overlay","midroll","upnext","sequential"}, dataProvider = "testUrls")
	public void verifyMidrollOverlayUpNext(String testName, UrlObject url)throws OoyalaException {
		
		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

            if (adStartTimeValidator.isAdPlayTimePresent(url)) {
                result = result && adStartTimeValidator.setTime(url.getAdStartTime()).validateAdStartTime("MidRoll_willPlaySingleAd_1");
            } else
                result = result && event.validate("MidRoll_willPlaySingleAd_1",200000);

			result = result && event.validate("singleAdPlayed_1", 30000);

            if (adStartTimeValidator.isOverlayPlayTimePresent(url)) {
                result = result && adStartTimeValidator.setTime(url.getOverlayPlayTime()).validateAdStartTime("showNonlinearAd_1");
            }

			result = result && overlayValidator.validateOverlayRenderingEvent(6000);

            result = result && overlayValidator.validate("nonlinearAdPlayed_1", 6000);

			result = result && discoverValidator.validate("reportDiscoveryClick_1",60000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}
		Assert.assertTrue(result, "test failed");
	}
}