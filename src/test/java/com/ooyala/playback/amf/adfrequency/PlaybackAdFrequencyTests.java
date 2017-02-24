package com.ooyala.playback.amf.adfrequency;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdFrequencyValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackAdFrequencyTests extends PlaybackWebTest {

	public PlaybackAdFrequencyTests() throws OoyalaException {
		super();
	}

	private PlayValidator playValidator;
	private AdFrequencyValidator adFrequencyValidator;

	@Test(groups = { "amf", "adFrequency" }, dataProvider = "testUrls")
	public void verifyAdFrequency(String testName, String url) throws OoyalaException {
		boolean result = true;

		try {

			url = adFrequencyValidator.split(url).getUrl();

			driver.get(url);
			
			result = result && playValidator.clearCache();

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && adFrequencyValidator.validate("", 1000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Ad frequency tests failed.");
	}

}
