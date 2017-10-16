package com.ooyala.playback.api;

import com.ooyala.playback.APIValidationsTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlayerAPIValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 16/10/17.
 */
public class ValidateAPIForEmptyEmbedCodeTests extends APIValidationsTest {

	private PlayerAPIValidator ooyalaAPIValidator;
	private PlayValidator playValidator;

	public ValidateAPIForEmptyEmbedCodeTests() throws OoyalaException {
		super();
	}

	@Test(groups = "api", dataProvider = "testUrls")
	public void testOoyalaAPI(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;

		try {
			driver.get(url.getUrl());
			result = result && playValidator.waitForPage();
			injectScript();
			result = result && ooyalaAPIValidator.validateSetEmptyEmbedCode();

		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}

		Assert.assertTrue(result);
	}
}
