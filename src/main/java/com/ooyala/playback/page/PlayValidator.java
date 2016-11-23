package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

public class PlayValidator extends PlayBackPage implements PlaybackValidator {

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
		// loadingSpinner();
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
		extentTest.log(LogStatus.INFO, "Successfully found play button");

		return errorScreen;

	}

	public void validate(String element, int timeout) throws Exception {
		// loadingSpinner();

		// clickOnIndependentElement("PLAY_BUTTON");
		PlayBackFactory.getInstance(driver).getPlayAction().startAction();
		extentTest.log(LogStatus.PASS, "Clicked on play button");
		Thread.sleep(1000);

		waitOnElement("PLAYING_SCREEN", 60);

		waitOnElement(By.id(element), timeout);
		extentTest.log(LogStatus.PASS,
				"Video Playing and validation of element " + element
						+ " is successful");
	}
}
