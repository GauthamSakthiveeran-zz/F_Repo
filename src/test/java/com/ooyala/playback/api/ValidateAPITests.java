package com.ooyala.playback.api;

import org.testng.annotations.Test;

import com.ooyala.playback.APIValidationsTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlayerAPIValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 29/12/16.
 */
public class ValidateAPITests extends APIValidationsTest {

	private PlayerAPIValidator ooyalaAPIValidator;
	private PlayValidator playValidator;

	public ValidateAPITests() throws OoyalaException {
		super();
	}

	@Test(groups = "api", dataProvider = "testUrls")
	public void testOoyalaAPI(String testName, UrlObject url) throws OoyalaException {
		boolean result = true;

		try {
			driver.get(url.getUrl());
			result = result && playValidator.waitForPage();
			injectScript();
			validateAPI(testName, result, ooyalaAPIValidator, playValidator);

		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		s_assert.assertTrue(result, "API");
		s_assert.assertAll();

	}
}
