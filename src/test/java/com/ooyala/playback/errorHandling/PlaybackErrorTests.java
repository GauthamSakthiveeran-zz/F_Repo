package com.ooyala.playback.errorHandling;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.*;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaybackErrorTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackErrorTests.class);

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