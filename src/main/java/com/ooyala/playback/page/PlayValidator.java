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
			if(!waitOnElement("PLAY_BUTTON", 60)) return false;
		} catch (Exception e) {
			driver.navigate().refresh();
			if(!waitOnElement("INNER_WRAPPER", 60)) return false;
			errorScreen = isElementPresent("ERROR_SCREEN");
			if (errorScreen)
				driver.navigate().refresh();
			if(!waitOnElement("PLAY_BUTTON", 60)) return false;
		}
		logger.info("Page is loaded completely");
		extentTest.log(LogStatus.PASS, "Successfully found play button");

		return true;

	}

	public boolean validate(String element, int timeout) throws Exception {
		
		Thread.sleep(2000);

		if(!PlayBackFactory.getInstance(driver).getPlayAction().startAction()) return false;
		
		if(!waitOnElement(By.id(element), timeout)) return false;
		extentTest.log(LogStatus.PASS,
				"Video Playing and validation of element " + element
						+ " is successful");
		return true;
	}
}
