package com.ooyala.playback.amf.VAST;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidrollOverlayUpNextTests extends PlaybackWebTest {

	public PlaybackMidrollOverlayUpNextTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayValidator playValidator;
	private OverlayValidator overLayValidator;
	private DiscoveryValidator discoverValidator;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMidrollOverlayUpNext(String testName, String url)
			throws OoyalaException {
		
		boolean result = true;
		
		try {
			
			driver.get(url);

			result = result && playValidator.waitForPage();
			
			injectScript();
			
			result = result && playValidator.validate("playing_1", 60000);
			
			result = result && event.validate("MidRoll_willPlaySingleAd_1", 180000);
            extentTest.log(LogStatus.PASS, "Midroll Ads started");
            result = result && event.validate("singleAdPlayed_1", 160000);
            extentTest.log(LogStatus.PASS, "Midroll Ads played");
            result = result && event.validate("showNonlinearAd_1", 90000);
            sleep(2000);
            result = result && overLayValidator.validate("nonlinearAdPlayed_1", 16000);
            
            extentTest.log(PASS, "Overlay Ads Played");
            
            result = result && discoverValidator.validate("reportDiscoveryClick_1", 60000);
            extentTest.log(LogStatus.PASS, "Verified MidrollOverlayUpNext Tests");
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}

}
