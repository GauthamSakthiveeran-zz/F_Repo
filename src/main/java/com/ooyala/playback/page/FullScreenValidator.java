package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class FullScreenValidator extends BaseValidator {

	public static Logger logger = Logger.getLogger(FullScreenValidator.class);
	
	public FullScreenValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("fullscreen");
	}

	public void validate(String element, int timeout) throws Exception {

		waitOnElement("fullScreenBtn", 60);
		if ((getBrowser().equalsIgnoreCase("firefox")
				|| getBrowser().equalsIgnoreCase("safari") || getBrowser()
				.equalsIgnoreCase("internet explorer"))) {
			clickOnHiddenElement("pauseButton");
			clickOnElement("fullScreenBtn");
		} else {
			if (getPlatform().equalsIgnoreCase("Android")) {
				clickOnHiddenElement("fullScreenBtn");
			} else {
				try {
					clickOnHiddenElement("pauseButton");
					clickOnElement("fullScreenBtn");
					logger.info("Clicked on FullScreen Button");
				} catch (Exception e) {
					clickOnHiddenElement("pauseButton");
					clickOnElement("fullScreenBtn");
					clickOnHiddenElement("playButton");
				}
			}
		}
	}
}
