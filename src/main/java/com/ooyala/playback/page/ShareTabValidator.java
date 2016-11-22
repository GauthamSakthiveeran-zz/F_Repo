package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * Created by soundarya on 11/8/16.
 */
public class ShareTabValidator extends PlayBackPage implements
		PlaybackValidator {
	public static Logger Log = Logger.getLogger(ShareTabValidator.class);

	public ShareTabValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("sharetab");
	}

	@Override
	public void validate(String element, int timeout) throws Exception {
		try {
			waitOnElement("SHARE_BTN", 60);
			clickOnIndependentElement("SHARE_BTN");
			Thread.sleep(5000);
		} catch (Exception e) {
			logger.info("exception \n" + e.getMessage());
			clickOnIndependentElement("MORE_OPTION_ITEM");
			waitOnElement("SHARE_BTN", 60);
			clickOnIndependentElement("SHARE_BTN");
			if (!(isElementPresent("SHARE_TAB")))
				clickOnIndependentElement("SHARE_BTN");
		}
		waitOnElement("CONTENT_SCREEN", 60);
		if (!(isElementPresent("SHARE_TAB")))
			clickOnIndependentElement("SHARE_BTN");
		Thread.sleep(2000);

		String shareTab = readTextFromElement("SHARE_TAB");
		Log.info("Share Tab value " + shareTab);
		Thread.sleep(1000);

		String embedTab = readTextFromElement("EMBED_TAB");
		Log.info("Text in Embed Tab  " + embedTab);

		Assert.assertTrue(shareTab.equalsIgnoreCase("Compartir"),
				"Localization Failing");
		Assert.assertTrue(embedTab.equalsIgnoreCase("Insertar"),
				"Localization Failing");
		clickOnIndependentElement("SHARE_CLOSE");
		Log.info("ShareTab and Embed Tab localization verified");

	}

}
