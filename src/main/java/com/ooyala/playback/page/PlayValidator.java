package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

import junit.framework.Assert;

public class PlayValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(PlayValidator.class);

	public PlayValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
	}
	
	private void errorDescription(){
		if(isElementPresent("ERROR_SCREEN")){
			String text = getWebElement("ERROR_DESCRIPTION").getText();
			extentTest.log(LogStatus.INFO,"FAIL : " + text);
		}
		
		Assert.assertTrue("Play button is not found", false);
	}

	@Override
	public boolean waitForPage() {
		boolean errorScreen = false;

		try {
			if (!waitOnElement("PLAY_BUTTON", 90000)) {
				errorDescription();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			driver.navigate().refresh();
			if (!waitOnElement("INNER_WRAPPER", 60000))
				return false;
			errorScreen = isElementPresent("ERROR_SCREEN");
			if (errorScreen)
				driver.navigate().refresh();
			if (!waitOnElement("PLAY_BUTTON", 60000)){
				errorDescription();
				return false;
			}
		}
		logger.info("Page is loaded completely");
		return true;
	}

	public boolean validate(String element, int timeout) throws Exception {

		// if(!PlayBackFactory.getInstance(driver).getPlayAction().startAction())
		// return false;

		if (!clickOnIndependentElement("PLAY_BUTTON")){
			extentTest.log(LogStatus.FAIL, "FAILED to click on PLAY_BUTTON.");
			return false;
		}

		if (!loadingSpinner()){
			extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
			return false;
		}

		if (!waitOnElement("PLAYING_SCREEN", 60000))
			return false;

		if (!waitOnElement(By.id(element), timeout))
			return false;
		extentTest.log(LogStatus.PASS,
				"Video Playing and validation of element " + element
						+ " is successful");
		return true;
	}
}
