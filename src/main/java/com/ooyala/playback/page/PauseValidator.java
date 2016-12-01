package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
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
		
		Actions action = new Actions(driver);
		
		if (isElementPresent("HIDDEN_CONTROL_BAR")) {
			logger.info("hovering mouse over the player");
			action.moveToElement(getWebElement("HIDDEN_CONTROL_BAR")).perform();
		}
		
		if(!clickOnIndependentElement("PAUSE_BUTTON")) return false;

		Thread.sleep(1000);
		
		if(!waitOnElement("PAUSE_SCREEN", 60000)) return false;

		if(!waitOnElement(By.id(element), timeout)) return false;
		return true;
	}
}
