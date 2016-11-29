package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

public class AdSkipButtonValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(AdSkipButtonValidator.class);

	public AdSkipButtonValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adclicks");
	}

	public boolean validate(String element, int timeout) throws Exception {
		if(!waitOnElement("showAdSkipButton_1", 60)) return false;
		try {
			return waitOnElement("AD_PANEL", 10) 
					&& clickOnIndependentElement("AD_PANEL") 
					&& waitOnElement("skipAd_1", 60);

		} catch (Exception e) {
			extentTest.log(LogStatus.WARNING,"adSkip Button is not present!!");
			extentTest.log(LogStatus.INFO,"Validating videoAdUiPreSkipButton");
			
			return clickOnIndependentElement("VIDEO_AD_UI_PRE_SKIP_BUTTON") && waitOnElement("skipAd_1", 60);
		}
	}

}
