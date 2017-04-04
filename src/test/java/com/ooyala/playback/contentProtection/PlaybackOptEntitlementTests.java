package com.ooyala.playback.contentProtection;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DRMValidator;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StreamTypeValidator;
import com.ooyala.playback.page.SyndicationRuleValidator;
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
	private ErrorDescriptionValidator error;
	private SyndicationRuleValidator syndicationRuleValidator;
	private DRMValidator drm;
	private StreamTypeValidator streams;

	public PlaybackOptEntitlementTests() throws OoyalaException {
		super();
	}

	@Test(groups = "syndicationRules", dataProvider = "testUrls")
	public void testOptEntitlement(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			
			result = result && syndicationRuleValidator.deleteEntitlement(url.getEmbedCode(), url.getPCode());
			
			Thread.sleep(5000);

			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

			result = result && error.expectedErrorCode("sas").expectedErrorDesc("Invalid Authorization Response")
					.validate("", 1000);

			result = result && syndicationRuleValidator.createEntitlement(url.getEmbedCode(), url.getPCode(), 2);
			
			Thread.sleep(5000);

			driver.get(url.getUrl());
			
			result = result && play.waitForPage();
			
			result = result && drm.opt().validate("drm_tag", 5000);

			injectScript();

			result = result && play.validate("playing_1", 60000);
			
			result = result && streams.setStreamType("mpd").validate("videoPlayingurl", 1000);
			
			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while checking entitlement" + e);
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "OPT Entitlement tests failed");
	}
}