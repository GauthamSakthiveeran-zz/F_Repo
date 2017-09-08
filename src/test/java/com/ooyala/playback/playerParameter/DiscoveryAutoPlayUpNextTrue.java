package com.ooyala.playback.playerParameter;


import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;


public class DiscoveryAutoPlayUpNextTrue extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(DiscoveryAutoPlayUpNextFalse.class);
	private EventValidator eventValidator;
	private AutoplayAction autoplayAction;
	private SeekAction seekAction;
    private UpNextValidator discoveryUpNext;
    private PlayValidator play;
	public DiscoveryAutoPlayUpNextTrue() throws OoyalaException {
		super();
	}

	@Test(groups = "autoplay", dataProvider = "testUrls")
	public void testDiscoveryWithAutoPlayUpNextTrue(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

			injectScript();

			result = result && eventValidator.loadingSpinner();

			result = result && play.clickOnIndependentElement("PLAY_BUTTON");

			result = result && eventValidator.validate("singleAdPlayed_1", 5000);

			result = result && eventValidator.validate("playing_1", 60000);

			result = result && eventValidator.playVideoForSometime(6);

			result = result && seekAction.seek(10, true);

			result = result && eventValidator.validate("seeked_1", 20000);

			result = result && eventValidator.validate("played_1", 60000);
			
			result = result && discoveryUpNext.autoPlayUpNextVideo();
			

		} catch (Exception e) {
			logger.error("Exception while checking autoplay for discovery videos  " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "DiscoveryAutoPlayUpNextFalse tests failed");
	}
}
