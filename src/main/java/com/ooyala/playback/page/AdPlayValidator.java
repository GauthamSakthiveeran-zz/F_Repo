package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class AdPlayValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger Log = Logger.getLogger(AdPlayValidator.class);
	
	public AdPlayValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}

	public void validate(String element, int timeout) throws Exception {
		waitOnElement(element,timeout);
	}

}
