package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import static java.lang.Thread.sleep;

public class FullScreenValidator extends BaseValidator {

    public static Logger logger = Logger.getLogger(FullScreenValidator.class);

    public FullScreenValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("fullscreen");
        addElementToPageElements("pause");

    }

    public void validate(String element, int timeout) throws Exception {
        WebElement player = getWebElement("OOPLAYER");
        Actions action = new Actions(driver);
        action.moveToElement(player).perform();
        waitOnElement("FULLSCREEN_BTN", 60);

        if (getPlatform().equalsIgnoreCase("Android")) {
            clickOnIndependentElement("FULLSCREEN_BTN");
        } else {
            clickOnIndependentElement("PAUSE_BUTTON");
            clickOnIndependentElement("FULLSCREEN_BTN");
        }

        if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("firefox") || getBrowser().equalsIgnoreCase("internet explorer") || getPlatform().equalsIgnoreCase("Android"))) {
            waitOnElement(By.id("fullscreenChangedtrue"), 60);
            logger.info("Changed into Fullscreen");
        }
        Thread.sleep(3000);
        clickOnIndependentElement("PAUSE_BUTTON");
        sleep(2000);

        clickOnIndependentElement("NORMAL_SCREEN");

        // PBW-5165 we are not verifying fullscreen change event for safari and firefox browser as fullscreen is not working in safari in automation
        if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("firefox") || getBrowser().equalsIgnoreCase("internet explorer") || getPlatform().equalsIgnoreCase("Android"))) {
            waitOnElement(By.id("fullscreenChangedfalse"), 60);
            logger.info("Video changed to normal size");
        }
    }
}
