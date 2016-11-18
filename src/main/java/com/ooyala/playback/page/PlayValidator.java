package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.qe.common.exception.OoyalaException;

public class PlayValidator extends BaseValidator {

    public static Logger Log = Logger.getLogger(PlayValidator.class);

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
		//loadingSpinner();
		boolean errorScreen = false;
		try {
			waitOnElement("PLAY_BUTTON", 60);
		} catch (Exception e) {
			driver.navigate().refresh();
			waitOnElement("INNER_WRAPPER", 60);
			errorScreen = isElementPresent("ERROR_SCREEN");
			if (errorScreen)
                driver.navigate().refresh();
				waitOnElement("PLAY_BUTTON", 60);
		}
        logger.info("Page is loaded completely");

		return errorScreen;

	}

	public void validate(String element, int timeout) throws Exception {
		//loadingSpinner();
        clickOnIndependentElement("PLAY_BUTTON");
		Thread.sleep(1000);
		waitOnElement("PLAYING_SCREEN", 60);
		waitOnElement(By.id(element), timeout);
        Log.info("Video Playing");
	}
}
