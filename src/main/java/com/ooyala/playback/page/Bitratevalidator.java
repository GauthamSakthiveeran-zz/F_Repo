package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.testng.Assert.assertEquals;

/**
 * Created by soundarya on 11/17/16.
 */
public class Bitratevalidator extends BaseValidator {

    public static Logger logger = Logger.getLogger(Bitratevalidator.class);

    public Bitratevalidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("bitrate");

    }

    public void validate(String element, int timeout) throws Exception {
        boolean result;

        try {
            result = isElementPresent("bitrate");
        } catch (Exception e) {
            clickOnIndependentElement("moreOptionItem");
            sleep(1000);
            result = isElementPresent("bitrate");
            clickOnIndependentElement("bitrate");

        }
        /* Todo change birtate selection and verify
        List<WebElement> bitrateSelection = getWebElementsList("bitrateSelection");
*/
    }
}
