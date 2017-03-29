package com.ooyala.playback.amf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackIMAPreVastMidAdsTests extends PlaybackWebTest {

	public PlaybackIMAPreVastMidAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyIMAPreVastMidAds(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {

			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playAction.startAction();

			result = result && event.validate("PreRoll_willPlayAds", 120000);
			result = result && event.validate("adsPlayed_1", 200000);

			result = result && event.validate("adPodEnd_google-ima-ads-manager_0_1", 6000);

			result = result && event.validate("playing_1", 20000);

			result = result && event.validate("MidRoll_willPlayAds_2", 100000);
			result = result && event.validate("adsPlayed_2", 200000);
			
			result = result && seekValidator.validate("seeked_1", 190000);

			if(!getBrowser().contains("internet explorer") && !getBrowser().contains("safari") && !getBrowser().equalsIgnoreCase("MicrosoftEdge")){
				result = result && event.validate("adPodEnd_vast_2_3", 6000);
			}else{
				result = result && event.validate("adPodEnd_vast_2_2", 6000);
			}
			
			result = result && event.validate("played_1", 200000);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Tests failed");

	}

}
