package com.ooyala.playback.page;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/16/16.
 */
public class ControlBarValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(ControlBarValidator.class);

	public ControlBarValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("controlBar");

	}

	public void validate(String element, int timeout) throws Exception {

		ArrayList<String> controlBarElement = new ArrayList<String>();

		controlBarElement.addAll(Arrays.asList("PLAY_HEAD", "PLAY_PAUSE",
				"VOLUME_BUTTON", "FULLSCREEN_BTN", "SHARE_BTN",
				"DISCOVERY_BTN", "TIME_DURATION"));

		boolean iscontrolshown = isElementPresent("CONTROL_BAR");

		if (!iscontrolshown) {
			System.out
					.println("Control bar is hiden hence mouse hovering on it");
			Actions act = new Actions(driver);

			act.moveToElement(getWebElement("CONTROL_BAR")).build().perform();

		}
		try {
			for (String icon : controlBarElement) {
				waitOnElement(icon, 60);
			}
			boolean ismoreoption = isElementVisible("MORE_OPTION_ITEM");
			if (ismoreoption) {
				clickOnIndependentElement("MORE_OPTION_ITEM");
				waitOnElement("DISCOVERY_BTN", 60);
				waitOnElement("QUALITY_BTN", 60);
				clickOnIndependentElement("CC_PANEL_CLOSE");
			}
		} catch (Exception e) {

			waitOnElement("PLAY_PAUSE", 60);
			waitOnElement("VOLUME_BUTTON", 60);
			waitOnElement("FULLSCREEN_BTN", 60);
			// seleniumActions.waitForElement("OOYALA_LOGO", 60);

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
}
