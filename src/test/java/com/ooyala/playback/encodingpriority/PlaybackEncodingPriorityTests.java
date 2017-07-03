package com.ooyala.playback.encodingpriority;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaybackEncodingPriorityTests extends PlaybackWebTest {

	public PlaybackEncodingPriorityTests() throws OoyalaException {
		super();
	}

	private EncodingValidator encode;
	private PlayValidator playValidator;
	private SeekValidator seek;
	private EventValidator event;
	private PlayAction playAction;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyEncodingPriority(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.loadingSpinner();

			result = result && encode.getStreamType(url).verifyEncodingPriority(url);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
