package com.ooyala.playback.amf;

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
			
			event.validate("MidRoll_willPlaySingleAd_1", 180);
            extentTest.log(LogStatus.PASS, "Midroll Ads started");
            event.validate("singleAdPlayed_1", 160);
            extentTest.log(LogStatus.PASS, "Midroll Ads played");
            event.validate("showNonlinearAd_1", 90);
            sleep(2000);
            overLayValidator.validate("nonlinearAdPlayed_1", 160);
            
            extentTest.log(PASS, "Overlay Ads Played");
            
            discoverValidator.validate("reportDiscoveryClick_1", 60);
            extentTest.log(LogStatus.PASS, "Verified MidrollOverlayUpNext Tests");
			
			result = true;
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}

}
