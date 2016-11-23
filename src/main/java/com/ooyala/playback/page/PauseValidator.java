package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;

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
	}

	public void validate(String element, int timeout) throws Exception {
		waitOnElement("PAUSE_BUTTON", 60);
		clickOnIndependentElement("PAUSE_BUTTON");
		//PlayBackFactory.getInstance(driver).getPauseAction().startAction();
		Thread.sleep(1000);
		if (isElementPresent("PAUSE_SCREEN")) {
			logger.info("verify pause screen");
			waitOnElement("PAUSE_SCREEN", 60);
		} else {
			// verify discovery if there is on pauseDiscovery
			logger.info("verify discovery if set on pausescreen");
			waitOnElement("CONTENT_SCREEN", 60);
		}

		waitOnElement(By.id(element), timeout);
		logger.info("Video paused");
	}
}
