package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 10/27/16.
 */
public class PauseValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(PauseValidator.class);

	public PauseValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("pause");
		addElementToPageElements("discovery");
	}

	public boolean validate(String element, int timeout) throws Exception {

		if (isElementPresent("HIDDEN_CONTROL_BAR")) {
			logger.info("hovering mouse over the player");
			moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
		}
		Thread.sleep(1000);

		if (!clickOnIndependentElement("PAUSE_BUTTON")){
			extentTest.log(LogStatus.FAIL, "Failed to click on PAUSE_BUTTON");
			return false;
		}

		Thread.sleep(1000);

		// If discovery is enabled then we are not able to see pause screen and therefore handled that scenario
		// for discovery
		if (!waitOnElement("CONTENT_SCREEN",5000)){
			if (!waitOnElement("PAUSE_SCREEN", 60000)){
				extentTest.log(LogStatus.FAIL, "PAUSE_SCREEN not found.");
				return false;
			}
		}


		if (!waitOnElement(By.id(element), timeout)){
			extentTest.log(LogStatus.FAIL, element + " not found.");
			return false;
		}
		return true;
	}
}
