package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DestroyValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(DestroyValidator.class);

	public DestroyValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("startscreen");
	}

	public boolean validate(String element, int timeout) throws Exception {
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("pp.destroy()");

		return waitOnElement(element, timeout)
				&& isElementPresent("STATE_SCREEN_SELECTABLE"); // destroy_1

	}

}
