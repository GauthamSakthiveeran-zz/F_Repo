package com.ooyala.playback.amf.midroll;

import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdClickThroughValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.MidrollAdValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.SetEmbedCodeValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.playback.utils.CommandLineParameters;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidRollPoddedAdsTests extends PlaybackWebTest {

	public PlaybackMidRollPoddedAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekAction seek;
	private SeekValidator seekValidator;
	private PoddedAdValidator poddedAdValidator;
	private MidrollAdValidator adStartTimeValidator;

	@Test(groups = { "amf", "podded", "midroll" }, dataProvider = "testUrls")
	public void verifyMidrollPodded(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());
			result = result && playValidator.waitForPage();
			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

			result = result && seek.seek("8");

			result = result
					&& adStartTimeValidator.setTime(url.getAdStartTime()).validateAdStartTime("MidRoll_willPlayAds");

			result = result && seek.fromLast().setTime(20).startAction();

			if (event.isAdPluginPresent("freewheel")
					|| (event.isAdPluginPresent("ima") && testName.contains("MAIN")
							&& System.getProperty(CommandLineParameters.platform).equalsIgnoreCase("android"))
					|| (event.isAdPluginPresent("ima") && testName.contains("MAIN"))) {
				result = result && event.validate("adsPlayed_2", 200000); // TODO
				result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_2", 120000);
			} else {
				result = result && event.validate("adsPlayed_1", 250000);
				result = result && poddedAdValidator.setPosition("MidRoll").validate("countPoddedAds_1", 120000);
			}

			result = result && seekValidator.validate("seeked_1", 60000);
			result = result && event.validate("played_1", 200000);
		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
	}
}
