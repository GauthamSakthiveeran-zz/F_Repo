package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.MidrollAdValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SetEmbedCodeValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackPreMidPostRollAdsTests extends PlaybackWebTest {

	public PlaybackPreMidPostRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private SetEmbedCodeValidator setEmbedCodeValidator;
    private MidrollAdValidator adStartTimeValidator;

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
			
			executeScript("pp.skipAd()");

			result = result && event.validate("adsPlayed_1", 200000);

			result = result && event.validate("playing_1", 150000);

            if (adStartTimeValidator.isAdPlayTimePresent(url)){
                result = result && adStartTimeValidator.setTime(url.getAdStartTime()).validateAdStartTime("MidRoll_willPlayAds");
            }else{
            	result = result && event.validate("MidRoll_willPlayAds", 200000);
            }
			
            executeScript("pp.skipAd()");
			
			result = result && event.validate("adsPlayed_2", 150000);

    		result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("PostRoll_willPlayAds", 900000);
			
			executeScript("pp.skipAd()");

			if (isPulse) {
				result = result && event.validate("singleAdPlayed_6", 60000);
				result = result && event.validate("seeked_1", 60000);
			} else
				result = result && event.validate("adsPlayed_3", 60000);

			result = result && event.validate("played_1", 200000);

			/*if(testName.contains("SetEmbedCode")){
				result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		Assert.assertTrue(result, "Pre Mid Post Roll Ads failed.");

	}

}
