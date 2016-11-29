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
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPreMidPostRollAdsTests extends PlaybackWebTest{

	public PlaybackPreMidPostRollAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPreMidPostroll(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);


            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playAction.startAction();

	        loadingSpinner();
            result = result &&  event.validate("PreRoll_willPlayAds", 150000);

            result = result &&  event.validate("adsPlayed_1", 2000000);

	        extentTest.log(PASS, "Played Preroll Ads");
	        event.validate("playing_1", 150000);

	        //TestUtilities.seekPlayback(webDriver);
	        sleep(5000);
	        loadingSpinner();

	        seekAction.seekSpecific(15);

	        loadingSpinner();
            result = result &&  event.validate( "MidRoll_willPlayAds", 150000);
	        event.validate( "adsPlayed_2", 150000);
	        loadingSpinner();

	        extentTest.log(PASS, "Played Midroll Ads");
	        seekAction.seekSpecific(15);

	        loadingSpinner();

            result = result &&  event.validate( "PostRoll_willPlayAds", 150000);
	        event.validateForSpecificPlugins("singleAdPlayed_6", 200000, "pulse");
	        
	        event.validate("adsPlayed_3", 150000); // TODO needs to be ignored for specific plugins
	        
	        /*if(map!=null && map.get("ad_plugin")!=null && map.get("ad_plugin").contains("pulse")) {
	        	event.validate("singleAdPlayed_6", 200);
	        }else {
	        	event.validate("adsPlayed_3", 150);
	        }*/

	        extentTest.log(PASS, "Played Postroll Ads");
	        
	        event.validateForSpecificPlugins("seeked_1", 150000, "pulse");

            result = result &&   event.validate("played_1", 200000);
	        extentTest.log(PASS, "Verified PreMidPostRoll Ads Test");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}
