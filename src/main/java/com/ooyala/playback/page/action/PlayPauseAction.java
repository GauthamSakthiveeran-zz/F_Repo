package com.ooyala.playback.page.action;

import com.ooyala.playback.page.PlayBackPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static org.testng.Assert.assertEquals;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlayPauseAction extends PlayBackPage implements PlayerAction {

    public PlayPauseAction(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("play");
        addElementToPageElements("pause");

    }

    @Override
    public void startAction() {
        boolean ispause = isElementPresent("playButton");
        logger.info("Video Paused" + ispause);
        if(ispause)
            try {
                waitOnElement("playButton", 10);
                clickOnIndependentElement("playButton");
            } catch (Exception e) {
                waitOnElement("pasueScreen",10);
                clickOnIndependentElement("pasueScreen");
            }

    }
}