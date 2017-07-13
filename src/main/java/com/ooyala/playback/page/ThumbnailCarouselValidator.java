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

    public void dragdrop(WebElement LocatorFrom, WebElement LocatorTo) {
        String xto = Integer.toString(LocatorTo.getLocation().x);
        String yto = Integer.toString(LocatorTo.getLocation().y);
        driver.executeScript("function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; " +
                        "simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]);",
                LocatorFrom, xto, yto);
    }
}
