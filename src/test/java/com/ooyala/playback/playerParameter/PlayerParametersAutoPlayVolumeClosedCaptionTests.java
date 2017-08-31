package com.ooyala.playback.playerParameter;


import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.page.action.ChromeFlashUpdateAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Gautham
 */
public class PlayerParametersAutoPlayVolumeClosedCaptionTests extends PlaybackWebTest {
	private static Logger logger = Logger
			.getLogger(PlayerParametersAutoPlayVolumeClosedCaptionTests.class);

	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private CCValidator ccValidator;
	private AutoplayAction autoplayAction;
	private PlayerAPIValidator apiValidator;
	private ChromeFlashUpdateAction chromeValidator;

	public PlayerParametersAutoPlayVolumeClosedCaptionTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testClosedCaption(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			
			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

			injectScript();
			
			result = result && eventValidator.loadingSpinner();
			
			Thread.sleep(2000);

			result = result && (apiValidator.isPlayerState("playing"));
			
			result = result && (apiValidator.isPlayerVolume("0.5"));
			
			result = result && ccValidator.validate("cclanguage", 60000);


		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Closed Caption tests failed", e);

		}
		Assert.assertTrue(result, "Closed Caption tests failed");
	}
}
