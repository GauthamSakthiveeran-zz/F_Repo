package com.ooyala.playback.contentProtection;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.BitmovinTechnologyValidator;
import com.ooyala.playback.page.DRMValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.SyndicationRuleValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by snehal on 14/02/17.
 */
public class PlaybackDeviceRegistrationTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackDeviceRegistrationTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private SeekValidator seek;
	private SyndicationRuleValidator syndicationRuleValidator;
	private DRMValidator drm;
	private StreamValidator streams;
	private BitmovinTechnologyValidator tech;
	private PauseValidator pause;
	private PlayAction playAction;
//	private ErrorDescriptionValidator error;
	
	public PlaybackDeviceRegistrationTests() throws OoyalaException {
		super();
	}

	@Test(groups = "syndicationRules", dataProvider = "testUrls")
	public void testDeviceRegistration(String testName, UrlObject url) {
		boolean result = true;
		try {

			result = result && syndicationRuleValidator.deleteDevices(url.getPCode());
			
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();
			
			tech.getConsoleLogs();

			result = result && play.validate("playing_1", 60000);
			
			Thread.sleep(5000);

			result = result && syndicationRuleValidator.isDeviceRegistered(url.getPCode());

			if(!url.getVideoPlugins().contains("OSMF"))
				result = result && drm.opt().validate("drm_tag", 5000);

			result = result && pause.validate("paused_1", 60000);

			result = result && playAction.startAction();

			result = result && seek.validate("seeked_1", 60000);

			if (eventValidator.isVideoPluginPresent("bit_wrapper")) {
				result = result && streams.setStreamType("mpd").validate("videoPlayingurl", 1000);
				result = result && tech.setStream("dash").validate("bitmovin_technology", 6000);
			}

			if (eventValidator.isVideoPluginPresent("osmf")) {
				result = result && streams.setStreamType("f4m").validate("videoPlayingurl", 1000);
			}

			result = result && eventValidator.validate("played_1", 60000);

			result = result && syndicationRuleValidator.deleteDevices(url.getPCode());
			
			//TODO

			/*result = result && syndicationRuleValidator.updateUserDeviceLimit(url.getPCode(), 1);
			
			driver.get(url.getUrl());
			
			result = result && play.waitForPage();
			
			driver.navigate().refresh();
			
			result = result && play.waitForPage();
			
			result = result && playAction.startAction();
			
			result = result && error.expectedErrorCode("drm_server_error").expectedErrorDesc("DRM server error").validate("", 6000);*/

		} catch (Exception e) {
			logger.error("Error while checking device registration" + e);
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Device Registration Test failed");

	}

	/*private String getNewBrowser() { 
		switch (browser) {
		case "chrome":
			return "firefox";
		case "firefox":
			if (getPlatform().toLowerCase().contains("windows"))
				return "internet explorer";
			else
				return "chrome";
		case "internet explorer":
			return "chrome";
		}
		return "chrome";
	}*/
}
