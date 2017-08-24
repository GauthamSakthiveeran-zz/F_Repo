package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.qe.common.util.PropertyReader;
import com.relevantcodes.extentreports.LogStatus;

public class PlayerSkinCaptionsValidator extends PlayBackPage {

	private static Logger logger = Logger.getLogger(PlayerSkinCaptionsValidator.class);

	public PlayerSkinCaptionsValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("cc");
		addElementToPageElements("fcc");

	}

	public boolean verifyWebElementCSSProperty(String element, String cssStyleProperty, String value) {
		PropertyReader properties = null;
		try {
			properties = PropertyReader.getInstance("cssProperty.properties");
			if ((getWebElement(element).getCssValue(cssStyleProperty))
					.equalsIgnoreCase(properties.getProperty(value))) {
				logger.info("css Property Check Passed");
				extentTest.log(LogStatus.PASS, "Color of Element matched." + element + "-" + value);
				return true;
			} else {
				logger.info("css Property Check Failed");
				extentTest.log(LogStatus.FAIL, "Color of Element not matched." + element + "-" + value);
				return false;

			}

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, e);
			return false;
		}

	}

}