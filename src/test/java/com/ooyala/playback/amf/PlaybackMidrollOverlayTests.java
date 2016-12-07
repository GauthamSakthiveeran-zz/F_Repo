package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidrollOverlayTests extends PlaybackWebTest {

	public PlaybackMidrollOverlayTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private OverlayValidator overLayValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyMidrollOverlay(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

            result = result && playValidator.validate("playing_1", 60000);
			
			/*if (event.isStreamingProtocolPrioritized("hds")) { 
				extentTest.log(LogStatus.INFO,"For Flash Specific cases");
				
				if(event.isVideoPlugin("akamai")){
					result = result && event.validate("MidRoll_willPlayAds_1", 160000);
					result = result && event.validate("adsPlayed_1", 160000);
					
				}else{
					result = result && event.validate("MidRoll_willPlaySingleAd_1", 160000);
					result = result && event.validate("singleAdPlayed_1", 160000);
				}
				
            }*/
            result = result && event.validate("showNonlinearAd_1", 160000);

			result = result
					&& overLayValidator.validate("nonlinearAdPlayed_1", 160000);

			result = result && seekValidator.validate("seeked_1", 160000);

			result = result && event.validate("videoPlayed_1", 160000);
			result = result && event.validate("played_1", 160000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");
	}

}
