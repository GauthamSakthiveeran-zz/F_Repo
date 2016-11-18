package com.ooyala.playback.page;

import static java.lang.System.out;
import static java.lang.Thread.sleep;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

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
            waitOnElement("discoveryToaster", 60);
        } catch (Exception e) {
            if (isElementPresent("playingScreen")) {
                clickOnIndependentElement("pauseButton");
            } else {
                clickOnIndependentElement("playButton");
                sleep(5000);
                clickOnIndependentElement("pauseButton");
            }
            waitOnElement("discoveryToaster", 60);
            logger.info("Discovery Toaster present");
        }

        getWebElementsList("discoveryImgWrapper");
        List<WebElement> count = getWebElementsList("discoveryImgWrapper");
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
        }
        clickOnIndependentElement("imageStyle");
        try {
            waitOnElement(By.id("reportDiscoveryClick_1"), 60);
        } catch (Exception e) {
            clickOnIndependentElement("imageStyle");
            waitOnElement(By.id("reportDiscoveryClick_1"), 60);
        }
        waitOnElement(By.id("reportDiscoveryImpression_1"), 60);
        waitOnElement(By.id("setEmbedCode_1"), 60);
        waitOnElement(By.id("playbackReady_1"), 60);
        waitOnElement(By.id("videoPreload_1"), 60);
        logger.info("Discovery Video clicked and new video loaded successfully");

    }
    public void verifyDiscoveryEnabled(String Onevent, boolean flag)
    {
        boolean discoverytray = isElementPresent("discoveryStyle");
        boolean discoveryscreen = isElementPresent("contentScreen");
        logger.info("discvoery screen is enabled " + Onevent + ": " + discoveryscreen);
        logger.info("discvoery Toaster is Shown " + Onevent + ": " + discoverytray);
        Assert.assertEquals(discoveryscreen, flag);
        Assert.assertEquals(discoverytray,flag);

    }
}
