package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

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

	public boolean  validate(String element, int timeout) throws Exception {
		if(!waitOnElement("PAUSE_BUTTON", 60)) return false;
		if(!clickOnIndependentElement("PAUSE_BUTTON")) return false;

		Thread.sleep(1000);
		/*if (isElementPresent("PAUSE_SCREEN")) {
			logger.info("verify pause screen");
			if(!waitOnElement("PAUSE_SCREEN", 60)) return false;
		} else {

			logger.info("verify discovery if set on pausescreen");
			if(!waitOnElement("CONTENT_SCREEN", 60)) return false;
		}*/
		
		if(!waitOnElement("PAUSE_SCREEN", 60)) return false;

		if(!waitOnElement(By.id(element), timeout)) return false;
		return true;
	}
}
