package com.ooyala.playback.amf.midroll;

import com.ooyala.playback.page.*;
import com.ooyala.playback.url.UrlObject;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private SetEmbedCodeValidator setEmbedCodeValidator;
    private AdStartTimeValidator adStartTimeValidator;

	@Test(groups = {"amf","midroll"}, dataProvider = "testUrls")
	public void verifyMidRoll(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

			result = result && event.validate("videoPlaying_1", 90000);

			if (event.isVideoPluginPresent("akamai")) {
				
				if (event.isAdPluginPresent("freewheel")){
                    if (adStartTimeValidator.isAdPlayTimePresent(url)){
                        result = result && adStartTimeValidator.validateAdStartTime("MidRoll_willPlaySingleAd_2");
                    }else
                        result = result && event.validate("MidRoll_willPlayAds_2", 120000);

					result = result && event.validate("adsPlayed_2", 60000);
				} else{
                    if (adStartTimeValidator.isAdPlayTimePresent(url)){
                        result = result && adStartTimeValidator.validateAdStartTime("MidRoll_willPlaySingleAd_1");
                    }else
                        result = result && event.validate("MidRoll_willPlayAds_1", 120000);
					result = result && event.validate("adsPlayed_1", 60000);
				}

				
			} else {
                if (adStartTimeValidator.isAdPlayTimePresent(url)){
                    result = result && adStartTimeValidator.validateAdStartTime("MidRoll_willPlaySingleAd_1");
                }else
                    result = result && event.validate("MidRoll_willPlayAds_1", 120000);
				if (event.isAdPluginPresent("pulse"))
					result = result && event.validate("singleAdPlayed_2", 60000);
				else
					result = result && event.validate("singleAdPlayed_1", 60000);
			}
			
			result = result && event.validate("playing_2", 60000);

			if(testName.contains("SetEmbedCode")){
//				result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000); TODO: Resolve NPE
			}else{
				result = result && seekValidator.validate("seeked_1", 160000);
				result = result && event.validate("played_1", 160000);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}
}
