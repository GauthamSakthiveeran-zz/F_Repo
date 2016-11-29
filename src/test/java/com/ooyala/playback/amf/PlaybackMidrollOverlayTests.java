package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidrollOverlayTests extends PlaybackWebTest{

	public PlaybackMidrollOverlayTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private OverlayValidator overLayValidator;
	
	@Test(groups = "amf", dataProvider = "testUrlData")
	public void verifyMidrollOverlay(String testName, String url)
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
			
			if (playValidator.isStreamingProtocolPrioritized("hds")) { 
				extentTest.log(LogStatus.INFO,"For Flash Specific cases");
                event.validate("MidRoll_willPlaySingleAd_1", 180);
                event.validate("singleAdPlayed_1", 160);
                extentTest.log(PASS, "Midroll Ad Played");
            }
            result = result && event.validate("showNonlinearAd_1", 160);
            sleep(2000);

            result = result && overLayValidator.validate("nonlinearAdPlayed_1", 160);
            
            extentTest.log(PASS, "Overlay Ads Played");

            result = result && seekValidator.validate("seeked_1",120);

            event.validate("videoPlayed_1", 160);
            result = result &&  event.validate("played_1", 160);
            extentTest.log(PASS, "Verified MidrollOverlay ads");
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}

}
