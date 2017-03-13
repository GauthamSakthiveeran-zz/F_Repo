package com.ooyala.playback.page;

import com.ooyala.qe.common.util.PropertyReader;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

/**
 * Created by snehal on 07/03/17.
 */
public class SetEmbedCodeValidator extends PlayBackPage implements
        PlaybackValidator{

    public static Logger logger = Logger.getLogger(SetEmbedCodeValidator.class);
    Map<String, String> data;
    String embedCode;

    public SetEmbedCodeValidator(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {
        PropertyReader properties = PropertyReader.getInstance("urlData.properties");
        embedCode = properties.getProperty("setEmbedmbedCode");

        ((JavascriptExecutor) driver).executeScript("pp.setEmbedCode('"+embedCode+"')");

        if(!waitOnElement(By.id("setEmbedCode_1"), 50000)) return false;

        if(!waitOnElement(By.id("videoElementDisposed_1"), 60000)) return false;

        Thread.sleep(3000);

        String newAssetEmbedCode = ((JavascriptExecutor) driver)
                .executeScript("return pp.getEmbedCode()").toString();

        if(embedCode.equals(newAssetEmbedCode)){
            logger.info("New aseet is loadded properly");
            return true;
        } else {
            logger.error("New aseet is not loadded properly");
            extentTest.log(LogStatus.FAIL, "New asset is not loaded");
            return false;
        }

    }
}
