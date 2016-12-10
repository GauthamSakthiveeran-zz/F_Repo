package com.ooyala.playback.syndicationrules;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SaasPortValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by snehal on 23/11/16.
 */
public class PlaybackOptEntitlementTests extends PlaybackWebTest {

	private static Logger logger = Logger
			.getLogger(PlaybackOptEntitlementTests.class);
	private EventValidator eventValidator;
	private PlayValidator play;
	private SeekValidator seek;
	private SaasPortValidator sasport;

	public PlaybackOptEntitlementTests() throws OoyalaException {
		super();
	}

	@Test(groups = "syndicationRules", dataProvider = "testUrls")
	public void testOptEntitlementAlice(String testName, String url)
			throws OoyalaException {
		boolean result = true;
		try {
			sasport.validate("CREATE_ENTITLEMENT", 300);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}
			driver.get(url);
			result = result && play.waitForPage();
			Thread.sleep(10000);

			injectScript();
			result = result && play.validate("playing_1", 60000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

			result = result && sasport.validate("DISPLAY_BTN", 10);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		Assert.assertTrue(result, "OPT ENtitlement tests failed");
	}
}
