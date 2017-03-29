package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 3/23/17.
 */
public class FlightTimeValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(FlightTimeValidator.class);

	public FlightTimeValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("play");
	}

	public boolean validate(String element, int timeout) throws Exception {
		if (!loadingSpinner()) {
			return false;
		}

		if (!errorDescription()) {
			return false;
		}
		String errorCode = driver.executeScript("return pp.getErrorCode()").toString();
		logger.info("Error code :" + errorCode);
		if (!errorCode.equalsIgnoreCase("past")) {
			logger.error("Flight time Syndication is not working");
			extentTest.log(LogStatus.FAIL, "Flight Time Syndication is not working");
			return false;
		}
		return true;
	}

	public boolean errorDescription() {

		logger.info("Checking error description");

		if (!waitOnElement("ERROR_SCREEN", 20000)) {
			logger.error("Error screen is not showing");
			extentTest.log(LogStatus.FAIL, "Error screen is not showing");
			return false;
		}
		
		String text = getWebElement("ERROR_DESCRIPTION").getText();
		
		if(!text.equals("This video is no longer available")) {
			extentTest.log(LogStatus.FAIL, "Error message is wrong");
			return false;
		}

		logger.info("Error description is present");
		return true;
	}

}
