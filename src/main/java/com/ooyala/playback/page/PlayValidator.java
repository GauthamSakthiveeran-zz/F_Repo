package com.ooyala.playback.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.qe.common.exception.OoyalaException;

public class PlayValidator extends BaseValidator {

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
		loadingSpinner();
		boolean errorScreen = false;
		try {
			waitOnElement("PLAY_BUTTON", 60);
			errorScreen = true;
		} catch (Exception e) {
			driver.navigate().refresh();
			waitOnElement("INNER_WRAPPER", 60);
			errorScreen = isElementVisible("ERROR_SCREEN");
			if (!errorScreen)
				waitOnElement("PLAY_BUTTON", 60);
		}
		// finally {
		// errorScreen = isElementVisible("ERROR_SCREEN");
		// if (errorScreen) {
		// logger.info("Error screen occured hence refreshing page again");
		// driver.navigate().refresh();
		// } else {
		// logger.info("Page is loaded completely");
		// }
		// }

		return errorScreen;

	}

	public void validate(String element, int timeout) throws Exception {
		

		loadingSpinner();
		if (isElementVisible("START_SCREEN")) {
			clickOnIndependentElement("PLAY_BUTTON");
		} else {
			clickOnElement("PLAY_BUTTON");
		}
		Thread.sleep(1000);
		waitOnElement("PLAYING_SCREEN", 60);
		waitOnElement("PLAYING", timeout);
	}
}
