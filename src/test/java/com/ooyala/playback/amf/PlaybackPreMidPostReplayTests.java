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
import com.ooyala.playback.url.Url;
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

	@Test(groups = "amf", dataProvider = "testUrlData")
	public void verifyPreMidPostcontrols(String testName, Url urlData, String url)
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

			playValidator.validate("playing_1", 120);
	        event.validate("PreRoll_willPlayAds", 120);

	        event.validate("MidRoll_willPlayAds", 120);

	        event.validate("PostRoll_willPlayAds", 120);

	        seekValidator.validate("seeked_1",120);
	        event.validate("played_1", 190);

	        event.validate("endScreen", 120);
	        replayValidator.validate("replay_1", 120);
	        
	        extentTest.log(PASS, "Verified Pre_Mid_Post Basic control Replay");

	        event.validate("PreRoll_willPlayAds_OnReplay", 120);
	        
	        if(!getPlatform().equalsIgnoreCase("Android")) {
	        	adClickThrough.clickThroughAds(urlData);
	        }

	        event.validate("singleAdPlayed_4", 120);

	        extentTest.log(PASS, "Verified Preroll ad is playing on replay");

	        event.validate("MidRoll_willPlayAds_OnReplay", 120);

	        if(!getPlatform().equalsIgnoreCase("Android")) {
	        	adClickThrough.clickThroughAds(urlData);
	        }

	        event.validate("singleAdPlayed_5", 120);

	        extentTest.log(PASS, "Verified Midroll ad is playing on replay");

	        event.validate("PostRoll_willPlayAds_OnReplay", 120);

	        if(!getPlatform().equalsIgnoreCase("Android")) {
	        	adClickThrough.clickThroughAds(urlData);
	        }

	        event.validate("singleAdPlayed_6", 120);

	        extentTest.log(PASS, "Verified Postroll ad is playing on replay");

	        extentTest.log(PASS, "Verified Pre_Mid_Post Basic control test");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
