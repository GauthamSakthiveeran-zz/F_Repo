package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
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

		if (!clickOnIndependentElement("PAUSE_BUTTON")){
			extentTest.log(LogStatus.FAIL, "Failed to click on PAUSE_BUTTON");
			return false;
		}

		// If discovery is enabled then we are not able to see pause screen and therefore handled that scenario
		// for discovery
		if (!waitOnElement("CONTENT_SCREEN",3000)){
			if (!waitOnElement("PAUSE_SCREEN", 3000)){
				if (getBrowser().contains("safari")) {
					int count = 5;
					while (count >= 0) {
						if (!clickOnIndependentElement("PAUSE_BUTTON")) {
							extentTest.log(LogStatus.FAIL, "FAILED to click on PAUSE_BUTTON.");
							return false;
						}
						if (waitOnElement(By.id(element), 1000)) {
							extentTest.log(LogStatus.PASS,
									"video is paused and validation of " + element + " is successful");
							return true;
						}
						count--;
					}
					clickOnHiddenElement("PAUSE_BUTTON");
				} else {
					return false;
				}
			}
		}
		if (!waitOnElement(By.id(element), timeout)){
			extentTest.log(LogStatus.FAIL, element + " not found.");
			return false;
		}
		return new PlayBackFactory(driver, extentTest).getScrubberValidator().validate("", 1000);
	}
}
