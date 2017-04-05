package com.ooyala.playback.core;

import com.ooyala.playback.page.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class HTML5FirstPlaybackTests extends PlaybackWebTest {

	public HTML5FirstPlaybackTests() throws OoyalaException {
		super();
	}

	private PlayValidator play;
	private BitmovinTechnologyValidator tech;
	private SeekValidator seek;
	private EventValidator eventValidator;
    private EncodingValidator encodingValidator;

	@Test(groups = "html5", dataProvider = "testUrls")
	public void testHTML5FirstPlayback(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
        String [] parameters = {"{\"platform\":\"html5\"}","{\"platform\":\"flash\"}"};
		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			injectScript();

			tech.getConsoleLogs();

			result = result && play.validate("playing_1", 60000);

			result = result && tech.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 120000);

            //verifying html5 and flash platform

            for (int i=0 ; i<parameters.length; i++) {

                driver.get(encodingValidator.getNewUrl(parameters[i], browser));

                result = result && play.waitForPage();

                injectScript();

                tech.getConsoleLogs();

                result = result && play.validate("playing_1", 60000);

                result = result && tech.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);

                result = result && seek.validate("seeked_1", 60000);

                result = result && eventValidator.validate("played_1", 120000);
            }

		} catch (Exception e) {
			logger.error("Exception while checking basic playback " + e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "Basic playback tests failed" + testName);

	}
}
