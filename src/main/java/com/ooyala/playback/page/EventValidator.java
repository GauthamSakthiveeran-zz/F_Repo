package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/14/16.
 */
public class EventValidator extends BaseValidator {


    public static Logger logger = Logger.getLogger(DiscoveryValidator.class);

    public EventValidator(WebDriver driver) {
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
        waitOnElement(element,timeout);
    }

}
