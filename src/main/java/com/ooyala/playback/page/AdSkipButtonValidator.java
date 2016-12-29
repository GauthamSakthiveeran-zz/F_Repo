package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

public class AdSkipButtonValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(AdSkipButtonValidator.class);

	public AdSkipButtonValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("adclicks");
	}
	
	public boolean validate(String element, int timeout) throws Exception {
		if (!waitOnElement(By.id("showAdSkipButton_1"), 60000)) {
			asserts.assertTrue(false, "Wait on element showAdSkipButton_1 failed after 60000 ms.");
			return false;
		}
		try {

			if (isElementPresent("AD_SKIP_BTN")) {
				asserts.assertTrue(clickOnIndependentElement("AD_SKIP_BTN") && waitOnElement(By.id("skipAd_1"), 60000),
						"Click on Ad skip button failed.");
			} else {
				asserts.assertTrue(false, "AD_SKIP_BTN not found.");
			}

			return true;

		} catch (Exception e) {
			extentTest.log(LogStatus.INFO, "Ad Skip Button is not present!!");
			extentTest.log(LogStatus.INFO, "Validating videoAdUiPreSkipButton");
			asserts.assertTrue(
					clickOnIndependentElement("VIDEO_AD_UI_PRE_SKIP_BUTTON") && waitOnElement(By.id("skipAd_1"), 60000),
					"Click on VIDEO_AD_UI_PRE_SKIP_BUTTON failed.");
			return true;
		}
	}

}