package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.Url;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidRollPoddedAdsTests extends PlaybackWebTest{

	public PlaybackMidRollPoddedAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMidrollPodded(String testName, String url)
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

            result = result && playValidator.validate("playing_1", 60);

            result = result && seekValidator.validate("seeked_1", 60);
			
			event.validate("videoPlayed_1", 200);

            result = result && poddedAdValidator.validate("countPoddedAds", 120);
			
			event.validate("seeked_1", 60);
            result = result && event.validate("played_1", 200);
	        extentTest.log(PASS, "Verified MidrollPodded Ads Tests");
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}

}
