package com.ooyala.playback.contentProtection;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.SyndicationRuleValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 3/23/17.
 */
public class PlaybackFlightTimeTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackGeoRestrictionTests.class);

	private ErrorDescriptionValidator error;
	private EventValidator event;
	private PlayValidator play;
	private SeekValidator seek;
	private SyndicationRuleValidator syndicationRuleValidator;

	PlaybackFlightTimeTests() throws OoyalaException {
		super();
	}

	@Test(groups = "syndicationRules", dataProvider = "testUrls")
	public void testFlightTime(String testName, UrlObject url) {
		boolean result = true;
		try {

			result = result && syndicationRuleValidator.updatePublishingRule(url.getEmbedCode(), url.getApiKey(),
					url.getSecret(), true);

			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 10000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && event.validate("played_1", 60000);

			result = result && syndicationRuleValidator.updatePublishingRule(url.getEmbedCode(), url.getApiKey(),
					url.getSecret(), false);

			Thread.sleep(10000);

			extentTest.log(LogStatus.INFO, "Applied the publishing rule for flight time.");

			driver.get(url.getUrl());

			result = result && event.isPageLoaded();

			result = result && error.expectedErrorCode("past").expectedErrorDesc("This video is no longer available")
					.validate("", 60000);

		} catch (Exception e) {
			logger.error("Error while checking flight time syndication" + e);
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}

		Assert.assertTrue(result, "flight time syndication test failed");
	}
}
