package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/16/16.
 */
public class ControlBarValidator extends BaseValidator {

    public static Logger logger = Logger.getLogger(ControlBarValidator.class);

    public ControlBarValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("fullscreen");
        addElementToPageElements("pause");

    }

    public void validate(String element, int timeout) throws Exception {


            ArrayList<String> controlBarElement = new ArrayList<String>();
            controlBarElement.addAll(Arrays.asList("playhead", "playPause", "volumeBtn", "fullScreenBtn1", "shareBtn", "discoveryBtn", "timeDuration"));

            boolean iscontrolshown = isElementVisible("controlBar");

            if(!iscontrolshown) {
                System.out.println("Control bar is hiden hence mouse hovering on it");
               // Actions act = new Actions(driver);
                //act.moveToElement(driver.findElement(locators.getobjectLocator("controlBar"))).build().perform();
            }
            try
            {
                for(String icon: controlBarElement){
                    waitOnElement(icon, 60);
                }
                boolean ismoreoption = isElementVisible("moreOptionItem");
                if(ismoreoption) {
                    clickOnIndependentElement("moreOptionItem");
                    waitOnElement("discoveryBtn", 60);
                   waitOnElement("qualityBtn", 60);
                 clickOnIndependentElement("ccPanelClose");
                }
            } catch (Exception e)
            {
                waitOnElement("playPause", 60);
                waitOnElement("volumeBtn", 60);
                waitOnElement("fullScreenBtn1", 60);
                // seleniumActions.waitForElement("ooyalaLogo", 60);
            }

           /* String ooyalalogo = webDriver.findElement(locators.getobjectLocator("ooyalaLogo")).findElement(By.tagName("img")).getAttribute("src");
            if(!(ooyalalogo.contains(".png") || ooyalalogo.contains(".svg") || ooyalalogo.contains(".jpg") || ooyalalogo.contains(".gif")))
                Assert.assertTrue(ooyalalogo.contains(".png"), "Ooyala branding Logo is not present");*/

        }
}

