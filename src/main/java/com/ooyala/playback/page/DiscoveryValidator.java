package com.ooyala.playback.page;

import static java.lang.Thread.sleep;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

public class DiscoveryValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(DiscoveryValidator.class);

	public DiscoveryValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("discovery");
		addElementToPageElements("play");
		addElementToPageElements("pause");

	}
	
	public boolean validateDiscoveryToaster() throws Exception{

		if (isElementPresent("PAUSE_BUTTON")) {
			clickOnIndependentElement("PAUSE_BUTTON");
		}
		return waitOnElement("DISCOVERY_TOASTER", 10000);
		
	}
	
	public boolean validateLeftRightButton() throws Exception{

		List<WebElement> count = getWebElementsList("DISCOVERY_IMG_WRAPPER");

		logger.info("Count Value :" + count.size());
		logger.info("Number of Discovery Videos " + count.size());

		boolean flagTrue = false;
		try {
			flagTrue = isElementVisible("RIGHT_BTN");
			logger.info("Is right button showing on Discovery Screen  "
					+ flagTrue);
		} catch (Exception e) {
			logger.info("Max videos are showing on Discovery screen");
			return false;
		}
		if (count.size() > 3 && flagTrue) {
			if (!clickOnIndependentElement("RIGHT_BTN"))
				return false;
			sleep(2000);
			if (!clickOnIndependentElement("LEFT_BTN"))
				return false;
			extentTest.log(LogStatus.PASS,
					"verified discovery left right button");
		}
		return true;
	}
	
	public boolean validateImageStyle(){
		if(!clickOnIndependentElement("IMAGE_STYLE")) return false;
        if(!waitOnElement(By.id("reportDiscoveryClick_1"), 60000)) return false;
		return true;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		if (validateDiscoveryToaster() && validateLeftRightButton()
				&& validateImageStyle()) {
			return waitOnElement(By.id("reportDiscoveryImpression_1"), 60000)
					&& waitOnElement(By.id("setEmbedCode_1"), 60000)
					&& waitOnElement(By.id("playbackReady_1"), 60000)
					&& waitOnElement(By.id("videoPreload_1"), 60000);
		}

//		loadingSpinner();
		return false;
	}

	public void verifyDiscoveryEnabled(String Onevent, boolean flag) { // TODO
		boolean discoverytray = isElementPresent("DISCOVERY_STYLE");
		boolean discoveryscreen = isElementPresent("CONTENT_SCREEN");

		logger.info("discovery screen is enabled " + Onevent + ": "
				+ discoveryscreen);
		logger.info("discovery Toaster is Shown " + Onevent + ": "
				+ discoverytray);
		Assert.assertEquals(discoveryscreen, flag);
		Assert.assertEquals(discoverytray, flag);

	}

	public boolean clickOnDiscoveryCloseButton(String element, int timeout) {
		return waitOnElement(element, timeout)
				&& clickOnIndependentElement(element);
	}
}
