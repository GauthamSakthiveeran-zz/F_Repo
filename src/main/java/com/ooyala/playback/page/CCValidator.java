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

import com.ooyala.facile.page.WebPage;

/**
 * Created by soundarya on 11/3/16.
 */
public class CCValidator extends BaseValidator {

	public static Logger logger = Logger.getLogger(CCValidator.class);
	
	public CCValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("cc");
	}

	public void validate(String element, int timeout) throws Exception {
		try {
			try {
				waitOnElement("ccBtn", 60);
			} catch (Exception e) {
				if (getPlatform().equalsIgnoreCase("Android"))
					clickOnIndependentElement("moreOptionIcon");
				else {
					clickOnElement("moreOptionIcon");
				}
				waitOnElement("ccBtn", 60);
			}
			boolean ccbutton = isElementVisible("ccBtn");
			Assert.assertEquals(ccbutton, true,
					"ClosedCaption button is not present on player");
			boolean ccpanel = isElementVisible("ccPanelClose");
			if (ccpanel) {
				clickOnElement("ccPanelClose");
				clickOnElement("playButton");
			}

		} catch (Exception e) {
			logger.info("closedCaption button is not  present\n");
		}
		Object ccobj = ((JavascriptExecutor) driver)
				.executeScript("var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;"
						+ "{return attrb;}");
		@SuppressWarnings("unchecked")
		ArrayList<String> langlist = ((ArrayList<String>) ccobj);
		logger.info("Closed Caption Available Languages: " + langlist);
		for (int i = 0; i < langlist.size(); i++) {
			((JavascriptExecutor) driver)
					.executeScript("pp.setClosedCaptionsLanguage(\""
							+ langlist.get(i) + "\")");
			WebElement ccElement1 = (new WebDriverWait(driver, 60))
					.until(ExpectedConditions.presenceOfElementLocated(By
							.id("cclanguage_" + langlist.get(i))));
		}
		Thread.sleep(1000);
		try {
			clickOnHiddenElement("ccBtn");
		} catch (Exception e) {
			if (getPlatform().equalsIgnoreCase("Android")) {
				clickOnElement("moreOptionIcon");
				waitOnElement("ccBtn", 60);
				clickOnElement("ccBtn");
			} else {
				clickOnElement("moreOptionIcon");
				waitOnElement("ccBtn", 60);
				clickOnElement("ccBtn");
			}
		}
		Thread.sleep(1000);

		try {
			waitOnElement("ccPopoverHorizontal", 10);
			boolean horizontal_CC_Option = isElementVisible("ccPopoverHorizontal");
			logger.info(horizontal_CC_Option);
			if (horizontal_CC_Option) {
				waitOnElement("ccSwitchContainerHorizontal", 20);
				waitOnElement("ccMoreCaptions", 10);
				waitOnElement("ccCloseButton", 10);
				clickOnElement("ccMoreCaptions");
			}
		} catch (Exception e) {

			logger.info("Horizontal cc option is not present");
		}

		boolean ccpanelshown = isElementVisible("closedCaptionPanel");
		if (!ccpanelshown) {
			try {
				clickOnElement("ccBtn");
			} catch (Exception e) {
				clickOnHiddenElement("ccBtn");
			}

		}

		boolean ccpanel = isElementVisible("closedCaptionPanel");
		Assert.assertEquals(ccpanel, true,
				"closedCaption languages panel is not present");
		Thread.sleep(1000);
		waitOnElement("ccsSwitch", 60);

		clickOnElement("ccSwitchContainer");

		clickOnElement("ccPanelClose");
		boolean ispause = isElementVisible("playButton");
		if (ispause)
			clickOnElement("playButton");
		Thread.sleep(2000);
		boolean isccshowing1 = isElementVisible("ccmode_disabled");
		Assert.assertEquals(isccshowing1, true, "ClosedCaption is not disabled");
		Thread.sleep(2000);
		boolean isccshowing = isElementVisible("ccmode_showing");
		Assert.assertEquals(isccshowing, true, "ClosedCaption is not showing");
	}
}
