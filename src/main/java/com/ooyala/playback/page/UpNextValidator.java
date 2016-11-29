package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/1/16.
 */
public class UpNextValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger Log = Logger.getLogger(UpNextValidator.class);

	public UpNextValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("upnext");
	}

	public boolean validate(String element, int timeout) throws Exception {
		
		try{
			return 
					waitOnElement("UPNEXT_CONTENT", 60000)
					&& waitOnElement("CONTENT_METADATA", 60000)
					&& clickOnIndependentElement("UPNEXT_CLOSE_BTN");
		}catch(Exception ex){
			ex.printStackTrace();
			extentTest.log(LogStatus.FAIL, "No Upnext panel");
			return false;
		}
		
		

	}
}
