package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageLevelOverridingValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger log = Logger.getLogger(PageLevelOverridingValidator.class);

	public PageLevelOverridingValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}


	public boolean validate(String element, int timeout) throws Exception {
		return false;
	}
}
