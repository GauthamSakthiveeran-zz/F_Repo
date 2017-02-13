package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/8/16.
 */
public class ShareTabValidator extends PlayBackPage implements
		PlaybackValidator {
	private static Logger logger = Logger.getLogger(ShareTabValidator.class);

	public ShareTabValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("sharetab");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		try {
			if (!waitOnElement("SHARE_BTN", 30000))
				return false;
			if (!clickOnIndependentElement("SHARE_BTN"))
				return false;
			Thread.sleep(5000);
		} catch (Exception e) {
			logger.info("exception \n" + e.getMessage());
			if (!clickOnIndependentElement("MORE_OPTION_ITEM"))
				return false;
			if (!waitOnElement("SHARE_BTN", 60000))
				return false;
			if (!clickOnIndependentElement("SHARE_BTN"))
				return false;
			if (!isElementPresent("SHARE_TAB"))
				if (!clickOnIndependentElement("SHARE_BTN"))
					return false;
		}

		if (!isElementPresent("SHARE_TAB"))
			if (!clickOnIndependentElement("SHARE_BUTTON"))
				return false;
		if (!waitOnElement("CONTENT_SCREEN", 60000))
			return false;

		Thread.sleep(2000);

		String shareTab = readTextFromElement("SHARE_TAB");
		logger.info("Share Tab value " + shareTab);
		Thread.sleep(1000);

		String embedTab = readTextFromElement("EMBED_TAB");
		logger.info("Text in Embed Tab  " + embedTab);

		if (!shareTab.equalsIgnoreCase("Compartir")
				|| !embedTab.equalsIgnoreCase("Insertar")) {
			extentTest.log(LogStatus.FAIL, "Localization Failed.");
			return false;
		}

		return clickOnIndependentElement("SHARE_CLOSE");

	}

}
