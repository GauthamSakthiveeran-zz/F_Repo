package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.Url;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreMidPostRollAdsTests extends PlaybackWebTest{

	public PlaybackPreMidPostRollAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;

	@Test(groups = "amf", dataProvider = "testUrlData")
	public void verifyPreMidPostroll(String testName, Url urlData, String url)
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
	        event.validate("PreRoll_willPlayAds", 150);

	        event.validate("adsPlayed_1", 200);

	        extentTest.log(PASS, "Played Preroll Ads");
	        event.validate("playing_1", 150);

	        //TestUtilities.seekPlayback(webDriver);
	        sleep(5000);
	        loadingSpinner();

	        seekAction.seekSpecific(urlData,15);

	        loadingSpinner();
	        event.validate( "MidRoll_willPlayAds", 150);
	        event.validate( "adsPlayed_2", 150);
	        loadingSpinner();

	        extentTest.log(PASS, "Played Midroll Ads");
	        seekAction.seekSpecific(urlData,15);

	        loadingSpinner();

	        event.validate( "PostRoll_willPlayAds", 150);
	        if(urlData.getAdPlugins().getName().equals("PULSE")) {
	        	event.validate("singleAdPlayed_6", 200);
	        }else {
	        	event.validate("adsPlayed_3", 150);
	        }

	        extentTest.log(PASS, "Played Postroll Ads");

	        if (!urlData.getAdPlugins().getName().equals("PULSE")){
	        	event.validate("seeked_1", 150);
	        }

	        event.validate("played_1", 200);
	        extentTest.log(PASS, "Verified PreMidPostRoll Ads Test");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
