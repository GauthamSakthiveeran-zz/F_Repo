package com.ooyala.playback.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class DestroyValidator extends BaseValidator{

	public DestroyValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("startscreen");
	}

	@Override
	public void validate(String element, int timeout) throws Exception {
		Thread.sleep(2000);
        ((JavascriptExecutor) driver).executeScript("pp.destroy()");

        waitOnElement("destroy_1", 50);

        boolean isPlayerPresent = isElementPresent("stateScreenSelectable");

        Assert.assertFalse(isPlayerPresent,"Player is not present");
		
	}

}
