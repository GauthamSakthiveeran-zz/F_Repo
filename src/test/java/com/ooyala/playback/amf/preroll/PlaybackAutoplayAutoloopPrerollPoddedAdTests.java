package com.ooyala.playback.amf.preroll;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jitendra on 15/03/17.
 */
public class PlaybackAutoplayAutoloopPrerollPoddedAdTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopPrerollPoddedAdTests.class);
	private EventValidator eventValidator;
	private SeekValidator seekValidator;
    private SeekAction seek;

	public PlaybackAutoplayAutoloopPrerollPoddedAdTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

			injectScript();

			boolean autoplay = false;

			autoplay = Boolean.parseBoolean(driver.executeScript(
					"return pp.parameters.autoPlay").toString());

			if(!autoplay){
				logger.error("Autoplay not set for this video");
				result = false;
			}

			result = result && eventValidator.validate("countPoddedAds_1",60000);
			int noOfAds = Integer.parseInt(driver.executeScript("return countPoddedAds_1.textContent").toString());
			for (int i=1 ; i<=noOfAds;i++){
				result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
				result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
			}
			result = result && seekValidator.validate("seeked_1", 60000);
			result = result && eventValidator.validate("replay_1", 60000);

			result = result && eventValidator.validate("countPoddedAds_2",60000);
			int noOfAdsOnReplay = Integer.parseInt(driver.executeScript("return countPoddedAds_2.textContent").toString());
			for (int i=noOfAds+1; i<=noOfAdsOnReplay;i++){
				result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
				result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
				result = result && eventValidator.validate("willPlayAdOnReplay_"+(i-noOfAds)+"", 45000);
			}

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed for "+testName+"", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed for "+testName+"");
	}
}
