package com.ooyala.playback.page;

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
		double expectedMuteVol = 0.0;
		double expectedMaxVol = 1.0;

		((JavascriptExecutor) driver).executeScript("pp.pause()");

		Long currentVolume = (Long) (((JavascriptExecutor) driver).executeScript("return pp.getVolume()"));
		logger.info("Current volume: " + currentVolume);

		if (getPlatform().equalsIgnoreCase("android")){
			return checkVolumeForAndroid(expectedMuteVol,expectedMaxVol);
		} else
			return checkVolumeForBrowser(expectedMuteVol,expectedMaxVol);
	}

	public boolean checkInitialVolume(String asset) throws Exception{
		double initialVolume = 0.5;
		logger.info("Initial Volume is set to "+initialVolume);
        boolean isInitialTimeMatches = initialVolume == getVolume();
        if (isInitialTimeMatches) {
			logger.info("initial time matched for " + asset);
			extentTest.log(LogStatus.PASS, "initial time matched for " + asset);
		}
        else {
			logger.error("initial time not matching for " + asset);
			extentTest.log(LogStatus.FAIL,"initial time not matching for " + asset);
		}
		return isInitialTimeMatches;
	}

	protected double getVolume() throws Exception {
		double volume = Double
				.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getVolume()").toString());
		extentTest.log(LogStatus.INFO, "volume set to " + volume);
		logger.info("volume set to " + volume);
		return volume;

	}

	public boolean checkVolumeForBrowser(double expectedMuteVol, double expectedMaxVol){
		try {

			if (!(isElementPresent("CONTROL_BAR"))) {
				moveElement(getWebElement("CONTROL_BAR"));
			}

			if (clickOnIndependentElement("VOLUME_MAX")) {
				double getMuteVol = getVolume();
				if (getMuteVol != expectedMuteVol) {
					extentTest.log(LogStatus.FAIL, "Mute volume isn't matching");
					logger.error("Mute volume isn't matching");
					return false;
				} else {
					extentTest.log(LogStatus.PASS, "Mute volume works");
					logger.info("Mute volume works");
				}

			} else {
				extentTest.log(LogStatus.FAIL, "Unable to click on Volume");
				logger.error("Unable to click on Volume");
				return false;
			}

			if (clickOnIndependentElement("VOLUME_MUTE")) {
				double getMaxVol = getVolume();

				if (getMaxVol != expectedMaxVol) {
					extentTest.log(LogStatus.FAIL, "Max volume is not the same");
					logger.error("Max volume is not the same");
					return false;
				} else {
					extentTest.log(LogStatus.PASS, "Max volume works");
					logger.info("Max volume works");
				}
			} else {
				extentTest.log(LogStatus.FAIL, "Unable to click on Volume");
				logger.error("Unable to click on Volume");
				return false;
			}

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Volume control is not working properly" + e.getMessage());
			logger.error("Volume control is not working properly \n" + e.getMessage());
			return false;
		}

		((JavascriptExecutor) driver).executeScript("pp.play()");
		return true;
	}

	public boolean checkVolumeForAndroid(double expectedMuteVol, double expectedMaxVol){
		double getMuteVol;
		try {

			if (!(isElementPresent("CONTROL_BAR"))) {
				moveElement(getWebElement("CONTROL_BAR"));
			}

			if (clickOnIndependentElement("VOLUME_MAX")) {
				if (!clickOnIndependentElement("VOLUME_MAX")){

					return false;
				}

				getMuteVol = getVolume();
				if (getMuteVol != expectedMuteVol) {
					extentTest.log(LogStatus.FAIL, "Mute volume is't matching");
					logger.error("Mute volume is't matching");
					return false;
				} else {
					extentTest.log(LogStatus.PASS, "Mute volume works");
					logger.info("Mute volume works");
				}

			} else {
				extentTest.log(LogStatus.FAIL, "Unable to click on Volume");
				logger.error("Unable to click on Volume");
				return false;
			}

			if (clickOnIndependentElement("VOLUME_MUTE")) {
				clickOnIndependentElement("VOLUME_MUTE");
				double getMaxVol = getVolume();
				if (getMaxVol != expectedMaxVol) {
					extentTest.log(LogStatus.FAIL, "Max volume is not the same");
					logger.error("Max volume is not the same");
					return false;
				} else {
					extentTest.log(LogStatus.PASS, "Max volume works");
					logger.info("Max volume works ");
				}
			} else {
				extentTest.log(LogStatus.FAIL, "Unable to click on Volume");
				logger.error("Unable to click on Volume");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, "Volume control is not working properly" + e.getMessage());
			logger.error("Volume control is not working properly \n" + e.getMessage());
			return false;
		}

		driver.executeScript("pp.play()");
		return true;
	}
}
