package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.LogStatus;

public class ErrorDescriptionValidator extends PlayBackPage implements PlaybackValidator{
	
	private static Logger logger = Logger.getLogger(ErrorDescriptionValidator.class);

	public ErrorDescriptionValidator(WebDriver webDriver) {
		super(webDriver);
		addElementToPageElements("play");
	}
	
	private String expectedErrorCode;
	private String expectedErrorDesc;
	
	public ErrorDescriptionValidator expectedErrorCode(String expectedErrorCode){
		this.expectedErrorCode = expectedErrorCode;
		return this;
	}
	
	public ErrorDescriptionValidator expectedErrorDesc(String expectedErrorDesc){
		this.expectedErrorDesc = expectedErrorDesc;
		return this;
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
		if (!errorCode.equalsIgnoreCase(expectedErrorCode)) {
			logger.error("Syndication is not working");
			extentTest.log(LogStatus.FAIL, "Syndication is not working");
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
		
		if(!text.equals(expectedErrorDesc)) {
			extentTest.log(LogStatus.FAIL, "Error message is wrong");
			return false;
		}

		logger.info("Error description is present");
		return true;
	}
	

}
