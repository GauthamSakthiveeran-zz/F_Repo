package com.ooyala.playback.encodingpriority;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackEncodingPriorityTests extends PlaybackWebTest {

	public PlaybackEncodingPriorityTests() throws OoyalaException {
		super();
	}

	private EncodingValidator encode;
	private PlayValidator playValidator;
	private EventValidator event;
	private PlayAction playAction;
	private SeekAction seekAction;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyEncodingPriority(String testName, UrlObject url) throws OoyalaException {
        boolean result = true;
		boolean isDRM = testName.toLowerCase().contains("drm");

		try {
            driver.get(url.getUrl());
            result = result && playValidator.waitForPage();
            injectScript();
            result = result && playAction.startAction();
            result = result && event.loadingSpinner();
            result = result && isDRM? encode.validateDRM() : encode.getStreamType(url).verifyEncodingPriority(url);
            result = result && seekAction.seekTillEnd().startAction();
            result = result && event.validate("played_1", 120000);
            
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
