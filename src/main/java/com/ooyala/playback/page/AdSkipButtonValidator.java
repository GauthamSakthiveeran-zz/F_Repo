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

	boolean custom = false;

	public AdSkipButtonValidator custom() {
		custom = true;
		return this;
	}

	public boolean validate(String element, int timeout) throws Exception {
		if (!waitOnElement(By.id("showAdSkipButton_1"), 60000))
			return false;
		try {
			if (custom)
				return true;
			
//			if(waitOnElement("VIDEO_AD_UI_PRE_SKIP_BUTTON",2000)){
//				return clickOnIndependentElement("VIDEO_AD_UI_PRE_SKIP_BUTTON") && waitOnElement("skipAd_1", 60000);
//			}

			return waitOnElement("AD_SKIP_BTN", 2000) && clickOnIndependentElement("AD_SKIP_BTN")
					&& waitOnElement(By.id("skipAd_1"), 60000);

		} catch (Exception e) {
			extentTest.log(LogStatus.INFO, "adSkip Button is not present!!");
			extentTest.log(LogStatus.INFO, "Validating videoAdUiPreSkipButton");
			return clickOnIndependentElement("VIDEO_AD_UI_PRE_SKIP_BUTTON") && waitOnElement("skipAd_1", 60000);
		}
	}

}
