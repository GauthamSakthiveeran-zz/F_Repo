package com.ooyala.playback.amf.adfrequency;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdFrequencyValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackAdFrequencyTests extends PlaybackWebTest {

	public static Logger logger = Logger.getLogger(PlaybackAdFrequencyTests.class);
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
			logger.info("Navigated to : "+url.getUrl());

			Assert.assertEquals(playValidator.clearCache(),true,"Failed to clear cache");
			logger.info("Successfully cleared cache");

			Assert.assertEquals(playValidator.waitForPage(),true,"Failed to wait for page");
			logger.info("Successfully wait for page");

			injectScript();
			logger.info("Successfully injected script");

			Assert.assertEquals(adFrequencyValidator.split(url).validate("", 1000),true,"Failed to validate ad Frequency");
			logger.info("Successfully validated ad frequency");

			Assert.assertEquals(adFrequencyValidator.validateAdCapFrequency(testDescription,url.getAdFirstPlay(),url.getAdFrequency(),2000),true,"Failed to validate ad Cap Frequency");
			logger.info("Successfully validated ad Cap frequency");

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Ad frequency tests failed.");
	}
}