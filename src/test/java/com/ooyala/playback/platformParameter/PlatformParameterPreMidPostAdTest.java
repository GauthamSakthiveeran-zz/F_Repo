package com.ooyala.playback.platformParameter;

import com.ooyala.playback.page.*;

import static java.net.URLDecoder.decode;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlatformParameterPreMidPostAdTest extends PlaybackWebTest {

	public PlatformParameterPreMidPostAdTest() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator play;
	private PauseValidator pause;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private StreamValidator streamTypeValidator;
	private BitmovinTechnologyValidator bitmovinvalidator;
	private ChromeComponentValidator chromeValidator;

	@Test(groups = { "premidpostroll" }, dataProvider = "testUrls")
	public void verifyPreroll(String testName, UrlObject url) throws Exception {
		boolean result = true;

		try {

			if (testName.contains("Flash") && getBrowser().contains("chrome")) {

				driver.get("chrome://components/");

				chromeValidator.isFlashPluginUpdated();

			}

			driver.get(url.getUrl());
			result = result && playValidator.waitForPage();
			injectScript();
			bitmovinvalidator.getConsoleLogs();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 60000);
			result = result && event.validate("singleAdPlayed_1", 60000);

			result = result && event.loadingSpinner();

			result = result && event.validate("playing_1", 35000);

			result = result && event.playVideoForSometime(3);

			result = result && pause.validate("paused", 60000);

			result = result && bitmovinvalidator.setStream(url.getStreamType()).validate("bitmovin_technology", 6000);

			if (url.getStreamType() != null && !url.getStreamType().isEmpty()) {
				result = result && event.validate("videoPlayingurl", 40000);
				result = result
						&& streamTypeValidator.setStreamType(url.getStreamType()).validate("videoPlayingurl", 1000);
			}

			result = result && play.validate("playing_2", 60000);

			result = result && event.validate("MidRoll_willPlaySingleAd_2", 60000);
			result = result && event.validate("singleAdPlayed_2", 60000);

			result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("seeked_1", 120000);

			result = result && event.validate("played_1", 200000);

			result = result && event.validate("PostRoll_willPlaySingleAd_3", 60000);
			result = result && event.validate("singleAdPlayed_3", 60000);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Platform Tests passed" + testName);

	}
}
