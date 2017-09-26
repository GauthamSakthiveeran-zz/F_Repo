package com.ooyala.playback.playerParameter;


import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.AdFrequencyValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class AutoplayAdFrequencyTest extends PlaybackWebTest {

	public static Logger logger = Logger.getLogger(AutoplayAdFrequencyTest.class);
	private PlayValidator playValidator;
	private AdFrequencyValidator adFrequencyValidator;
	private VolumeValidator volumeValidator;
	private EventValidator event;
	private AutoplayAction autoplayAction;

	public AutoplayAdFrequencyTest() throws OoyalaException {
		super();
	}

	@Test(groups = { "amf", "adFrequency" }, dataProvider = "testUrls")
	public void verifyAdFrequency(String testDescription, UrlObject url) throws OoyalaException {
		boolean result = true;
		try {
			driver.get(url.getUrl());
			logger.info("Navigated to : "+url.getUrl());

			Assert.assertEquals(event.isPageLoaded(),true,"Failed to clear cache");

			Assert.assertEquals(autoplayAction.startAction(),true,"Failed to wait for page");

			injectScript();

			Assert.assertEquals(adFrequencyValidator.split(url).validate("", 1000),true,"Failed to validate ad Frequency");

			Assert.assertEquals(volumeValidator.validateInitialVolume(0.5), true,"initial volume is not matching with test data"); 
			Assert.assertEquals(adFrequencyValidator.validateAdCapFrequency(testDescription,url.getAdFirstPlay(),url.getAdFrequency(),2000),true,"Failed to validate ad Cap Frequency");

		} catch (Exception e) {
			logger.error(e.getMessage());
			result = false;
			extentTest.log(LogStatus.FAIL, e);
		}
		Assert.assertTrue(result, "Ad frequency tests failed.");
	}
}