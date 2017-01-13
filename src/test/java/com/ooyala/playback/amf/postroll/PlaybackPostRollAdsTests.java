package com.ooyala.playback.amf.postroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackPostRollAdsTests extends PlaybackWebTest {

	public PlaybackPostRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private SeekAction seekAction;

	@Test(groups = {"amf","postroll"}, dataProvider = "testUrls")
	public void verifyPostroll(String testName, String url)  {

		boolean result = true;

		try {

			driver.get(url);

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 90000);
			if (event.isAdPluginPresent("vast") && event.isVideoPluginPresent("bit_wrapper"))
				result = result && seekValidator.validate("seeked_1", 10000);
			else{
				result = result && seekAction.fromLast().setTime(30).startAction();
				result = result && event.validate("seeked_1", 10000);
			}
			
			result = result && event.validate("videoPlayed_1", 90000);
			result = result && event.validate("PostRoll_willPlaySingleAd_1", 90000);
			
			// this is not working for IE11 BitmovinPostrol_VPAID2.0_HLS - need to check in actual browser
			
			if (!(getBrowser().contains("internet explorer") && event.isVideoPluginPresent("bit")
					&& event.isAdPluginPresent("vast") && event.isStreamingProtocolPrioritized("hls")))
				result = result && event.validate("singleAdPlayed_1", 190000); 
			
			result = result && event.validate("played_1", 200000);
			

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
