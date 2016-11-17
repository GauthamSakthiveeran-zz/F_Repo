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
        addElementToPageElements("play");
        addElementToPageElements("pause");

	}

	public void validate(String element, int timeout) throws Exception {

        clickOnIndependentElement("pauseButton");
        try {
            waitOnElement("disocoveryToaster", 60);
        } catch (Exception e) {
            if (isElementVisible("playingScreen")) {
                clickOnIndependentElement("pauseButton");
            } else {
                clickOnIndependentElement("playButton");
                sleep(5000);
                clickOnIndependentElement("pauseButton");
            }
            waitOnElement("disocoveryToaster", 60);
            logger.info("Discovery Toaster present");
        }


        /*List<WebElement> count = driver.findElements(locators.getobjectLocator("discveryImgWrapper"));
        out.println("Count Value :" + count.size());
        logger.info("Number of Discovery Videos " +count.size());

        boolean flagTrue = false;
        try {
            flagTrue =isElementVisible("rightBtn");
            logger.info("Is right button showing on Discovery Screen  " + flagTrue);
        } catch (Exception e) {
            out.println("Max videos are showing on Discovery screen");
        }
        if (count.size() > 3 && flagTrue) {
            clickOnIndependentElement("rightBtn");
            sleep(2000);
            clickOnIndependentElement("leftBtn");
            logger.info("verified discovery left right button");
        }*/
        clickOnIndependentElement("imageStyle");
        try {
            waitOnElement("reportDiscoveryClick_1", 60);
        } catch (Exception e) {
            clickOnIndependentElement("imageStyle");
            waitOnElement("reportDiscoveryClick_1", 60);
        }
        waitOnElement("reportDiscoveryImpression_1", 60);
        waitOnElement("setEmbedCode_1", 60);
        waitOnElement("playbackReady_1", 60);
        waitOnElement("videoPreload_1", 60);
        logger.info("Discovery Video clicked and new video loaded successfully");

    }
}
