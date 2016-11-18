package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 10/27/16.
 */
public class PauseValidator extends BaseValidator {

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
		waitOnElement("pauseButton", 60);
        clickOnIndependentElement("pauseButton");
		Thread.sleep(1000);
		if (isElementPresent("pauseScreen")) {
			logger.info("verify pause screen");
			waitOnElement("pauseScreen", 60);
		} else {
			// verify discovery if there is on pauseDiscovery
			logger.info("verify discovery if set on pausescreen");
			waitOnElement("contentScreen", 60);
		}

        waitOnElement(By.id(element),timeout);
        logger.info("Video paused");
	}
}
