package com.ooyala.playback.page;

import com.ooyala.playback.page.BaseValidator;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;


/**
 * Created by soundarya on 11/1/16.
 */
public class UpNextValidator  extends BaseValidator {

    public static Logger Log = Logger.getLogger(UpNextValidator.class);


    public UpNextValidator(WebDriver webDriver){
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("upnext");
    }

    public void validate(String element,int timeout)throws Exception {
        waitOnElement(element, 60);
        waitOnElement("CONTENT_METADATA", 60);
        clickOnIndependentElement("UPNEXT_CLOSE_BTN");
        Log.info("Verified upNextPanel and up next content ");
        Log.info("verified up next close button");

    }
}
