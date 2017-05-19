package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
			//If browser is Safari, then we have to click on element using javascript
			if (getBrowser().contains("safari")) {
				return new PlayBackFactory(driver,extentTest).getSafariValidator().validate("SHARE_BTN",10000);
			}
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


		String shareTab = readTextFromElement("SHARE_TAB");
		logger.info("Share Tab value " + shareTab);

		String embedTab = readTextFromElement("EMBED_TAB");
		logger.info("Text in Embed Tab  " + embedTab);

		if (!shareTab.equalsIgnoreCase("Compartir")
				|| !embedTab.equalsIgnoreCase("Insertar")) {
			extentTest.log(LogStatus.FAIL, "Localization Failed.");
			return false;
		}

		if(!clickOnIndependentElement("SHARE_CLOSE")) {
			return false;
		}
		//If browser is Safari, then we have to click on element using javascript
		if (getBrowser().contains("safari")) {
			try {
				WebElement element1 = getWebElement("SHARE_CLOSE");
				driver.executeScript("arguments[0].click()", element1);
			}catch(Exception e){
				logger.error("unable to click on close button even eith javascript executor" +e.getMessage());
			}
		}

		return true;
	}
}
