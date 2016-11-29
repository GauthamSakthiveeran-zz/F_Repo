package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreMidPostReplayTests extends PlaybackWebTest {

	public PlaybackPreMidPostReplayTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private ReplayValidator replayValidator;
	private AdClickThroughValidator adClickThrough;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPreMidPostcontrols(String testName, String url)
			throws OoyalaException {

		boolean result = false;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playValidator.validate("playing_1", 120);
            result = result && event.validate("PreRoll_willPlayAds", 120);

            result = result && event.validate("MidRoll_willPlayAds", 120);

            result = result && event.validate("PostRoll_willPlayAds", 120);

            result = result && seekValidator.validate("seeked_1",120);
            result = result && event.validate("played_1", 190);

            result = result &&  event.validate("endScreen", 120);
            result = result && replayValidator.validate("replay_1", 120);
	        
	        extentTest.log(PASS, "Verified Pre_Mid_Post Basic control Replay");

            result = result && event.validate("PreRoll_willPlayAds_OnReplay", 120);
	        
	        
	        if(!getPlatform().equalsIgnoreCase("Android")) {
	        	adClickThrough.validate("", 120);
	        }

	        event.validate("singleAdPlayed_4", 120);

	        extentTest.log(PASS, "Verified Preroll ad is playing on replay");

	        event.validate("MidRoll_willPlayAds_OnReplay", 120);

	        if(!getPlatform().equalsIgnoreCase("Android")) {
	        	adClickThrough.validate("", 120);
	        }

	        event.validate("singleAdPlayed_5", 120);

	        extentTest.log(PASS, "Verified Midroll ad is playing on replay");

	        event.validate("PostRoll_willPlayAds_OnReplay", 120);

	        if(!getPlatform().equalsIgnoreCase("Android")) {
	        	adClickThrough.validate("", 120);
	        }

	        event.validate("singleAdPlayed_6", 120);

	        extentTest.log(PASS, "Verified Postroll ad is playing on replay");

	        extentTest.log(PASS, "Verified Pre_Mid_Post Basic control test");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
