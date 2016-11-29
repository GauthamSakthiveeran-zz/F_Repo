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
		if(!waitOnElement("PAUSE_BUTTON", 60000)) return false;
		if(!clickOnIndependentElement("PAUSE_BUTTON")) return false;

		Thread.sleep(1000);
		
		if(!waitOnElement("PAUSE_SCREEN", 60000)) return false;

		if(!waitOnElement(By.id(element), timeout)) return false;
		return true;
	}
}
