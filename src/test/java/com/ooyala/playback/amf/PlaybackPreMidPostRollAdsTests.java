package com.ooyala.playback.amf;

import com.ooyala.playback.page.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
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
    private AdClickThroughValidator adClickThroughValidator;

	@Test(groups = {"amf","preroll","midroll","postroll"}, dataProvider = "testUrls")
	public void verifyPreMidPostroll(String testName, UrlObject url) throws OoyalaException {

		boolean click = testName.contains("Clickthrough");

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();
			
			boolean isPulse = event.isAdPluginPresent("pulse");

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 150000);

			if (result && click){
				driver.executeScript("pp.pause()");
				s_assert.assertTrue(adClickThroughValidator.validate("videoPausedAds_2",20000),"Clickthrough");
			}
			
			executeScript("pp.skipAd()");

			result = result && event.validate("adsPlayed_1", 200000);

			result = result && event.validate("playing_1", 150000);

            if (adStartTimeValidator.isAdPlayTimePresent(url)){
                if (result && !click) {
                    result = result && adStartTimeValidator.setTime(url.getAdStartTime()).validateAdStartTime("MidRoll_willPlayAds");
                }else {
                    result = result && event.validate("MidRoll_willPlayAds", 200000);
                    if (result && click){
                        driver.executeScript("pp.pause()");
                        s_assert.assertTrue(adClickThroughValidator.validate("videoPausedAds_4",20000),"Clickthrough");
                    }
                }
            }else{
            	result = result && event.validate("MidRoll_willPlayAds", 200000);
            	if (result && click){
					driver.executeScript("pp.pause()");
					s_assert.assertTrue(adClickThroughValidator.validate("videoPausedAds_4",20000),"Clickthrough");
				}
            }
			
            executeScript("pp.skipAd()");
			
			result = result && event.validate("adsPlayed_2", 150000);

    		result = result && seekAction.seekTillEnd().startAction();

			result = result && event.validate("PostRoll_willPlayAds", 900000);
			if (result && click){
				driver.executeScript("pp.pause()");
				s_assert.assertTrue(adClickThroughValidator.validate("videoPausedAds_6",20000),"Clickthrough");
			}
			
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
			logger.error(e.getMessage());
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}
		s_assert.assertTrue(result, "Pre Mid Post Roll Ads failed.");
		s_assert.assertAll();
	}
}
