package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by snehal on 29/11/16.
 */
public class IsAdPlayingValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(IsAdPlayingValidator.class);

	public IsAdPlayingValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		if (!loadingSpinner()) {
			return false;
		}
		boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver).executeScript("return pp.isAdPlaying()"));
		logger.info("is Ad Playing : ? "+isAdplaying);
		extentTest.log(LogStatus.INFO, "Ad is playing: " + isAdplaying);
		return isAdplaying;
	}

	public void skipAd() throws Exception {
		((JavascriptExecutor) driver).executeScript("pp.skipAd()");
	}

}
