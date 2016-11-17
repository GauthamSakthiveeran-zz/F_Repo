package com.ooyala.playback.page.action;

import com.ooyala.playback.page.PlayBackPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by soundarya on 11/16/16.
 */
public class AutoplayAction extends PlayBackPage implements PlayerAction {

    public AutoplayAction(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("play");
    }

    @Override
    public void startAction() {
        Boolean autoplay = false;
        try {
            autoplay = Boolean.parseBoolean(((JavascriptExecutor) driver).executeScript("return pp.parameters.autoplay").toString());
        } catch (Exception e) {
            System.out.print("Autoplay not set for this video");
        }
        if (!autoplay) {
            waitOnElement("playButton", 60);
            clickOnIndependentElement("playButton");
        }
    }
}
