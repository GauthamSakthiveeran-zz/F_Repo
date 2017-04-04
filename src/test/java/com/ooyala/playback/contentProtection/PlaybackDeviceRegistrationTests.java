package com.ooyala.playback.contentProtection;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.SyndicationRuleValidator;
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

	public PlaybackDeviceRegistrationTests() throws OoyalaException {
		super();
	}

	@Test(groups = "syndicationRules", dataProvider = "testUrls")
	public void testDeviceRegistration(String testName, UrlObject url) {
		boolean result = true;
		try {

			result = result && syndicationRuleValidator.deleteDevices(url.getPCode());

			result = result && syndicationRuleValidator.deleteEntitlement(url.getEmbedCode(), url.getPCode());

			result = result && syndicationRuleValidator.createEntitlement(url.getEmbedCode(), url.getPCode(), 1);

			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && syndicationRuleValidator.isDeviceRegistered(url.getPCode());

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

			if (!getBrowser().contains("safari")) {
				WebDriver newDriver = getWebdriver(getNewBrowser());

				result = result && syndicationRuleValidator.initializeNewDriverAndValidateError(url.getUrl(), newDriver,
						"drm_server_error", "DRM server error", url.getPCode());

				result = result && syndicationRuleValidator.deleteDevices(url.getPCode());

				newDriver.quit();

				driver.quit();
			}

		} catch (Exception e) {
			logger.error("Error while checking device registration" + e);
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Device Registration Test failed");

	}

	private String getNewBrowser() { // TODO for safari
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
	}
}
