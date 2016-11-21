package com.ooyala.playback.page;

import static java.lang.System.out;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class AdSkipButtonValidator extends BaseValidator {

	public AdSkipButtonValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adclicks");
	}

	@Override
	public void validate(String element, int timeout) throws Exception {
		waitOnElement("showAdSkipButton_1", 60);
		try {
			waitOnElement("adSkipBtn", 10);
			clickOnIndependentElement("adSkipBtn");
			// verify that second ad skiped
			waitOnElement("skipAd_1", 60);
			out.println("Ad skiped");

		} catch (Exception e) {
			System.out.println("adSkip Button is not present!!");
		}
	}

}
