package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * Created by soundarya on 11/16/16.
 */

public class AspectRatioValidator extends PlayBackPage implements
        PlaybackValidator {

    public static Logger Log = Logger.getLogger(AspectRatioValidator.class);


    public AspectRatioValidator(WebDriver webDriver){
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("aspectratio");
    }

    public void validate(String element,int timeout)throws Exception {
        if (isElementPresent(By.id(element))) {
            //dynamic element - long time for iselementPresent
            int width = Integer.parseInt(getWebElement(element).getAttribute("width"));
            int height = Integer.parseInt(getWebElement(element).getAttribute("height"));
            int diff = width / 4;
            int expectedHeight = width - diff;
            Assert.assertEquals(expectedHeight, height, "Video is in 4:3 ratio");
            logger.info(" verified Aspect Ratio 4:3");
        } else {
            logger.info("Aspect ratio element not present");
        }
    }

    public void verticalVideoValidate(String element,int timeout)throws Exception {
        if (isElementPresent(By.id(element))) {
            int width = Integer.parseInt(getWebElement(element).getAttribute("width"));
            int height = Integer.parseInt(getWebElement(element).getAttribute("height"));
            Assert.assertEquals(width, 320,"Width Matches");
            Assert.assertEquals(height, 568, "Heigth Matches");
            logger.info(" verified VerticalVideo");
        } else {
            logger.info("Aspect ratio element not present");
        }
    }

}
