package com.ooyala.playback.amf.midroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.OverlayValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidrollOverlayTests extends PlaybackWebTest {

	public PlaybackMidrollOverlayTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private OverlayValidator overLayValidator;
	private AdClickThroughValidator adClicks;

	@Test(groups = {"amf","overlay","midroll"}, dataProvider = "testUrls")
	public void verifyMidrollOverlay(String testName, String url)
			throws OoyalaException {

		boolean result = true;
		
		try {
			
			driver.get(url);
			
			// IMA Overlay is not showing
			// https://jira.corp.ooyala.com/browse/PBI-1825
			if (event.isAdPluginPresent("ima")) {
				extentTest.log(LogStatus.SKIP, "IMA Overlay is not showing as PBI-1825");
				return;
			}

			result = result && playValidator.waitForPage();

			injectScript();

            result = result && playValidator.validate("playing_1", 60000);
            
            if(!event.isVideoPluginPresent("osmf")){
            	result = result && event.validate("MidRoll_willPlaySingleAd_1", 160000);
				result = result && event.validate("singleAdPlayed_1", 160000);
            }
			
            result = result && event.validate("showNonlinearAd_1", 160000);
            
            result = result && adClicks.overlay().validate("", 120000);

			result = result
					&& overLayValidator.validate("nonlinearAdPlayed_1", 160000);

			// TODO , seeked_1 is not showing up in IE 11
			if (!(getBrowser().equalsIgnoreCase("internet explorer") && event.isVideoPluginPresent("osmf")
					&& event.isAdPluginPresent("vast"))) {
				
				result = result && seekValidator.validate("seeked_1", 160000);

				result = result && event.validate("videoPlayed_1", 160000);
				result = result && event.validate("played_1", 160000);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");
	}

}
