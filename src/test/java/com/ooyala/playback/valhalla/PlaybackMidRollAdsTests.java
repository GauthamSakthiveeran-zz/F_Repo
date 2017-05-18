package com.ooyala.playback.valhalla;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdPluginValidator;
import com.ooyala.playback.page.MidrollAdValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMidRollAdsTests() throws OoyalaException {
		super();
	}

	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private MidrollAdValidator midrollAdValidator;
	private AdPluginValidator adPluginValidator;

	@Test(groups = { "amf", "midroll" }, dataProvider = "testUrls")
	public void verifyMidRoll(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		String adPlugin = testName.split(" - ")[1].split(" ")[0].toLowerCase();

		try {
			driver.get(url.getUrl());
			
			driver.navigate().refresh();

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

			url.setAdPlugins(adPlugin);

			result = result && midrollAdValidator.validateMidrollAd(url);

			result = result && adPluginValidator.setAdPlugin(adPlugin).validate("admanager", 1000);

			result = result && seekValidator.validate("seeked_1", 160000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Verified");
	}
}
