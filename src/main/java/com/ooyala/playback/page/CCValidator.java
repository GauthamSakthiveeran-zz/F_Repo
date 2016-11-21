package com.ooyala.playback.page;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * Created by soundarya on 11/3/16.
 */
public class CCValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(CCValidator.class);

	public CCValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("cc");
		addElementToPageElements("play");
		addElementToPageElements("controlBar");
	}

	public void validate(String element, int timeout) throws Exception {
		try {
			try {
				waitOnElement("CC_BTN", 60);
			} catch (Exception e) {
				clickOnIndependentElement("MORE_OPTION_ICON");
				waitOnElement("CC_BTN", 60);
			}
			boolean ccbutton = isElementPresent("CC_BTN");
			Assert.assertEquals(ccbutton, true,
					"ClosedCaption button is not present on player");
			logger.info("Verified the presence of ClosedCaption button ");
			if (isElementPresent("CC_PANEL_CLOSE")) {
				clickOnIndependentElement("CC_PANEL_CLOSE");
				clickOnIndependentElement("PLAY_BUTTON");
			}

		} catch (Exception e) {
			logger.error("closedCaption button is not  present\n");
		}
		checkClosedCaptionLanguages();
		logger.info("Verified the ClosedCaption button languages");
		Thread.sleep(1000);
		try {
			clickOnIndependentElement("CC_BTN");
		} catch (Exception e) {
			clickOnIndependentElement("MORE_OPTION_ICON");
			waitOnElement("CC_BTN", 60);
			clickOnIndependentElement("CC_BTN");
		}
		Thread.sleep(1000);

		closedCaptionMicroPanel();
		logger.info("Verified  ClosedCaption button Micropanel ");

		if (!(isElementPresent("CLOSED_CAPTION_PANEL"))) {
			clickOnIndependentElement("CC_BTN");
		}

		boolean ccpanel = isElementVisible("CLOSED_CAPTION_PANEL");
		Assert.assertEquals(ccpanel, true,
				"closedCaption languages panel is not present");
		Thread.sleep(1000);
		waitOnElement("CC_SWITCH", 60);

		clickOnIndependentElement("CC_SWITCH_CONTAINER");
		clickOnIndependentElement("CC_PANEL_CLOSE");

		logger.info("Verified ClosedCaption ");

		if (isElementPresent("PLAY_BUTTON"))
			clickOnIndependentElement("PLAY_BUTTON");
		Thread.sleep(2000);

		Assert.assertEquals(isElementPresent("ccmode_disabled"), true,
				"ClosedCaption is not disabled");
		Thread.sleep(2000);

		Assert.assertEquals(isElementPresent("ccmode_showing"), true,
				"ClosedCaption is not showing");
	}

	protected void closedCaptionMicroPanel() throws Exception {
		try {
			waitOnElement("CC_POPHOVER_HORIZONTAL", 10);
			boolean horizontal_CC_Option = isElementPresent("CC_POPHOVER_HORIZONTAL");
			logger.info(horizontal_CC_Option);
			if (horizontal_CC_Option) {
				waitOnElement("CC_SWITCH_CONTAINER_HORIZONTAL", 20);
				waitOnElement("CC_MORE_CAPTIONS", 10);
				waitOnElement("CC_CLOSE_BUTTON", 10);
				clickOnIndependentElement("CC_MORE_CAPTIONS");
				logger.info("Verified presence of closedCaptionMicroPanel ");
			}
		} catch (Exception e) {
			logger.error("Horizontal cc option is not present");
		}

	}

	protected void checkClosedCaptionLanguages() throws Exception {
		ArrayList<String> langlist = ((ArrayList<String>) (((JavascriptExecutor) driver)
				.executeScript("var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;"
						+ "{return attrb;}")));
		logger.info("Closed Caption Available Languages: " + langlist);
		for (int i = 0; i < langlist.size(); i++) {
			((JavascriptExecutor) driver)
					.executeScript("pp.setClosedCaptionsLanguage(\""
							+ langlist.get(i) + "\")");
			WebElement ccElement1 = (new WebDriverWait(driver, 60))
					.until(ExpectedConditions.presenceOfElementLocated(By
							.id("cclanguage_" + langlist.get(i))));
		}
	}
}
