package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/22/16.
 */
public class ThumbnailValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(ThumbnailValidator.class);

    public ThumbnailValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("controlbar");

    }

    public void validate(String element, int timeout) throws Exception {
        boolean result;

        Actions action = new Actions(driver);

        WebElement element1 = getWebElement("SCRUBBER_BAR");
        action.moveToElement(element1).build().perform();
        waitOnElement("THUMBNAIL_CONTAINER",60);

    }
}


