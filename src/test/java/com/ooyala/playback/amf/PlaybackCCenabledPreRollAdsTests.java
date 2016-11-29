package com.ooyala.playback.amf;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

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
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

            result = result && playValidator.waitForPage();
			Thread.sleep(10000);

			injectScript();

            result = result && playAction.startAction();

			// TODO - not sure if this is needed

			/*
			 * if (Description.equalsIgnoreCase("Preroll_Bitmovin_Pulse_CC")){
			 * waitForElement(webDriver, "singleAdPlayed_2", 190);
			 * loadingSpinner(webDriver); } else { waitForElement(webDriver,
			 * "singleAdPlayed_1", 190); loadingSpinner(webDriver); }
			 */

			event.validateForSpecificPlugins("singleAdPlayed_2", 190, "pulse");

            result = result && event.validate("singleAdPlayed_1", 190);

			extentTest.log(PASS, "Preroll Ad Completed");

            result = result && event.validate("playing_1", 120);

			extentTest.log(PASS, "Main video started to play");

			sleep(2000);

			pauseAction.startAction();

            result = result && ccValidator.validate("cclanguage", 60);

			sleep(2000);
            result = result && seekAction.setTime(10).fromLast().startAction();

			/*
			 * if(Description.equalsIgnoreCase("BitmovinCCenabledpreroll_IMA")){
			 * ((JavascriptExecutor)
			 * webDriver).executeScript("pp.seek(pp.getDuration()-28);"); }else
			 * { ((JavascriptExecutor)
			 * webDriver).executeScript("pp.seek(pp.getDuration()-10);"); }
			 */

			Thread.sleep(5000);

			event.validate("seeked_1", 190);
            result = result && event.validate("played_1", 190);

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
