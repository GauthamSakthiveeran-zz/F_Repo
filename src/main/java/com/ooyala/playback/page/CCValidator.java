package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

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
		addElementToPageElements("controlbar");
		addElementToPageElements("pause");
	}

	private boolean verifyCloseClosedCaptionPanel() throws Exception {
		switchToControlBar();
		if (!isElementPresent("CC_PANEL_CLOSE")){
			extentTest.log(LogStatus.FAIL, "CC_PANEL_CLOSE not present.");
			return false;
		}
		if (!clickOnIndependentElement("CC_PANEL_CLOSE")){
			extentTest.log(LogStatus.FAIL, "Couldn't click on CC_PANEL_CLOSE");
			return false;
		}
		extentTest.log(LogStatus.PASS, "Verified closed caption panel close");
		return true;
	}

	private boolean checkClosedCaptionButton() {

		try {
			Thread.sleep(1000);
			switchToControlBar();
			
			if (!isElementPresent("CC_BTN")){
				if (!waitOnElement("CC_BTN", 6000)){
					if (!clickOnIndependentElement("MORE_OPTION_ICON"))
						return false;
					if (!waitOnElement("CC_BTN", 6000))
						return false;
				}
			}
			if (!clickOnIndependentElement("CC_BTN"))
				return false;
			Thread.sleep(1000);

			extentTest.log(LogStatus.PASS, "Verified closed caption button");
			return true;

		} catch (Exception e) {
			extentTest.log(
					LogStatus.FAIL,
					"closedCaption button is not  present."
							+ e.getLocalizedMessage());
			return false;
		}

	}

	private boolean validateClosedCaptionPanel() throws Exception {
		switchToControlBar();
		if (!isElementPresent("CLOSED_CAPTION_PANEL")) {
			if (!clickOnIndependentElement("CC_BTN")){
				return false;
			}
		}

		if (isElementPresent("CLOSED_CAPTION_PANEL")) {
			extentTest.log(LogStatus.PASS, "Verified closed caption panel");
			return true;
		}
		extentTest.log(LogStatus.FAIL, "CLOSED_CAPTION_PANEL is not present");
		return false;

	}

	private boolean validateSwitchContainer() {
		try{
			if(getBrowser().equalsIgnoreCase("MicrosoftEdge")){
				WebElement element = getWebElement("CC_SWITCH");
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
				}
		}catch (Exception e){
			logger.error("Error in focus on element cc switch");
		}

		if (waitOnElement("CC_SWITCH", 30000)
				&& clickOnIndependentElement("CC_SWITCH_CONTAINER")) {
			extentTest.log(LogStatus.PASS,
					"Verified closed caption panel switch container");
			return true;
		}
		return false;
	}

	private void switchToControlBar() throws Exception {
		if (isElementPresent("HIDDEN_CONTROL_BAR")) {
			logger.info("hovering mouse over the player");
			Thread.sleep(2000);
			moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
		} else if (isElementPresent("CONTROL_BAR")) {
				moveElement(getWebElement("CONTROL_BAR"));
		}
	}

	public boolean validate(String element, int timeout) throws Exception {

		boolean flag = checkClosedCaptionButton() && verifyCloseClosedCaptionPanel() && closedCaptionMicroPanel()
				&& validateClosedCaptionPanel() && validateSwitchContainer() && verifyCloseClosedCaptionPanel()
				&& checkClosedCaptionLanguages() && validateClosedCaptionCloseButton();

		if (flag) {
			if (clickOnIndependentElement("PAUSE_BUTTON")) {
				flag = flag && waitOnElement(By.id("ccmode_disabled"), 20000);
			} else {
				flag = false;
			}
		}
		return flag;

	}
	
	private boolean validateClosedCaptionCloseButton() throws Exception {
		switchToControlBar();
		if (!isElementPresent("CC_CLOSE_BUTTON")) {
			if (!clickOnIndependentElement("CC_BTN")){
				return false;
			}
		}

		if (isElementPresent("CC_CLOSE_BUTTON")) {
			if(clickOnIndependentElement("CC_CLOSE_BUTTON")){
				extentTest.log(LogStatus.PASS, "Verified CC_CLOSE_BUTTON");
				return true;
			}
		}
		extentTest.log(LogStatus.FAIL, "CC_CLOSE_BUTTON is not present");
		return false;

	}

	protected boolean closedCaptionMicroPanel() throws Exception {
		try {
			switchToControlBar();

			if (!clickOnIndependentElement("CC_BTN"))
				return false;

			if (!getWebElement("oo-responsive").getAttribute("className").equalsIgnoreCase("oo-xsmall")) {
				if (!waitOnElement("CC_POPHOVER_HORIZONTAL", 6000))
					return false;
				boolean horizontal_CC_Option = isElementPresent("CC_POPHOVER_HORIZONTAL");

				if (horizontal_CC_Option) {
					if (isElementPresent("CC_SWITCH_CONTAINER_HORIZONTAL") && isElementPresent("CC_MORE_CAPTIONS")
							&& clickOnIndependentElement("CC_MORE_CAPTIONS")) {
						return true;
					} else {
						extentTest.log(LogStatus.FAIL, "Verification of cc pop over horizontal elements failed.");
						return false;
					}

				} 
			} else {
				return true;
			}
			extentTest.log(LogStatus.FAIL, "CC_POPHOVER_HORIZONTAL is not present!");
			return false;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL,
					"Horizontal cc option is not present");
			;
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	protected boolean checkClosedCaptionLanguages() throws Exception {
		ArrayList<String> langlist = ((ArrayList<String>) (((JavascriptExecutor) driver)
				.executeScript("var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;"
						+ "{return attrb;}")));
		boolean flag = true;
		if (langlist == null || langlist.size() == 0) {
			extentTest.log(LogStatus.FAIL, "langList is null");
			return false;
		}
		logger.info("Closed Caption Available Languages: " + langlist);
		for (int i = 0; i < langlist.size(); i++) {
			((JavascriptExecutor) driver)
					.executeScript("pp.setClosedCaptionsLanguage(\""
							+ langlist.get(i) + "\")");
			WebElement ccElement1 = (new WebDriverWait(driver, 60000))
					.until(ExpectedConditions.presenceOfElementLocated(By
							.id("cclanguage_" + langlist.get(i))));
			if (ccElement1 == null) {
				flag = flag && false;
			}

		}
		if (flag)
			extentTest.log(LogStatus.PASS,
					"Verified closed caption panel languages");
		else
			extentTest.log(LogStatus.FAIL,
					"Closed caption panel languages Failed.");
		return flag;
	}
}
