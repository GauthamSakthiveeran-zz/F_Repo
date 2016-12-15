package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

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

	@Override
	public boolean waitForPage() {
		boolean errorScreen = false;

		try {
			if (!waitOnElement("PLAY_BUTTON", 90000)) {
				errorScreen = isElementPresent("ERROR_SCREEN");
				if (errorScreen
						&& getWebElement("ERROR_DESCRIPTION")
								.getText()
								.equalsIgnoreCase(
										"This video isn't encoded for your device")) {
					extentTest.log(LogStatus.ERROR,
							"Video format is not supported in this browser");
				}
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
			if (!waitOnElement("PLAY_BUTTON", 60000))
				return false;
		}
		logger.info("Page is loaded completely");
		return true;
	}

	public boolean validate(String element, int timeout) throws Exception {

		// if(!PlayBackFactory.getInstance(driver).getPlayAction().startAction())
		// return false;

		if (!clickOnIndependentElement("PLAY_BUTTON"))
			return false;

		if (!loadingSpinner())
			return false;

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
