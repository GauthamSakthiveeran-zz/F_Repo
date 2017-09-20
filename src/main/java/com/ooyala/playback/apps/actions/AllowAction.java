package com.ooyala.playback.apps.actions;

import com.ooyala.playback.apps.PlaybackApps;
import io.appium.java_client.AppiumDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

public class AllowAction extends PlaybackApps implements Actions {

    private static Logger logger = Logger.getLogger(PlayAction.class);

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
}