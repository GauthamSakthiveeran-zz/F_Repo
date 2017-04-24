package com.ooyala.playback.contentProtection;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.BitmovinTechnologyValidator;
import com.ooyala.playback.page.DRMValidator;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.playback.utils.SyndicationRules;
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
	private DRMValidator drm;
	private StreamValidator streams;
	private BitmovinTechnologyValidator tech;
	private PauseValidator pause;
	private PlayAction playAction;

	public PlaybackOptEntitlementTests() throws OoyalaException {
		super();
		
	}

	@Test(groups = "syndicationRules", dataProvider = "testUrls")
	public void testOptEntitlement(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			
			SyndicationRules syndicationRules = new SyndicationRules(extentTest);

			result = result && syndicationRules.deleteEntitlement(url.getEmbedCode(), url.getPCode());
			result = result && syndicationRules.deleteDevices(url.getPCode());

			driver.get(url.getUrl());

			result = result && eventValidator.isPageLoaded();

			result = result && error.expectedErrorCode("sas").expectedErrorDesc("Invalid Authorization Response")
					.validate("", 1000);

			result = result && syndicationRules.createEntitlement(url.getEmbedCode(), url.getPCode(), 2);

			driver.get(url.getUrl());

			result = result && play.waitForPage();

			if(!testName.contains("Core:")) // Core has non drm assets
				result = result && drm.opt().validate("drm_tag", 5000);

			injectScript();

			tech.getConsoleLogs();

			result = result && play.validate("playing_1", 60000);
			
			result = result && eventValidator.playVideoForSometime(5);
			
			result = result && streams.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);

			result = result && tech.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);

			result = result && pause.validate("paused_1", 60000);

			result = result && playAction.startAction();

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);
			
			result = result && syndicationRules.deleteDevices(url.getPCode());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while checking entitlement" + e);
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "OPT Entitlement tests failed");
	}
}