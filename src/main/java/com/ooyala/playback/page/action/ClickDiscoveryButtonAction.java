package com.ooyala.playback.page.action;

import com.ooyala.playback.page.PlayBackPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static java.lang.System.out;
import static java.lang.Thread.sleep;
import static org.testng.Assert.assertEquals;

/**
 * Created by soundarya on 11/17/16.
 */
public class ClickDiscoveryButtonAction extends PlayBackPage implements PlayerAction {

    public ClickDiscoveryButtonAction(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("discovery");
        addElementToPageElements("controlbar");

    }

    @Override
    public void startAction() throws Exception{
        try{
            clickOnIndependentElement("discoveryBtn");
            sleep(2000);
            if (!isElementPresent("contentScreen")) {
                clickOnIndependentElement("discoveryBtn");
            }
        } catch (Exception e) {
            out.println("exception " + e.getMessage());
            clickOnIndependentElement("moreOptionIcon");
            Thread.sleep(2000);
            clickOnIndependentElement("discoveryBtn");
        }
    }
}
