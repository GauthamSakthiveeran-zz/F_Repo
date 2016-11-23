package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.Url;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekAction seekAction;

	@Test(groups = "amf", dataProvider = "testUrlData")
	public void verifyMidRoll(String testName, Url urlData, String url)
			throws OoyalaException {

		boolean result = false;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

			playValidator.validate("playing_1", 60);
			extentTest.log(PASS, "Video started playing");
			Thread.sleep(5000);

			seekAction.seekSpecific(urlData, 15);

			loadingSpinner();
			event.validate("videoPlaying_1", 90);
			event.validate("MidRoll_willPlaySingleAd_1", 120);
			extentTest.log(PASS, "Midroll Ad started to play");
			event.validate("singleAdPlayed_1", 160);
			extentTest.log(PASS, "Midroll Ad ended");

			if (urlData.getAdPlugins().getName().equals("PULSE")) {
				event.validate("singleAdPlayed_2", 160);
				Thread.sleep(2000);
			}

			seekAction.seekSpecific(urlData, 10);

			event.validate("videoPlayed_1", 160);
			event.validate("played_1", 160);
			extentTest.log(PASS, "Video Played");
			extentTest.log(PASS, "Verified MidrollAdsTest");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}
}
