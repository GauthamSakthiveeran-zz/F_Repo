package com.ooyala.playback.contentProtection;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SaasPortValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by snehal on 23/11/16.
 */
public class PlaybackOptEntitlementTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackOptEntitlementTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private SeekValidator seek;
	private SaasPortValidator sasport;
	private ErrorDescriptionValidator error;

	public PlaybackOptEntitlementTests() throws OoyalaException {
		super();
	}

	@Test(groups = "syndicationRules", dataProvider = "testUrls")
	public void testOptEntitlementAlice(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			
			driver.get(url.getUrl());

			result = result && sasport.getProperties();
			result = result && sasport.searchEntitlement();
			result = result && sasport.deleteEntitlement();
			
			Thread.sleep(5000);

			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

			result = result && error.expectedErrorCode("sas").expectedErrorDesc("Invalid Authorization Response")
					.validate("", 1000);

			result = result && sasport.searchEntitlement();

			result = result && sasport.createEntitlement("");
			
			Thread.sleep(5000);

			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);
			
			Thread.sleep(10000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			logger.error("Error while checking entitlement" + e);
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "OPT Entitlement tests failed");
	}
}