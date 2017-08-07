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

public class PlaybackPreMidPostRollAdsPoddedTests extends PlaybackWebTest {

	public PlaybackPreMidPostRollAdsPoddedTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private PoddedAdValidator poddedAdValidator;
	private SeekAction seekAction;
//	private SetEmbedCodeValidator setEmbedCodeValidator;
	private MidrollAdValidator adStartTimeValidator;
	private AdClickThroughValidator clickthrough;

	@Test(groups = {"amf","preroll","midroll","postroll","podded"}, dataProvider = "testUrls")
	public void verifyPreMidPostrollPodded(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		boolean click = testName.contains("Clickthrough");

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 60000);

			if (result && click){
				s_assert.assertTrue(clickthrough.validateClickThroughForPoddedAds("preroll"),"Clickthrough");
			}

			result = result && event.validate("adsPlayed_1", 600000);

			result = result && poddedAdValidator.setPosition("PreRoll").validate("countPoddedAds_1", 60000);

			result = result && event.validate("playing_1", 90000);

            if (adStartTimeValidator.isAdPlayTimePresent(url)){
            	if (result && !click) {
					result = result && adStartTimeValidator.setTime(url.getAdStartTime()).validateAdStartTime("MidRoll_willPlayAds");
				} else {
					result = result && event.validate("MidRoll_willPlayAds", 200000);
					if (result && click){
						s_assert.assertTrue(clickthrough.validateClickThroughForPoddedAds("midroll"),"Clickthrough");
					}
				}
            }else {
                result = result && event.validate("MidRoll_willPlayAds", 200000);
                if (result && click){
                    s_assert.assertTrue(clickthrough.validateClickThroughForPoddedAds("midroll"),"Clickthrough");
                }
            }
			result = result && event.validate("adsPlayed_2", 600000);

			result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 600000);
			
    		result = result && seekAction.seekTillEnd().startAction();
			
			result = result && event.validate("PostRoll_willPlayAds", 200000);

            if (result && click){
                s_assert.assertTrue(clickthrough.validateClickThroughForPoddedAds("postroll"),"Clickthrough");
            }

			result = result && event.validate("adsPlayed_3", 600000);

			result = result && poddedAdValidator.setPosition("PostRoll").validate("countPoddedAds_3", 600000);

			result = result && event.validate("played_1", 180000);

			/*if(testName.contains("SetEmbedCode")){
				result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000);
			}*/

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}

		s_assert.assertTrue(result, "Test failed");
		s_assert.assertAll();
	}
}
