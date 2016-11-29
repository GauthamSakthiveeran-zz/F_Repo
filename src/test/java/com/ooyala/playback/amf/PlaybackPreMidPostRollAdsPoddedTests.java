package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreMidPostRollAdsPoddedTests extends PlaybackWebTest{

	
	public PlaybackPreMidPostRollAdsPoddedTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private PoddedAdValidator poddedAdValidator;
	private SeekAction seekAction;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPreMidPostrollPodded(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playAction.startAction();
	        loadingSpinner();
            result = result && event.validate("PreRoll_willPlayAds", 60);

            result = result &&  event.validate("adsPlayed_1", 200);

	        extentTest.log(PASS, "Played Preroll Ads ");

	        sleep(1000);

            result = result &&  poddedAdValidator.validate("countPoddedAds_1", 60);


            result = result &&  event.validate("playing_1", 90);

	        loadingSpinner();
	        
	        seekAction.setFactor(2).fromLast().startAction();//seekAction.seek(seekAction.getDuration(2));

	        loadingSpinner();

            result = result &&   event.validate("MidRoll_willPlayAds", 200);
	        event.validate("adsPlayed_2", 200);

	        extentTest.log(PASS, "Played Midroll Ads ");

            result = result &&  poddedAdValidator.validate("countPoddedAds_2", 60);

	        loadingSpinner();
	        seekAction.setTime(10).fromLast().startAction();
	        loadingSpinner();

            result = result &&  event.validate( "PostRoll_willPlayAds", 180);
	        event.validate("adsPlayed_3", 200);
	        extentTest.log(PASS, "Played Postroll Ads ");

            result = result &&  poddedAdValidator.validate("countPoddedAds_3", 60);

            result = result &&  event.validate("played_1", 180);

	        extentTest.log(PASS, "Verified PreMidPostRoll Podded Ads Test ");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}
}
