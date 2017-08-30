package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

public class SeekValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(SeekValidator.class);

	public SeekValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("play");
	}

	private boolean skipScrubberValidation = false;

	public SeekValidator skipScrubberValidation() {
		skipScrubberValidation = true;
		return this;
	}

	public boolean validate(String element, int timeout) throws Exception {

		(new PlayBackFactory(driver, extentTest)).getSeekAction().seekTillEnd().startAction();

		if (!loadingSpinner()) {
			logger.error("Loading spinner seems to be there for a really long time.");
			extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
			return false;
		}

		if (driver.getCurrentUrl().contains("AnalyticsQEPlugin")) {
			if (!(isAnalyticsElementPreset("analytics_video_" + element)
					&& isAnalyticsElementPreset("analytics_video_requested_" + element))) {
				return false;
			}
		}

		if (getBrowser().equalsIgnoreCase("internet explorer")) {
			return true;
		}

		if (waitOnElement(By.id(element), timeout)) {
			extentTest.log(LogStatus.PASS, "Seek successful.");
			logger.info("Seek successful.");
			if (!loadingSpinner()) {
				logger.error("Loading spinner seems to be there for a really long time.");
				extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
				return false;
			}
			if(!skipScrubberValidation)
				return (new PlayBackFactory(driver, extentTest)).getScrubberValidator().validate("", timeout);
			return true;
		}

		extentTest.log(LogStatus.FAIL, "Wait on " + element + " failed after " + timeout + " ms");
		logger.error("Wait on " + element + " failed after " + timeout + " ms");
		return false;
	}
}
