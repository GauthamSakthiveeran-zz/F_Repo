package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

/**
 * Created by snehal on 28/11/16.
 */
public class DifferentElementValidator extends PlayBackPage implements PlaybackValidator {
    public static Logger logger = Logger.getLogger(DestroyValidator.class);

    public DifferentElementValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("element");
    }

    @Override
    public void validate(String element, int timeout) throws Exception {
        List<WebElement> ele=getWebElementsList("videoPath");
        String element1_id = ele.get(0).getAttribute("id");
        String element2_id = ele.get(1).getAttribute("id");
        Assert.assertNotEquals(element1_id, element2_id, "Both should not have same id");
        logger.info( "Two different elements created for ad and main video");
    }
}
