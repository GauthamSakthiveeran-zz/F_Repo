package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.mortbay.log.Log;
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
		double getMuteVol;

		((JavascriptExecutor) driver).executeScript("pp.pause()");

		Long currentVolume = (Long) (((JavascriptExecutor) driver).executeScript("return pp.getVolume()"));
		logger.info("Current volume: " + currentVolume);

		try {

			if (!(isElementPresent("CONTROL_BAR"))) {
				moveElement(getWebElement("CONTROL_BAR"));
			}
			
			if(!isElementPresent("VOLUME_MAX")){
				extentTest.log(LogStatus.FAIL, "Volume icon not found.");
				return false;
			}

			if (clickOnIndependentElement("VOLUME_MAX")) {
			    if (getPlatform().equalsIgnoreCase("android")) {
                    if (!clickOnIndependentElement("VOLUME_MAX")) {
                        return false;
                    }
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
	
	public boolean validateInitialVolume(double volume) throws Exception {
		if(volume == getVolume()) {
			logger.info("initial volume is  set correctly in player");
			extentTest.log(LogStatus.PASS, "initial volume is set correctly in player");
			return true;
			
		}
		logger.info("initial volume is not set as per the player params");
		extentTest.log(LogStatus.INFO, "initial volume is not set as per playerparams");
		return false;
	}

	protected double getVolume() throws Exception {
		double volume = Double
				.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getVolume()").toString());
		extentTest.log(LogStatus.INFO, "volume set to " + volume);
		logger.info("volume set to " + volume);
		return volume;

	}
}
