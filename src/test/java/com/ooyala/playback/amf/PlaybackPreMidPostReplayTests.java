package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.IsAdPlayingValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPreMidPostReplayTests extends PlaybackWebTest {

	public PlaybackPreMidPostReplayTests() throws OoyalaException {
		super();
	}

	private EventValidator eventValidator;
	private PlayValidator playValidator;
	private ReplayValidator replayValidator;
	private IsAdPlayingValidator isAdPlaying;
	private PoddedAdValidator poddedAdValidator;
	private SeekAction seekAction;
	private PlayAction playAction;
	private SeekValidator seek;

	@Test(groups = { "amf", "preroll", "midroll", "postroll", "replay" }, dataProvider = "testUrls")
	public void verifyPreMidPostcontrols(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		
		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();
			injectScript();
			
			result = result && playAction.startAction();


			result = result && eventValidator.validate("adsPlayed_1", 60000);
			result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 20000);
			
			result = result && eventValidator.validate("playing_1", 120000);
			
			result = result && seekAction.seek(10,true);

			result = result && eventValidator.validate("adsPlayed_2", 60000);
			result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 20000);

			result = result && eventValidator.validate("seeked_1", 30000);

			result = result && seekAction.seek("10");

			result = result && eventValidator.validate("seeked_2", 30000);

			if(isAdPlaying.validate("",1000)){
				result = false;
				extentTest.log(LogStatus.FAIL, "Ad is played after doing backward seek.");
			}

			result = result && seek.validate("seeked_3", 30000);

			result = result && eventValidator.skipScrubberValidation().validate("played_1", 60000);

			result = result && eventValidator.validate("adsPlayed_3", 60000);

			result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds_3", 20000);

			result = result && replayValidator.validate("replay_1", 30000);

			result = result && eventValidator.validate("PreRoll_willPlayAds_OnReplay", 25000);

			result = result && eventValidator.validate("singleAdPlayed_4", 25000);

			result = result && eventValidator.validate("adsPlayed_4", 60000);

			result = result && seek.validate("seeked_4", 30000);

			result = result && eventValidator.validate("MidRoll_willPlayAds_OnReplay", 25000);

			result = result && eventValidator.validate("singleAdPlayed_5", 25000);

			result = result && eventValidator.validate("PostRoll_willPlayAds_OnReplay", 25000);

			result = result && eventValidator.validate("singleAdPlayed_6", 25000);

			result = result && eventValidator.validate("adsPlayed_6", 60000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		Assert.assertTrue(result, "Test failed");

	}

}
