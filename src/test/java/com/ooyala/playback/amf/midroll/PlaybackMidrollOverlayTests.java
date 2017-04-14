package com.ooyala.playback.amf.midroll;

import com.ooyala.playback.page.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackMidrollOverlayTests extends PlaybackWebTest {

	public PlaybackMidrollOverlayTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private OverlayValidator overLayValidator;
	private AdClickThroughValidator adClicks;
	private AdStartTimeValidator adStartTimeValidator;

	@Test(groups = {"amf","overlay","midroll","sequential"}, dataProvider = "testUrls")
	public void verifyMidrollOverlay(String testName, UrlObject url)
			throws OoyalaException {

		boolean result = true;
		
		try {
			
			driver.get(url.getUrl());
			
			result = result && playValidator.waitForPage();

			injectScript();

            result = result && playValidator.validate("playing_1", 60000);
            
            if(!event.isVideoPluginPresent("osmf")){
				if (adStartTimeValidator.isAdPlayTimePresent(url)){
					result = result && adStartTimeValidator.validateAdStartTime("MidRoll_willPlaySingleAd_1");
				}else
            		result = result && event.validate("MidRoll_willPlaySingleAd_1", 160000);

				result = result && event.validate("singleAdPlayed_1", 160000);
            }
			
            result = result && event.validate("showNonlinearAd_1", 160000);

            if (adStartTimeValidator.isOverlayPlayTimePresent(url)) {
                result = result && adStartTimeValidator.validateNonLinearAdStartTime("showNonlinearAd_1");
            }
            
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
