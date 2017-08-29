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
	
	private String expectedErrorCode = "";
	private String expectedErrorDesc = "";
	
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
		if(driver.executeScript("return pp.getErrorCode()")==null){
			extentTest.log(LogStatus.FAIL, "Failed to get error code.");
			return false;
		}
		String errorCode = driver.executeScript("return pp.getErrorCode()").toString();
		logger.info("Error code :" + errorCode);
		if (!errorCode.equalsIgnoreCase(expectedErrorCode)) {
			logger.error("Failed to get error code.");
			extentTest.log(LogStatus.FAIL, "Failed to get error code.");
			return false;
		}
		return true;
	}
	
	private boolean errorDescription() {
		
		int count =5;

		logger.info("Checking error description");

		if (!waitOnElement("ERROR_SCREEN", 10000)) {
			if(count>=0) {
				driver.navigate().refresh();
				isPageLoaded();
				count--;
				return errorDescription();
			}
			logger.error("Error screen is not showing");
			extentTest.log(LogStatus.FAIL, "Error screen is not showing");
			return false;
		}
		
		if (!waitOnElement("ERROR_DESCRIPTION", 60000)) {
			if(expectedErrorDesc.isEmpty())
				return true;
			logger.error("ERROR_DESCRIPTION is not showing");
			extentTest.log(LogStatus.FAIL, "ERROR_DESCRIPTION is not showing");
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

    public void setErrorCode(String errorCode){
        driver.executeScript("pp.mb.publish(OO.EVENTS.ERROR,{code:\""+errorCode+"\"})");
    }
	

}
