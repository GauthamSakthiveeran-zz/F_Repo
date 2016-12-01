package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackCCenabledPreRollAdsTests extends PlaybackWebTest {

	public PlaybackCCenabledPreRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private PauseAction pauseAction;
	private CCValidator ccValidator;

	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyCCenabledPreroll(String testName, String url)
			throws Exception {
		boolean result = true;

		try {
            driver.get(url);

            result = result && playValidator.waitForPage();

            injectScript();

            result = result && playAction.startAction();

			result = result && event.validateForSpecificPlugins("singleAdPlayed_2", 60000, "pulse");

            result = result && event.validate("singleAdPlayed_1", 60000);

            result = result && event.validate("playing_1", 10000);

            result = result && ccValidator.validate("cclanguage", 60000);

            result = result && seekAction.seekTillEnd().startAction();

			/*
			 * if(Description.equalsIgnoreCase("BitmovinCCenabledpreroll_IMA")){
			 * ((JavascriptExecutor)
			 * webDriver).executeScript("pp.seek(pp.getDuration()-28);"); }else
			 * { ((JavascriptExecutor)
			 * webDriver).executeScript("pp.seek(pp.getDuration()-10);"); }
			 */

			Thread.sleep(5000);

			result = result && event.validate("seeked_1", 190000);
            result = result && event.validate("played_1", 190000);

			boolean isccCueshowing = event
					.validateElementPresence("ccshowing_1");
			Assert.assertEquals(isccCueshowing, true,
					"ClosedCaption Cue is not changing");

			extentTest.log(PASS, "Video completed palying");

			extentTest.log(PASS, "Verified PreRoll Ads test");

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback CC Enabled PreRoll Ad tests failed");

	}

}
