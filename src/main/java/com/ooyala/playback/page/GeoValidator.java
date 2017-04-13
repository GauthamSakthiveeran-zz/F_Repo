package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
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
		String country = driver.executeScript("return $.get(\"http://ipinfo.io\", function(response) {\n"
				+ "   console.log(response.country);\n" + "}, \"jsonp\");").toString();
		logger.info("Contry :" + country);
		if (country.equalsIgnoreCase("US")) {
			String errorCode = driver.executeScript("return pp.getErrorCode()").toString();
			if (errorCode != null) {
				logger.error("Geo Syndication is not working");
				extentTest.log(LogStatus.FAIL, "Geo Syndication is not working");
				return false;
			}
		} else {
			return new PlayBackFactory(driver, extentTest).getErrorDescriptionValidator().expectedErrorCode("geo")
					.expectedErrorDesc("This video is not authorized in your location").validate("", 1);

		}
		return false;
	}

}
