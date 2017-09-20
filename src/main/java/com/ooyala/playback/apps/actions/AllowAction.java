package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.facile.page.FacileWebElement;
import com.ooyala.playback.apps.PlaybackApps;

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
        FacileWebElement anElement = new FacileWebElement((FacileWebElement)this.pageElements.get(element));
        WebElement allowButton = this.getWebElementFromFacileWebElement(anElement);
        if(allowButton.isDisplayed()) {
            logger.info("Pop-up box is displaying need to give permission");
        }
        else {
            logger.info("PermissionAlready Given..");
            return true;
        }
        return true;
    }
}