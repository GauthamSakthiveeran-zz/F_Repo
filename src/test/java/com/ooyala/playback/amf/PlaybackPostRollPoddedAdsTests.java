package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostRollPoddedAdsTests extends PlaybackWebTest{

	public PlaybackPostRollPoddedAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPostrollPodded(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playValidator.validate("playing_1", 150);
            result = result && seekValidator.validate("seeked_1", 180);
            result = result && event.validate("videoPlayed_1", 180);
	        event.validate("played_1", 180);
	        extentTest.log(PASS, "Main video finished playing");

            result = result && poddedAdValidator.validate("countPoddedAds", 160);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
