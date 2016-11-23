package com.ooyala.playback.page;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/21/16.
 */
public class SocailScreenValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(ControlBarValidator.class);

	public SocailScreenValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("controlbar");
		addElementToPageElements("sharetab");
		addElementToPageElements("socialscreen");

	}

	public void validate(String element, int timeout) throws Exception {

		try {
			waitOnElement("SHARE_BTN", 60);
			clickOnIndependentElement("SHARE_BTN");
		} catch (Exception e) {
			clickOnIndependentElement("MORE_OPTION_ITEM");
			waitOnElement("SHARE_BTN", 60);
			clickOnIndependentElement("SHARE_BTN");
		}
		sleep(2000);

		if (!isElementPresent("SHARE_TAB"))
			clickOnIndependentElement("SHARE_BTN");
		sleep(2000);

		assertEquals(isElementPresent("CONTENT_SCREEN"), true,
				"Social screen is not showing.");

		assertEquals(isElementPresent("SHARE_PANEL"), true,
				"Share tab is not display as default on Social screen.");

		assertEquals(isElementPresent("TWITTER"), true,
				"Social screen is not showing.");

		assertEquals(isElementPresent("FACEBOOK"), true,
				"Social screen is not showing.");

		assertEquals(isElementPresent("GOOGLE_PLUS"), true,
				"Social screen is not showing.");

	}
}