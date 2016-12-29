package com.ooyala.playback.page;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/16/16.
 */
public class ControlBarValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(ControlBarValidator.class);

	public ControlBarValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("controlbar");
		addElementToPageElements("live");
		live = false;
	}

	private boolean live = false;

	public ControlBarValidator live() {
		live = true;
		return this;
	}

	public boolean validate(String element, int timeout) throws Exception {

		ArrayList<String> controlBarElement = new ArrayList<String>();

		controlBarElement
				.addAll(Arrays.asList("PLAY_HEAD", "PLAY_PAUSE", "VOLUME_BUTTON", "SHARE_BTN", "FULLSCREEN_BTN"));
		// "DISCOVERY_BTN", "TIME_DURATION" - no time duration for live

		boolean iscontrolshown = isElementPresent("CONTROL_BAR");

		if (!iscontrolshown) {
			extentTest.log(LogStatus.INFO, "Control bar is hiden hence mouse hovering on it");
			moveElement(getWebElement("CONTROL_BAR"));

		}
		try {

			if (live) {
				if (!waitOnElement("LIVE", 6000)) {
					extentTest.log(LogStatus.FAIL, "Live not shown in control bar.");
					return false;
				}
			}

			for (String icon : controlBarElement) {
				if (!waitOnElement(icon, 60000))
					return false;
			}
			boolean ismoreoption = isElementVisible("MORE_OPTION_ITEM");
			if (ismoreoption) {
				return clickOnIndependentElement("MORE_OPTION_ITEM") && waitOnElement("DISCOVERY_BTN", 60000)
						&& waitOnElement("QUALITY_BTN", 60000) && clickOnIndependentElement("CC_PANEL_CLOSE");
			} else
				return true;
		} catch (Exception e) {

			return waitOnElement("PLAY_PAUSE", 60000) && waitOnElement("VOLUME_BUTTON", 60000)
					&& waitOnElement("FULLSCREEN_BTN", 60000);

		}

		// Add feature for ooyalalogo
		/*
		 * String ooyalalogo =
		 * webDriver.findElement(locators.getobjectLocator("ooyalaLogo"
		 * )).findElement(By.tagName("img")).getAttribute("src");
		 * if(!(ooyalalogo.contains(".png") || ooyalalogo.contains(".svg") ||
		 * ooyalalogo.contains(".jpg") || ooyalalogo.contains(".gif")))
		 * Assert.assertTrue(ooyalalogo.contains(".png"),
		 * "Ooyala branding Logo is not present");
		 */

	}
	
	public boolean validateOrderingOfElements(){
		boolean iscontrolshown = isElementPresent("CONTROL_BAR");

		if (!iscontrolshown) {
			extentTest.log(LogStatus.INFO, "Control bar is hiden hence mouse hovering on it");
			moveElement(getWebElement("CONTROL_BAR"));

		}
		
		asserts.elementPosition()
		.thisIs(getWebElement("PLAY_PAUSE"))
		.leftOf(getWebElement("VOLUME_BUTTON"))
		.leftOf(getWebElement("SHARE_BTN"))
		.leftOf(getWebElement("FULLSCREEN_BTN"));
		
		return true;
	}
}
