package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by suraj on 5/18/17.
 */
public class SafariValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(SafariValidator.class);

    public SafariValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("discovery");
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("sharetab");
        addElementToPageElements("fullscreen");
    }

    public boolean validate(String element, int timeout) throws Exception {
        try {
            WebElement element1 = getWebElement(element);
            driver.executeScript("arguments[0].click()", element1);
        } catch (Exception e) {
            logger.error("unable to click on the element using javascript"+e.getMessage());
            return false;
        }
        return true;
    }
}
