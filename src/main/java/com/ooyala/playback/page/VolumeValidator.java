package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/8/16.
 */
public class VolumeValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(VolumeValidator.class);

	public VolumeValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("volume");
		addElementToPageElements("controlbar");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		double expectedmutevol = 0.0;
		double expectedmaxvol = 1.0;

		((JavascriptExecutor) driver).executeScript("pp.pause()");

		Long currentVolume = (Long) (((JavascriptExecutor) driver).executeScript("return pp.getVolume()"));
		logger.info("Current volume: " + currentVolume);
		try {

			if (!(isElementPresent("CONTROL_BAR"))) {
				moveElement(getWebElement("CONTROL_BAR"));
			}
			if (clickOnIndependentElement("VOLUME_MAX")) {
				double getmutevol = getVolume();
				if (getmutevol != expectedmutevol) {
					extentTest.log(LogStatus.FAIL, "Mute volume is't matching");
					return false;
				} else {
					extentTest.log(LogStatus.PASS, "Mute volume works");
				}

			} else {
				extentTest.log(LogStatus.FAIL, "Unable to click on Volume");
				return false;
			}
			if (clickOnIndependentElement("VOLUME_MUTE")) {
				double getMaxVol = getVolume();

				if (getMaxVol != expectedmaxvol) {
					extentTest.log(LogStatus.FAIL, "Max volume is not the same");
					return false;
				} else {
					extentTest.log(LogStatus.PASS, "Max volume works");
				}
			} else {
				extentTest.log(LogStatus.FAIL, "Unable to click on Volume");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, "Volume control is not working properly" + e.getMessage());
			return false;
		}

		((JavascriptExecutor) driver).executeScript("pp.play()");
		return true;
	}

	public boolean checkInitialVolume(String asset, double volume) throws Exception{
		double initialVolume = volume;
		logger.info("Intial Volume is set to "+volume);
        boolean isInitialTimeMatches = initialVolume == getVolume();
        if (isInitialTimeMatches) {
			logger.info("initial time matched for " + asset);
			extentTest.log(LogStatus.PASS, "initial time matched for " + asset);
		}
        else {
			logger.error("initial time not matching for " + asset);
		}
		return isInitialTimeMatches;
	}

	protected double getVolume() throws Exception {
		double volume = Double
				.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getVolume()").toString());
		extentTest.log(LogStatus.INFO, "volume set to " + volume);
		return volume;

	}
}
