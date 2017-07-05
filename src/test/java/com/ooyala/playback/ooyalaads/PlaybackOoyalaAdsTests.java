package com.ooyala.playback.ooyalaads;

import com.ooyala.playback.report.ExtentManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackOoyalaAdsTests extends PlaybackWebTest {

	public PlaybackOoyalaAdsTests() throws OoyalaException {
		super();
	}

	private static Logger logger = Logger.getLogger(PlaybackOoyalaAdsTests.class);
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seek;

	@Test(groups = { "amf", "ooyalaads" }, dataProvider = "testUrls")
	public void verifyPreroll(String testName, UrlObject url) throws Exception {
		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("ooyalaAds", 120000);

			result = result && event.validate("playing_2", 120000);

			result = result && seek.validate("seeked_1", 120000);

			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Test failed");
	}
}
