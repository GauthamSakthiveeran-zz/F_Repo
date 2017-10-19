package com.ooyala.playback.amf.adfrequency;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdFrequencyValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackAdFrequencyTests extends PlaybackWebTest {

	private PlayValidator playValidator;
	private AdFrequencyValidator adFrequencyValidator;

	public PlaybackAdFrequencyTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "amf", "adFrequency" }, dataProvider = "testUrls")
	public void verifyAdFrequency(String testDescription, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url.getUrl());
			result = result && playValidator.clearCache();

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && adFrequencyValidator.split(url).validate("", 1000);

			result = result && adFrequencyValidator.validateAdCapFrequency(testDescription, url.getAdFirstPlay(),
					url.getAdFrequency(), 2000);

		} catch (Exception e) {
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}
		Assert.assertTrue(result, "Ad frequency tests failed.");
	}
}