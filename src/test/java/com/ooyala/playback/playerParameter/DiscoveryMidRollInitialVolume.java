package com.ooyala.playback.playerParameter;


import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;


public class DiscoveryMidRollInitialVolume extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(DiscoveryAutoPlayUpNextFalse.class);
	private EventValidator eventValidator;
	private AutoplayAction autoplayAction;
	private SeekAction seekAction;
    private UpNextValidator discoveryUpNext;
    private PlayValidator play;
    private VolumeValidator volumeValidator;
	public DiscoveryMidRollInitialVolume() throws OoyalaException {
		super();
	}

	@Test(groups = "autoplay", dataProvider = "testUrls")
	public void testDiscoveryPostRollWithInitialTime(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();	
			

			injectScript();

			result = result && eventValidator.loadingSpinner();

			result = result && autoplayAction.startAction();

			result = result && volumeValidator.validateInitialVolume(0.5);
			result = result && seekAction.fromLast().setTime(30).startAction();

			result = result && eventValidator.validate("seeked_1", 60000);

			result = result && discoveryUpNext.validate("", 300000);

			result = result && eventValidator.validate("singleAdPlayed_1", 90000);

			
			result = result && eventValidator.validate("played_1", 10000);

			
			

		} catch (Exception e) {
			logger.error("Exception while checking autoplay for discovery videos  " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "DiscoveryPostRollAutoPlayInitialTime tests failed");
	}
}
