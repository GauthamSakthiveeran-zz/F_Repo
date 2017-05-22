package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/1/16.
 */
public class UpNextValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(UpNextValidator.class);

	public UpNextValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("upnext");
	}

	public boolean validate(String element, int timeout) throws Exception {

		if (!loadingSpinner()){
			extentTest.log(LogStatus.FAIL,"loading spinner appears for long time");
			return false;
		}

		try {
			// inc the timeout because of pulse ads.
			return waitOnElement("UPNEXT_CONTENT", 250000) && waitOnElement("CONTENT_METADATA", 2000)
					&& clickOnIndependentElement("UPNEXT_CLOSE_BTN");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("UpNext is not showing");
			extentTest.log(LogStatus.FAIL, "No Upnext panel");
			return false;
		}

	}
}
