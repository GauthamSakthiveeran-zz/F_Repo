package com.ooyala.playback.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/1/16.
 */
public class UpNextValidator extends BaseValidator {

	public UpNextValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("upnext");
	}

	public void validate(String element, int timeout) throws Exception {

		waitOnElement(element, 60);
		waitOnElement("contentMetadata", 60);
		clickOnIndependentElement("upNextCloseBtn");
	}
}
