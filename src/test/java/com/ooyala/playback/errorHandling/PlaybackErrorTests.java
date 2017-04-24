package com.ooyala.playback.errorHandling;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.ErrorDescriptionValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackErrorTests extends PlaybackWebTest {

	private PlayValidator play;
	private ErrorDescriptionValidator errorDescriptionValidator;

	public PlaybackErrorTests() throws OoyalaException {
		super();
	}

	@Test(groups = "error", dataProvider = "testUrls")
	public void testPlaybackError(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			errorDescriptionValidator.setErrorCode(url.getErrorCode());

			result = result && errorDescriptionValidator.expectedErrorCode(
					url.getErrorCode()).expectedErrorDesc(url.getErrorDescription())
					.validate("",20000);


		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Error tests failed : " + testName);
	}
}