package com.ooyala.playback.page.action;

import com.ooyala.playback.page.PlayBackPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static org.testng.Assert.assertEquals;

/**
 * Created by soundarya on 11/16/16.
 */
public class LiveAction extends PlayBackPage implements PlayerAction {

    public LiveAction(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("live");

    }

    @Override
    public void startAction() {

        boolean live = isElementPresent("LIVE");

        assertEquals(live, false, "Live text is not showing on the video or video is not live");
    }
}