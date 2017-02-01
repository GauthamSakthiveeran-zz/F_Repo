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
		if (!waitOnElement(By.id("showAdSkipButton_1"), 60000)){
			extentTest.log(LogStatus.FAIL, "Wait on element : showAdSkipButton_1 failed after " + timeout + " ms");
			return false;
		}
		try {

			if (isElementPresent("AD_SKIP_BTN")){
				if(clickOnIndependentElement("AD_SKIP_BTN") && waitOnElement(By.id("skipAd_1"), 60000)){
					 extentTest.log(LogStatus.PASS, "Clicked on Ad Skip Button.");
					 return true;
				 }else{
					 extentTest.log(LogStatus.FAIL, "Couldn't click on Ad Skip Button.");
					 return false;
				 }
			}/*else{
				driver.switchTo().frame(0);
				if (waitOnElement("VIDEO_AD_UI_PRE_SKIP_BUTTON")
						&& clickOnIndependentElement("VIDEO_AD_UI_PRE_SKIP_BUTTON")
						&& waitOnElement("skipAd_1", 60000)) {
					extentTest.log(LogStatus.PASS, "Clicked on Ad Skip Button.");
					driver.switchTo().defaultContent();
					return true;
				} else {
					extentTest.log(LogStatus.FAIL, "Couldn't click on Ad Skip Button.");
					driver.switchTo().defaultContent();
					return false;
				}
			}*/
				 
			return true;

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e.getMessage());
			return false;
		}
	}

}