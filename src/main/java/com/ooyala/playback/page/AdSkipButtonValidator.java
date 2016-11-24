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

	public void validate(String element, int timeout) throws Exception {
		waitOnElement("showAdSkipButton_1", 60);
		try {
			waitOnElement("adSkipBtn", 10);
			clickOnIndependentElement("adSkipBtn");
			// verify that second ad skipped
			waitOnElement("skipAd_1", 60);
			extentTest.log(LogStatus.PASS,"Ad skiped");

		} catch (Exception e) {
			clickOnIndependentElement("videoAdUiPreSkipButton");
			waitOnElement("skipAd_1", 60);
//			extentTest.log(LogStatus.FAIL,"adSkip Button is not present!!");
			extentTest.log(LogStatus.INFO,"Validating videoAdUiPreSkipButton");
		}
	}

}
