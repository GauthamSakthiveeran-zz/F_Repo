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

		boolean result = false;

		try {

			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

			playAction.startAction();
	        loadingSpinner();
	        event.validate("PreRoll_willPlayAds", 60);

	        event.validate("adsPlayed_1", 200);

	        extentTest.log(PASS, "Played Preroll Ads ");

	        sleep(1000);

	        poddedAdValidator.validate("countPoddedAds_1", 60);
	        
	        
	        event.validate("playing_1", 90);

	        loadingSpinner();
	        
	        seekAction.seek(seekAction.getDuration(2));

	        loadingSpinner();
	        
	        event.validate("MidRoll_willPlayAds", 200);
	        event.validate("adsPlayed_2", 200);

	        extentTest.log(PASS, "Played Midroll Ads ");
	        
	        poddedAdValidator.validate("countPoddedAds_2", 60);

	        loadingSpinner();
	        seekAction.seek(10, true);
	        loadingSpinner();

	        event.validate( "PostRoll_willPlayAds", 180);
	        event.validate("adsPlayed_3", 200);
	        extentTest.log(PASS, "Played Postroll Ads ");
	        
	        poddedAdValidator.validate("countPoddedAds_3", 60);

	        event.validate("played_1", 180);

	        extentTest.log(PASS, "Verified PreMidPostRoll Podded Ads Test ");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}
}
