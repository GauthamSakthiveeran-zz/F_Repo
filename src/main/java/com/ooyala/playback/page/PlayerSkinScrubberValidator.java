package com.ooyala.playback.page;
/**
 * Created by Gautham
 */

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.ooyala.qe.common.util.PropertyReader;
import com.relevantcodes.extentreports.LogStatus;

public class PlayerSkinScrubberValidator extends PlayBackPage {

	private static Logger logger = Logger.getLogger(PlayerSkinScrubberValidator.class);

	public PlayerSkinScrubberValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("controlbar");
		addElementToPageElements("scrubber");
		addElementToPageElements("cc");
		addElementToPageElements("sharetab");
		addElementToPageElements("volume");
		addElementToPageElements("bitrate");
		addElementToPageElements("fullscreen");
		addElementToPageElements("replay");
		addElementToPageElements("fcc");

	}

	// Function to Verify the CSS Property
	public boolean verifyWebElementCSSColor(String element, String cssStyleProperty, String color) {
		PropertyReader properties = null;
		try {
			properties = PropertyReader.getInstance("cssProperty.properties");
			if ((getWebElement(element).getCssValue(cssStyleProperty))
					.equalsIgnoreCase(properties.getProperty(color))) {
				logger.info("css Property Check Passed");
				extentTest.log(LogStatus.INFO, "Color of Element matched." + element + "-" + color);
				return true;
			} else {
				logger.info("css Property Check Failed");
				extentTest.log(LogStatus.INFO, "Color of Element not matched." + element + "-" + color);
				return false;

			}

		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e);
			return false;
		}

	}

	// Function to Mouseover and verify the CSS Properties
	public boolean moveToWebElementCSSColor(String element, String cssStyleProperty, String color) {

		Boolean result = false;

		moveElement(getWebElement(element));

		result = verifyWebElementCSSColor(element, cssStyleProperty, color);

		return result;

	}

	public boolean isCountdownNotPresent() {
		if (waitOnElement("AD_COUNTDOWN", 10000)) {
			if (getWebElement("AD_COUNTDOWN").getText().contains("00:")) {
				logger.info("AdCountDown is found ");
				extentTest.log(LogStatus.FAIL, "AdCountDown is found ");
				return false;
			} else {
				logger.info("AdCountDown not  found ");
				extentTest.log(LogStatus.PASS, "AdCountDown is found ");
				return true;
			}
		} else
			return true;
	}

	public boolean isElementNotPresentinAdScreen() {
		if (waitOnElement("AD_MARQUEE", 3000)) {

			logger.info("AdMarque found ");
			extentTest.log(LogStatus.FAIL, "AdMArque found ");
			return false;
		} else {
			logger.info("AdMarque not  found ");
			extentTest.log(LogStatus.PASS, "AdMArque found ");
			return true;
		}

	}

}