package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * Created by soundarya on 11/16/16.
 */
public class AspectRatioValidator extends BaseValidator {

    public static Logger Log = Logger.getLogger(AspectRatioValidator.class);


    public AspectRatioValidator(WebDriver webDriver){
        super(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    public void validate(String element,int timeout)throws Exception {
        if (isElementVisible(element)) {
            int width = Integer.parseInt(driver.findElement(By.id(element)).getAttribute("myw"));
            int height = Integer.parseInt(driver.findElement(By.id(element)).getAttribute("myh"));
            int diff = width / 4;
            int expectedHeight = width - diff;
            Assert.assertEquals(expectedHeight, height, "Video is in 4:3 ratio");
            logger.info(" verified Aspect Ratio 4:3");
        } else {
            logger.info("Aspect ratio element not present");
        }
    }

}
