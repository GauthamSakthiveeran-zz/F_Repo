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
public class PlayerParametersAutoPlayClosedCaptionTests extends PlaybackWebTest {
	private static Logger logger = Logger
			.getLogger(PlayerParametersAutoPlayClosedCaptionTests.class);

	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private CCValidator ccValidator;
	private AutoplayAction autoplayAction;
	private PlayerAPIValidator apiValidator;
	private ChromeFlashUpdateAction chromeValidator;

	public PlayerParametersAutoPlayClosedCaptionTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testClosedCaption(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			
			
			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();
			String playerState = driver.executeScript("return pp.getState()").toString();
	    	logger.info("Player State is : ========> " + playerState);

			injectScript();
			
			result = result && eventValidator.loadingSpinner();

			result = result && (apiValidator.isPlayerState("playing"));
			
			result = result && ccValidator.validate("cclanguage", 60000);


		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Closed Caption tests failed", e);

		}
		Assert.assertTrue(result, "Closed Caption tests failed");
	}
}
