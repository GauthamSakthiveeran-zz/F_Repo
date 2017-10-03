package com.ooyala.playback.playerParameter;


import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayerAPIValidator;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by Gautham
 */
public class PlayerParametersAutoPlayClosedCaptionTests extends PlaybackWebTest {
	private static Logger logger = Logger
			.getLogger(PlayerParametersAutoPlayClosedCaptionTests.class);

	private EventValidator eventValidator;
	private CCValidator ccValidator;
	private PlayerAPIValidator apiValidator;
	private PlayerAPIAction playerAPI;

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
			String playerState = playerAPI.getState();
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
