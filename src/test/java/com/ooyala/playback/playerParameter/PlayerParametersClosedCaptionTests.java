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
public class PlayerParametersClosedCaptionTests extends PlaybackWebTest {
	private static Logger logger = Logger
			.getLogger(PlayerParametersClosedCaptionTests.class);

	private PlayValidator play;
	private PauseValidator pause;
	private SeekValidator seek;
	private EventValidator eventValidator;
	private CCValidator ccValidator;
	private AutoplayAction autoplayAction;
	private PlayerAPIValidator apiValidator;
	private ChromeFlashUpdateAction chromeValidator;

	public PlayerParametersClosedCaptionTests() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testClosedCaption(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			
			
			if (testName.contains("OSMF") && getBrowser().contains("chrome")) {

				driver.get("chrome://components/");

				result = result &&	chromeValidator.isFlashPluginUpdated();

			}
			
			
			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

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
