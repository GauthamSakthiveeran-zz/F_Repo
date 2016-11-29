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

import com.relevantcodes.extentreports.LogStatus;

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
	
	private boolean verifyCloseClosedCaptionPanel(){
		if(!isElementPresent("CC_PANEL_CLOSE")) return false;
		if(!clickOnIndependentElement("CC_PANEL_CLOSE")) return false;
		extentTest.log(LogStatus.PASS,"Verified closed caption panel close");
		return true;
	}
	
	private boolean checkClosedCaptionButton(){
		
		try {
			Thread.sleep(1000);
			try {
				if(!waitOnElement("CC_BTN", 60)) return false;
			} catch (Exception e) {
				if(!clickOnIndependentElement("MORE_OPTION_ICON")) return false;
				if(!waitOnElement("CC_BTN", 60)) return false;
			}
			
			if(!isElementPresent("CC_BTN")) return false;
			if(!clickOnIndependentElement("CC_BTN")) return false;
			Thread.sleep(1000);

			extentTest.log(LogStatus.PASS,"Verified closed caption button");
			return true;
			
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "closedCaption button is not  present." + e.getLocalizedMessage());
			return false;
		}
		
	}
	
	private boolean validateClosedCaptionPanel(){
		if (!(isElementPresent("CLOSED_CAPTION_PANEL"))) {
			if(!clickOnIndependentElement("CC_BTN")) return false;
		}

		if(isElementPresent("CLOSED_CAPTION_PANEL")){
			extentTest.log(LogStatus.PASS,"Verified closed caption panel panel");
			return true;
		}
		return false;
		
	}
	
	private boolean validateSwitchContainer(){
		if(waitOnElement("CC_SWITCH", 60) && clickOnIndependentElement("CC_SWITCH_CONTAINER")){
			extentTest.log(LogStatus.PASS,"Verified closed caption panel switch container");
			return true;
		}
		return false;
	}

	public boolean validate(String element, int timeout) throws Exception {
		return 
		checkClosedCaptionButton()
		&& verifyCloseClosedCaptionPanel()
		&& checkClosedCaptionLanguages()
		&& checkClosedCaptionButton()
		&& closedCaptionMicroPanel()
		&& validateClosedCaptionPanel()
		&& validateSwitchContainer()
		&& verifyCloseClosedCaptionPanel();

		/*
		 * Todo fix this if (isElementPresent("PAUSE_BUTTON"))
		 * clickOnIndependentElement("PAUSE_BUTTON"); Thread.sleep(2000);
		 * 
		 * Assert.assertEquals(isElementPresent(By.id("ccmode_disabled")), true,
		 * "ClosedCaption is not disabled"); Thread.sleep(2000);
		 * 
		 * Assert.assertEquals(isElementPresent(By.id("ccmode_showing")), true,
		 * "ClosedCaption is not showing");
		 */
	}

	protected boolean closedCaptionMicroPanel() throws Exception {
		try {
			if(!waitOnElement("CC_POPHOVER_HORIZONTAL", 10)) return false;
			boolean horizontal_CC_Option = isElementPresent("CC_POPHOVER_HORIZONTAL");

			if (horizontal_CC_Option) {
				return
				waitOnElement("CC_SWITCH_CONTAINER_HORIZONTAL", 20)
				&& waitOnElement("CC_MORE_CAPTIONS", 10) 
				&& waitOnElement("CC_CLOSE_BUTTON", 10)
				&& clickOnIndependentElement("CC_MORE_CAPTIONS");
			}
			return false;
		} catch (Exception e) {
			extentTest.log(LogStatus.FAIL, "Horizontal cc option is not present");;
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	protected boolean checkClosedCaptionLanguages() throws Exception {
		ArrayList<String> langlist = ((ArrayList<String>) (((JavascriptExecutor) driver)
				.executeScript("var attrb = pp.getCurrentItemClosedCaptionsLanguages().languages;"
						+ "{return attrb;}")));
		boolean flag = true;
		if(langlist == null || langlist.size()==0) {
			extentTest.log(LogStatus.FAIL, "langList is null");
			return false;
		}
		logger.info("Closed Caption Available Languages: " + langlist);
		for (int i = 0; i < langlist.size(); i++) {
			((JavascriptExecutor) driver)
					.executeScript("pp.setClosedCaptionsLanguage(\""
							+ langlist.get(i) + "\")");
			WebElement ccElement1 = (new WebDriverWait(driver, 60))
					.until(ExpectedConditions.presenceOfElementLocated(By
							.id("cclanguage_" + langlist.get(i))));
			if(ccElement1==null){
				flag = flag && false;
			}
			
		}
		if(flag)
			extentTest.log(LogStatus.PASS,"Verified closed caption panel languages");
		else
			extentTest.log(LogStatus.FAIL,"Closed caption panel languages Failed.");
		return flag;
	}
}
