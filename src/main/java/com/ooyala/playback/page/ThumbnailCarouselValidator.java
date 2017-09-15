package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by Snehal on 13/01/2017.
 */
public class ThumbnailCarouselValidator extends PlayBackPage implements
        PlaybackValidator {
    private static Logger logger = Logger.getLogger(ThumbnailCarouselValidator.class);

    public ThumbnailCarouselValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("play");
        addElementToPageElements("controlbar");
        addElementToPageElements("sharetab");
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {

        if (!waitOnElement("SCRUBBER_BAR", 10000)) {
            return false;
        }
        if (!waitOnElement("SHARE_BTN", 10000)) {
            return false;
        }

        WebElement scrubberBarElement = getWebElement("SCRUBBER_BAR");
        WebElement shareButtonElement = getWebElement("SHARE_BTN");

        // Dragging the element to another element using javascript for safari browser
        if (getBrowser().equalsIgnoreCase("safari")) {
            dragdrop(scrubberBarElement, shareButtonElement);
        } else {
            try {
                Actions action = new Actions(driver);
                action.clickAndHold(scrubberBarElement).moveToElement(shareButtonElement).build().perform(); // click and hold the element
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (!((driver.findElement(By.cssSelector("div.oo-thumbnail-carousel-image")).isDisplayed()) && (driver.findElement(By.cssSelector("div.oo-thumbnail-carousel-center-image")).isDisplayed()))) {
            logger.error("Failed : Thumbnail corousel image is not showing");
            return false;
        }
        return true;
    }

}
