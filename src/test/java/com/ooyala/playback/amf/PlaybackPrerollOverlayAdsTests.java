package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPrerollOverlayAdsTests extends PlaybackWebTest{

	public PlaybackPrerollOverlayAdsTests() throws OoyalaException {
		super();
	}
	
	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private OverlayValidator overLayValidator;
	private SeekValidator seekValidator;
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPrerollOverlay(String testName, String url)
			throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url);

            result = result && playValidator.waitForPage();
			Thread.sleep(2000);

			injectScript();

            result = result && playAction.startAction();
	        loadingSpinner();

	        // added condition for IMA OVerlay as overlay is showing intermittently PBI-1825
//	        if(!Description.contains("IMA")) {

            result = result && overLayValidator.validate("nonlinearAdPlayed_1", 160000);
	        
//	        }
            result = result &&  event.validate("videoPlaying_1", 90000);
	        
	        seekValidator.validate("seeked_1", 120000);

	        // add a condition when the ad plays till end of the video
//	        if (parseDouble(((JavascriptExecutor) webDriver).executeScript("return pp.getPlayheadTime();").toString()) <
//	                (parseDouble(((JavascriptExecutor) webDriver).executeScript("return pp.getDuration();").toString()) - 5)) {
//	            seekPlayback(webDriver);
//	            waitForElement(webDriver, "seeked_1", 190);
//	        }
            result = result &&  event.validate("played_1", 190000);
	        extentTest.log(PASS, "Main video played");
	        extentTest.log(PASS, "Verified PrerollOverlayAds test");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Verified PreRoll Ads test");

	}

}