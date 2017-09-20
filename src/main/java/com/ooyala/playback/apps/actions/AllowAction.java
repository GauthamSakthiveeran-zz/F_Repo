package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

public class AllowAction extends PlaybackApps implements Actions {

    private static Logger logger = Logger.getLogger(AllowAction.class);

    public AllowAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("permission");
    }


    @Override
    public boolean startAction(String element ) throws Exception {
        if (!isAllowed(element))
            return clickOnIndependentElement(element);
        return true;
    }
    
    private boolean isAllowed(String element) {
        WebElement allowButton = getWebElement(element);
        if(allowButton.isDisplayed()) {
            logger.info("Pop-up box is displaying need to give permission");
            extentTest.log(LogStatus.INFO, "Pop-up box is displaying need to give permission");
        }
        else {
            logger.info("PermissionAlready Given..");
            extentTest.log(LogStatus.INFO, "PermissionAlready Given..");
            return true;
        }
        return true;
    }
}