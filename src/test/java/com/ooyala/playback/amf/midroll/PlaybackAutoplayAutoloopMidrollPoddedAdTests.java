package com.ooyala.playback.amf.midroll;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 15/03/17.
 */
public class PlaybackAutoplayAutoloopMidrollPoddedAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopMidrollPoddedAdTests.class);
	private EventValidator eventValidator;
    private SeekAction seek;

	public PlaybackAutoplayAutoloopMidrollPoddedAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = { "amf", "autoplay", "midroll" }, dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);
			
			try {
				injectScript();
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.info("Retrying...");
                Thread.sleep(5000);
                injectScript();
            }
			
			

			boolean autoplay = false;

			autoplay = Boolean.parseBoolean(driver.executeScript(
					"return pp.parameters.autoPlay").toString());

			if(!autoplay){
				extentTest.log(LogStatus.FAIL,"Autoplay not set for this video");
				result = false;
			}

			result = result && eventValidator.validate("playing_1", 60000);
			
			result = result && seek.seek(15,true);
			
			result = result && eventValidator.validate("MidRoll_willPlayAds", 60000);

			result = result && eventValidator.validate("adsPlayed_1", 200000);
			
			result = result && eventValidator.validate("countPoddedAds_1",60000);
			
			int noOfAds = Integer.parseInt(driver.executeScript("return countPoddedAds_1.textContent").toString());
			
			for (int i=1 ; i<=noOfAds;i++){
				result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
				result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
			}
			
			result = result && seek.seek(15,true);
			
			result = result && eventValidator.validate("replay_1", 60000);
			
			result = result && seek.seek(15,true);
			
			result = result && eventValidator.validate("countPoddedAds_2",60000);
			
			int noOfAdsOnReplay = Integer.parseInt(driver.executeScript("return countPoddedAds_2.textContent").toString());
			
			for (int i=noOfAds+1; i<=noOfAdsOnReplay;i++){
				result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
				result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
			}

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed for "+testName+"", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed for "+testName+"");
	}
}
