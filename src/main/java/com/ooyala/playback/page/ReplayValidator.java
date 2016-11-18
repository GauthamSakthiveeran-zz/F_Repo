package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/17/16.
 */
public class ReplayValidator extends BaseValidator {

    public static Logger Log = Logger.getLogger(ReplayValidator.class);


    public ReplayValidator(WebDriver webDriver){
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("replay");
    }

    public void validate(String element,int timeout)throws Exception {

       waitOnElement("END_SCREEN", 60);
       waitOnElement("REPLAY", 60);
       clickOnIndependentElement("REPLAY");

        waitOnElement(By.id(element), timeout);
    }
}