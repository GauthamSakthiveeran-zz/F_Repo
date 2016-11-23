package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class DestroyValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(DestroyValidator.class);

	public DestroyValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("startscreen");
	}

	public void validate(String element, int timeout) throws Exception {
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("pp.destroy()");

		waitOnElement("destroy_1", 50);

		boolean isPlayerPresent = isElementPresent("stateScreenSelectable");

		Assert.assertFalse(isPlayerPresent, "Player is not present");

	}

}
