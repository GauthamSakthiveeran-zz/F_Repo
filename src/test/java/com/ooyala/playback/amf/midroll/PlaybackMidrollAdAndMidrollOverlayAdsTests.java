package com.ooyala.playback.amf.midroll;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidrollAdAndMidrollOverlayAdsTests extends PlaybackWebTest {

	public PlaybackMidrollAdAndMidrollOverlayAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private OverlayValidator overLayValidator;
	private SeekValidator seekValidator;

	@Test(groups = { "amf", "midroll", "overlay", "sequential" }, dataProvider = "testUrls")
	public void verifyMidRoll(String testName, String url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

			result = result && event.validate("videoPlaying_1", 90000);
			
			Double time = 0D;
			
			while (time.intValue() <= 10) {
				logger.info(time.toString());
				time = Double.parseDouble(
						((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
			}

			result = result && event.validate("MidRoll_willPlayAds_1", 1000);
			
			result = result && event.validate("adsPlayed_1", 60000);
			
			time = 0D;
			
			while (time.intValue() <= 15) {
				logger.info(time.toString());
				time = Double.parseDouble(
						((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
			}

			result = result && event.validate("showNonlinearAd_1", 1000);
			
			if(!event.isAdPluginPresent("ima")) // because google puts in an iframe that isnt reachable
				result = result && overLayValidator.validate("nonlinearAdPlayed_1", 160000);

			result = result && seekValidator.validate("seeked_1", 160000);

			result = result && event.validate("videoPlayed_1", 160000);
			result = result && event.validate("played_1", 160000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}

}
