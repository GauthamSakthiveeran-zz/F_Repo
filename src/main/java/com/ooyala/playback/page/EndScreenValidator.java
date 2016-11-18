package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * Created by soundarya on 11/16/16.
 */
public class EndScreenValidator extends BaseValidator {

    public static Logger logger = Logger.getLogger(EndScreenValidator.class);

    public EndScreenValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("controlBar");
        addElementToPageElements("replay");
    }

    public void validate(String element, int timeout) throws Exception {
        waitOnElement("END_SCREEN", 60);
        String replaytxt = getWebElement("PLAY_PAUSE").findElement(By.tagName("span")).getAttribute("class");
        Assert.assertEquals(replaytxt.trim(), "oo-icon oo-icon-system-replay", "Replay button is not present on end screen");
        double currenttime = Double.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
        double totaltime = Double.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getDuration();").toString());
        Assert.assertEquals(currenttime, totaltime, "Current Time and TotalTime duration is not showing correctly");
    }

}