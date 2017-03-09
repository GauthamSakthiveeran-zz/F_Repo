package com.ooyala.playback.VTC;

import com.ooyala.playback.page.action.SeekAction;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by snehal on 25/11/16.
 */
public class PlaybackAutoplayAutoloopTests extends PlaybackWebTest {

	private static Logger logger = Logger.getLogger(PlaybackAutoplayAutoloopTests.class);
	private EventValidator eventValidator;
	private SeekValidator seekValidator;
    private SeekAction seek;

	public PlaybackAutoplayAutoloopTests() throws OoyalaException {
		super();
	}

	@Test(groups = "Playback", dataProvider = "testUrls")
	public void testAutoplayAutoloop(String testName, String url)
			throws OoyalaException {

		String[] parts= testName.split(":");
		String tcName = parts[1].trim();
		String adType = parts[2].trim();

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

			if (adType.equalsIgnoreCase("Preroll") || adType.equalsIgnoreCase("Midroll")){
				result = result && eventValidator.validate("adsPlayed_1", 45000);
				result = result && eventValidator.validate("playing_1", 60000);
				result = result && seekValidator.validate("seeked_1", 60000);
				result = result && eventValidator.validate("replay_1", 60000);
				result = result && eventValidator.validate("willPlaySingleAd_2", 45000);
				result = result && eventValidator.validate("adsPlayed_2", 45000);
			}

            if (adType.equalsIgnoreCase("Postroll")){
                result = result && eventValidator.validate("playing_1", 60000);
                result = result && seek.seek(10,true);
                result = result && eventValidator.validate("adsPlayed_1", 45000);
                result = result && eventValidator.validate("replay_1", 60000);
                result = result && seek.seek(10,true);
                result = result && eventValidator.validate("willPlayAdOnReplay_1", 45000);
                result = result && eventValidator.validate("adsPlayed_2", 45000);
            }

            if (adType.equalsIgnoreCase("PreMidPost")){
                result = result && eventValidator.validate("adsPlayed_1", 45000);
                result = result && eventValidator.validate("playing_1", 60000);
                result = result && seekValidator.validate("seeked_1", 60000);
                result = result && eventValidator.validate("willPlaySingleAd_2", 45000);
                result = result && eventValidator.validate("adsPlayed_2", 45000);
                result = result && eventValidator.validate("willPlaySingleAd_3", 45000);
                result = result && eventValidator.validate("adsPlayed_3", 45000);
                result = result && eventValidator.validate("replay_1", 60000);
                result = result && eventValidator.validate("willPlayAdOnReplay_1", 45000);
                result = result && eventValidator.validate("adsPlayed_4", 45000);
                result = result && seekValidator.validate("seeked_1", 60000);
                result = result && eventValidator.validate("willPlayAdOnReplay_2", 45000);
                result = result && eventValidator.validate("adsPlayed_5", 45000);
                result = result && eventValidator.validate("willPlayAdOnReplay_3", 45000);
                result = result && eventValidator.validate("adsPlayed_6", 45000);
            }

            if (adType.equalsIgnoreCase("MidrollPodded") || adType.equalsIgnoreCase("PostrollPodded") ||
                    adType.equalsIgnoreCase("PrerollPodded")){

                if (adType.equalsIgnoreCase("MidrollPodded") || adType.equalsIgnoreCase("PostrollPodded")){
                    result = result && eventValidator.validate("playing_1", 60000);
                    result = result && seek.seek(15,true);                          // video seeked beyond Midroll ad
                }
                result = result && eventValidator.validate("countPoddedAds_1",60000);
                int noOfAds = Integer.parseInt(driver.executeScript("return countPoddedAds_1.textContent").toString());
                for (int i=1 ; i<=noOfAds;i++){
                    result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
                    result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
                }
                if (adType.equalsIgnoreCase("PrerollPodded")) {
                    result = result && seekValidator.validate("seeked_1", 60000); // video seeked after playback of Preroll Ad
                }
                result = result && eventValidator.validate("replay_1", 60000);
                if (adType.equalsIgnoreCase("MidrollPodded") || adType.equalsIgnoreCase("PostrollPodded")){
                    result = result && seekValidator.validate("seeked_1", 60000);
                }
                result = result && eventValidator.validate("countPoddedAds_2",60000);
                int noOfAdsOnReplay = Integer.parseInt(driver.executeScript("return countPoddedAds_2.textContent").toString());
                for (int i=noOfAds+1; i<=noOfAdsOnReplay;i++){
                    result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
                    result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
                    result = result && eventValidator.validate("willPlayAdOnReplay_"+(i-noOfAds)+"", 45000);
                }
            }


            if (adType.equalsIgnoreCase("PreMidPostPodded")){
                result = result && eventValidator.validate("adsPlayed_1", 45000);
                result = result && eventValidator.validate("countPoddedAds_1",60000);
                int noOfPrerollAds = Integer.parseInt(driver.executeScript("return countPoddedAds_1.textContent").toString());
                for (int i=1; i<=noOfPrerollAds;i++){
                    result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
                    result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
                }

                if (!eventValidator.isAdPluginPresent("freewheel")) {
                    result = result && seek.seek(15, true);
                }
                result = result && eventValidator.validate("countPoddedAds_2",60000);
                int noOfMidrollAds = Integer.parseInt(driver.executeScript("return countPoddedAds_2.textContent").toString());
                for (int i=noOfPrerollAds+1; i<=noOfMidrollAds;i++){
                    result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
                    result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
                }

                if (eventValidator.isAdPluginPresent("freewheel")) {
                    result = result && seek.seek(15, true);
                }

                result = result && eventValidator.validate("countPoddedAds_3",60000);
             int noOfPostrollAds = Integer.parseInt(driver.executeScript("return countPoddedAds_3.textContent").toString());
                for (int i=noOfMidrollAds+1; i<=noOfPostrollAds;i++){
                    result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
                    result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
                }

                result = result && eventValidator.validate("replay_1", 60000);

                result = result && eventValidator.validate("countPoddedAds_4",60000);
                int noOfPrerollAdsOnReplay = Integer.parseInt(driver.executeScript("return countPoddedAds_4.textContent").toString());
                for (int i=noOfPostrollAds+1; i<=noOfPrerollAdsOnReplay;i++){
                    result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
                    result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
                    result = result && eventValidator.validate("willPlayAdOnReplay_"+(i-noOfPostrollAds)+"", 45000);
                }

                if (!eventValidator.isAdPluginPresent("freewheel")) {
                    result = result && seek.seek(15, true);
                }

                result = result && eventValidator.validate("countPoddedAds_5",60000);
                int noOfMidrollAdsOnReplay = Integer.parseInt(driver.executeScript("return countPoddedAds_5.textContent").toString());
                for (int i=noOfPrerollAdsOnReplay+1; i<=noOfMidrollAdsOnReplay;i++){
                    result = result && eventValidator.validate("willPlaySingleAd_"+i+"", 45000);
                    result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
                    result = result && eventValidator.validate("willPlayAdOnReplay_"+(i-noOfPostrollAds)+"", 45000);
                }

                if (eventValidator.isAdPluginPresent("freewheel")) {
                    result = result && seek.seek(15, true);
                }

                result = result && eventValidator.validate("countPoddedAds_6",60000);
                int noOfPostrollAdsOnReplay = Integer.parseInt(driver.executeScript("return countPoddedAds_6.textContent").toString());
                for (int i=noOfMidrollAdsOnReplay+1; i<=noOfPostrollAdsOnReplay;i++){
                    result = result && eventValidator.validate("singleAdPlayed_"+i+"", 45000);
                    result = result && eventValidator.validate("willPlayAdOnReplay_"+(i-noOfPostrollAds)+"", 45000);
                }
            }

		} catch (Exception e) {
			logger.error(e);
			result = false;
			extentTest.log(LogStatus.FAIL, "Playback Autoplay Autoloop test failed", e);
		}
		Assert.assertTrue(result, "Playback Autoplay Autoloop test failed");
	}
}
