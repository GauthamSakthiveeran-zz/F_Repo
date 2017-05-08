package com.ooyala.playback.amf;

import static java.lang.Double.parseDouble;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPlayerWithoutSkinTests extends PlaybackWebTest {

	public PlaybackPlayerWithoutSkinTests() throws OoyalaException {
		super();
	}

	private EventValidator event;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPlayerWithoutskin(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			String urlWithoutSkin = removeSkin(url.getUrl());
			driver.get(urlWithoutSkin);

			result = result && event.isPageLoaded();
			
			injectScript();

			Boolean autoplay = (Boolean) executeScript(
					"function test() {var bool = pp.parameters.autoplay; return bool;} return test();");

			result = result && !autoplay;

			executeScript("pp.play();");

			result = result && event.validate("PreRoll_willPlaySingleAd_1", 60000);

			result = result && event.validate("adIsPlaying", 60000);

			executeScript("pp.skipAd()");

			result = result && event.validate("singleAdPlayed_1", 60000);

			result = result && event.validate("InitialTime_0", 60000);

			result = result && event.validate("videoPlaying_1", 60000);

			executeScript("pp.pause()");

			result = result && event.validate("paused", 60000);

			executeScript("pp.setVolume(0.5)");

			double getvol = parseDouble(executeScript("return pp.getVolume()").toString());

			result = result && (getvol == 0.5);

			executeScript("pp.seek(20)");

			result = result && event.validate("seeked_1", 60000);

			executeScript("pp.seek(10)");

			result = result && event.validate("seeked_2", 60000);

			executeScript("pp.play()");

			executeScript("pp.seek(pp.getDuration()-7);");

			result = result && event.validate("videoPlaying_2", 190000);

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e);
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");
	}

}
