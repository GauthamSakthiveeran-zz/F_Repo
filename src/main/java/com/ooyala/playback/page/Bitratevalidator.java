package com.ooyala.playback.page;

import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/17/16.
 */
public class Bitratevalidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(Bitratevalidator.class);

	public Bitratevalidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("bitrate");

	}

	public void validate(String element, int timeout) throws Exception {
		boolean result;

		try {
			result = isElementPresent("BITRATE");
		} catch (Exception e) {
			clickOnIndependentElement("MORE_OPTION_ITEM");
			sleep(1000);
			result = isElementPresent("BITRATE");
			clickOnIndependentElement("BITRATE");

		}
		/*
		 * Todo change birtate selection and verify List<WebElement>
		 * bitrateSelection = getWebElementsList("bitrateSelection");
		 */
	}
}
