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
        WebElement player = getWebElement("ooplayer");
        Actions action = new Actions(driver);
        action.moveToElement(player).perform();
        waitOnElement("fullScreenBtn", 60);

        if (getPlatform().equalsIgnoreCase("Android")) {
            clickOnIndependentElement("fullScreenBtn");
        } else {
            clickOnIndependentElement("pauseButton");
            clickOnIndependentElement("fullScreenBtn");
        }

        if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("firefox") || getBrowser().equalsIgnoreCase("internet explorer") || getPlatform().equalsIgnoreCase("Android"))) {
            waitOnElement(By.id("fullscreenChangedtrue"), 60);
            logger.info("Changed into Fullscreen");
        }
        Thread.sleep(3000);
        clickOnIndependentElement("pauseButton");
        sleep(2000);

        clickOnIndependentElement("normalScreen");

        // PBW-5165 we are not verifying fullscreen change event for safari and firefox browser as fullscreen is not working in safari in automation
        if (!(getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("firefox") || getBrowser().equalsIgnoreCase("internet explorer") || getPlatform().equalsIgnoreCase("Android"))) {
            waitOnElement(By.id("fullscreenChangedfalse"), 60);
            logger.info("Video changed to normal size");
        }
    }
}
