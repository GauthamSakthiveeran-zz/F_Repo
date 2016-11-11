package com.ooyala.playback.page;

import static java.lang.System.out;
import static java.lang.Thread.sleep;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class DiscoveryValidator extends BaseValidator {

	
	public static Logger logger = Logger.getLogger(DiscoveryValidator.class);
	
	public DiscoveryValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("discovery");
	}

	public void validate(String element, int timeout) throws Exception {

		clickOnIndependentElement("PAUSE_BUTTON");
		try {
			waitOnElement("disocoveryToaster", 60);
		} catch (Exception e) {
			if (isElementVisible("playingScreen")) {
				clickOnElement("pauseButton");
			} else {
				clickOnElement("playButton");
				sleep(5000);
				clickOnElement("pauseButton");
			}
			waitOnElement("disocoveryToaster", 60);
		}

		List<WebElement> count = getWebElementsList("discveryImgWrapper");
		out.println("Count Value :" + count.size());

		boolean flagTrue = false;
		try {
			flagTrue = isElementVisible("rightBtn");
			out.println("Is right button showing on Discovery Screen  "
					+ flagTrue);
		} catch (Exception e) {
			out.println("Max videos are showing on Discovery screen");
		}
		if (count.size() > 3 && flagTrue) {
			clickOnIndependentElement("rightBtn");
			sleep(2000);
			clickOnIndependentElement("leftBtn");
			// test.log(INFO, "verified discovery left right button");
		}

		clickOnIndependentElement("imageStyle");
		try {
			waitOnElement("reportDiscoveryClick_1", 60);
		} catch (Exception e) {
			clickOnHiddenElement("imageStyle");
			waitOnElement("reportDiscoveryClick_1", 60);
		}
		waitOnElement("reportDiscoveryImpression_1", 60);
		waitOnElement("setEmbedCode_1", 60);
		waitOnElement("playbackReady_1", 60);
		waitOnElement("videoPreload_1", 60);

	}
}
