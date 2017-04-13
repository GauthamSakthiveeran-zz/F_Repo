package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.publishingrules.APIUtils;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by suraj on 3/22/17.
 */
public class GeoValidator extends PlayBackPage implements PlaybackValidator {

	private static Logger logger = Logger.getLogger(GeoValidator.class);

	public GeoValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}

	public boolean validate(String element, int timeout) throws Exception { 
		if (!loadingSpinner()) {
			return false;
		}
		String country = new APIUtils().getCountry();
		logger.info("Contry :" + country);
		if (country.contains("US")) {
			if(new PlayBackFactory(driver, extentTest).getPlayValidator().waitForPage()){
				return true;
			} else{
				extentTest.log(LogStatus.PASS, "Video should be playable in US");
			}
		} else {
			return new PlayBackFactory(driver, extentTest).getErrorDescriptionValidator().expectedErrorCode("geo")
					.expectedErrorDesc("This video is not authorized in your location").validate("", 1);

		}
		return false;
	}

}
