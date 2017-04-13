package com.ooyala.playback.contentProtection;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.GeoValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.SyndicationRuleValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 3/22/17.
 */
public class PlaybackGeoRestrictionTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackGeoRestrictionTests.class);
	private GeoValidator geo;
	private EventValidator event;
	private PlayValidator play;
	private SeekValidator seek;
	private SyndicationRuleValidator syndicationRuleValidator;

	public PlaybackGeoRestrictionTests() throws OoyalaException {
		super();
	}

	@Test(groups = "syndicationRules", dataProvider = "testUrls")
	public void testGeoRestriction(String testName, UrlObject url) {
		boolean result = true;
		try {

			result = result && syndicationRuleValidator.updatePublishingRule(url.getEmbedCode(), url.getApiKey(), true);

			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 10000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && event.validate("played_1", 60000);

			result = result
					&& syndicationRuleValidator.updatePublishingRule(url.getEmbedCode(), url.getApiKey(), false);

			Thread.sleep(10000);

			extentTest.log(LogStatus.INFO, "Applied the publishing rule for geo");

			driver.get(url.getUrl());

			Thread.sleep(5000);

			driver.get(url.getUrl());

			result = result && event.isPageLoaded();

			result = result && geo.validate("", 60000);

		} catch (Exception e) {
			logger.error("Error while checking geo restriction" + e);
			extentTest.log(LogStatus.FAIL, "Error while checking geo restriction", e);
			result = false;
		}

		Assert.assertTrue(result, "geo restriction test failed");
	}
}
