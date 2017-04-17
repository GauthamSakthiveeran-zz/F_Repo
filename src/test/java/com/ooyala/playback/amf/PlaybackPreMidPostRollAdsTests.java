package com.ooyala.playback.amf;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdStartTimeValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SetEmbedCodeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaybackPreMidPostRollAdsTests extends PlaybackWebTest {

	public PlaybackPreMidPostRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private SetEmbedCodeValidator setEmbedCodeValidator;
    private AdStartTimeValidator adStartTimeValidator;

	@Test(groups = {"amf","preroll","midroll","postroll"}, dataProvider = "testUrls")
	public void verifyPreMidPostroll(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();
			
			boolean isPulse = event.isAdPluginPresent("pulse");

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 150000);
			
			if (!isPulse) 
				executeScript("pp.skipAd()");

			result = result && event.validate("adsPlayed_1", 200000);

			result = result && event.validate("playing_1", 150000);

			if (!isPulse) 
				result = result && seekAction.setTime(15).fromLast().startAction(); // TODO Pulse skips the ad if video seeked ahead of the ad playing time

            if (adStartTimeValidator.isAdPlayTimePresent(url)){
                result = result && adStartTimeValidator.validateAdStartTime("MidRoll_willPlayAds");
            }else
                result = result && event.validate("MidRoll_willPlayAds", 150000);
			
			if (!isPulse) 
				executeScript("pp.skipAd()");
			
			result = result && event.validate("adsPlayed_2", 150000);

			result = result && seekAction.setTime(30).fromLast().startAction();

			result = result && event.validate("PostRoll_willPlayAds", 200000);
			
			if (!isPulse) 
				executeScript("pp.skipAd()");

			if (isPulse) {
				result = result && event.validate("singleAdPlayed_6", 60000);
				result = result && event.validate("seeked_1", 60000);
			} else
				result = result && event.validate("adsPlayed_3", 60000);

			result = result && event.validate("played_1", 200000);

			if(testName.contains("SetEmbedCode")){
				result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Pre Mid Post Roll Ads failed.");

	}

}
