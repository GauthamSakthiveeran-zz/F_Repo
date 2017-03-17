package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Arrays;

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

		if (isElementPresent("HIDDEN_CONTROL_BAR")) {
			logger.info("hovering mouse over the player");
			moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
		}

		ArrayList<String> controlBarElement = new ArrayList<String>();

		controlBarElement
				.addAll(Arrays.asList("PLAY_HEAD", "PLAY_PAUSE", "VOLUME_BUTTON", "SHARE_BTN", "FULLSCREEN_BTN"));
		// "DISCOVERY_BTN", "TIME_DURATION" - no time duration for live

		boolean iscontrolshown = isElementPresent("CONTROL_BAR");

		if (!iscontrolshown) {
			extentTest.log(LogStatus.INFO, "Control bar is hidden hence mouse hovering on it");
			moveElement(getWebElement("CONTROL_BAR"));

		}
		try {

			if (live) {
				if (!isElementPresent("LIVE")) {
					extentTest.log(LogStatus.FAIL, "Live not shown in control bar.");
				}
			}

			for (String icon : controlBarElement) {
				if (!isElementPresent(icon)) {
					extentTest.log(LogStatus.FAIL, icon+" not shown in control bar.");
					return false;
				}
					
			}
			boolean ismoreoption = isElementVisible("MORE_OPTION_ITEM");
			if (ismoreoption) {
				return clickOnIndependentElement("MORE_OPTION_ITEM") && waitOnElement("DISCOVERY_BTN", 60000)
						&& waitOnElement("QUALITY_BTN", 60000) && clickOnIndependentElement("CC_PANEL_CLOSE");
			} else
				return true;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, e.getMessage());
			return false;
		}
	}
	
}
