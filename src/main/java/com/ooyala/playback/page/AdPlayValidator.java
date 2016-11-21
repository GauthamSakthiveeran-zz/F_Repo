package com.ooyala.playback.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class AdPlayValidator extends BaseValidator {

	public AdPlayValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}

	@Override
	public void validate(String element, int timeout) throws Exception {
		waitOnElement(element,timeout);
	}

}
