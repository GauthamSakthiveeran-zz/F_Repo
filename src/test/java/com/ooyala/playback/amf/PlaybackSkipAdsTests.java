package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdSkipButtonValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackSkipAdsTests extends  PlaybackWebTest{
	
	
	
	public PlaybackSkipAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private AdSkipButtonValidator skipButtonValidator;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPrerollOverlay(String testName, String url)
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

			playAction.startAction();

	        loadingSpinner();

	        event.validate("willPlaySingleAd_1", 150);

            result = result && skipButtonValidator.validate("", 120);
	        
	        extentTest.log(PASS, "Clicked on Skip button");

            result = result && event.validate("singleAdPlayed_1", 150);
	        event.validate("playing_1", 150);
	        sleep(500);

            result = result &&  seekValidator.validate("seeked_1", 150);

            result = result &&  event.validate( "played_1", 150);
	        extentTest.log(PASS, "Main Video played successfully");
	        extentTest.log(PASS, "Verified SkipAds Test");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
