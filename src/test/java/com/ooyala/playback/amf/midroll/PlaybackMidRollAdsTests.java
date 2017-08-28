package com.ooyala.playback.amf.midroll;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.MidrollAdValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private MidrollAdValidator midrollAdValidator;
	private SeekAction seekAction;

	@Test(groups = { "amf", "midroll" }, dataProvider = "testUrls")
	public void verifyMidRoll(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());
			result = result && playValidator.waitForPage();
			injectScript();
			result = result && playValidator.validate("playing_1", 60000);
			result = result && midrollAdValidator.validateMidrollAd(url);
			result = result && event.validate("playing_2", 160000);
			event.validateVideoStartTime(Double.parseDouble(url.getAdStartTime())+1);
			result = result && seekValidator.validate("seeked_1", 160000);
			result = result && event.validate("played_1", 160000);

			if (result && (!url.getAdPlugins().equalsIgnoreCase("pulse"))) {
				extentTest.log(LogStatus.INFO, "Validating if ad plays when video is seeked post ad play time.");
				driver.navigate().refresh();
				result = result && playValidator.waitForPage();
				injectScript();
				result = result && playValidator.validate("playing_1", 60000);
				result = result && seekAction.seek(15, true);
				url.setAdStartTime(null);
				result = result && midrollAdValidator.validateMidrollAd(url);
				Thread.sleep(3000);
				result = result && seekAction.setTime(1).startAction();
				result = result && !event.checkIsAdPlaying();
				result = result && seekAction.fromLast().setTime(2).startAction();
				result = result && !event.checkIsAdPlaying();
				result = result && event.validate("played_1", 10000);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}
		Assert.assertTrue(result, "Verified");
	}
}