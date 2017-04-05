package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackEncodingPriorityTests extends PlaybackWebTest {

	public PlaybackEncodingPriorityTests() throws OoyalaException {
		super();
	}

	private EncodingValidator encode;
	private PlayValidator playValidator;
	private SeekValidator seek;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyEncodingPriority(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);
			
			result = result && encode.validate("", 6000);
			
			result = result && seek.validate("seeked_1", 19000);
			
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
