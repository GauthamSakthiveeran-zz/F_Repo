package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static java.lang.Thread.sleep;

/**
 * Created by soundarya on 11/17/16.
 */
public class Bitratevalidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(Bitratevalidator.class);

    public Bitratevalidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("bitrate");
    }

    public boolean validate(String element, int timeout) throws Exception {

        try {
            if (!isElementPresent("BITRATE"))
                return false;
        } catch (Exception e) {
            if (!clickOnIndependentElement("MORE_OPTION_ITEM"))
                return false;
            sleep(1000);
            if (!isElementPresent("BITRATE"))
                return false;
        }

        if (!clickOnIndependentElement("BITRATE"))
            return false;

        try{
            if(isElementPresent("BITRATE_CLOSE_BTN")) {
                clickOnIndependentElement("BITRATE_CLOSE_BTN");
            }
        } catch (Exception e){
            logger.info("Bitrate close button is not present");
        }

        int length = Integer.parseInt(((JavascriptExecutor) driver)
                .executeScript("return pp.getBitratesAvailable().length")
                .toString());
        if (length > 1) {
            boolean flag = true;
            for (int i = 0; i < length; i++) {
                String bitrate = driver.executeScript(
                        "return pp.getBitratesAvailable()[" + i + "]['bitrate']").toString();

                String id = driver.executeScript(
                        "return pp.getBitratesAvailable()[" + i + "]['id']").toString();

                logger.info("value of bitrate id is : " + id);

                driver.executeScript("return pp.setTargetBitrate('" + id + "')");

                driver.executeScript("pp.seek(10)");

                Thread.sleep(5000);

                driver.executeScript("pp.play();");
                if (!("0".equals(bitrate))) {
                    flag = flag && waitOnElement(By.id("bitrateChanged_" + (bitrate)),50000);
                    logger.info("Is bitrateChanged_" + bitrate + " present :?" + flag);
                }else {
                    String currentBitrate = ((JavascriptExecutor) driver).executeScript("return pp.getCurrentBitrate()[\"bitrate\"]").toString();
					flag = flag && (currentBitrate != null ? true : false);
                }
            }
            return flag;
        } else {
            String currentBitrate = ((JavascriptExecutor) driver)
                    .executeScript("return pp.getCurrentBitrate()[\"bitrate\"]")
                    .toString();
            ((JavascriptExecutor) driver).executeScript("return pp.play()");
            if (currentBitrate == null) {
                logger.error("Current bitrate should not be null for Auto Bitrate option");
                return false;
            }
            return true;
        }

    }

    int beforeAdPlay;
    int afterAdPlay;
    public boolean totalBitrateCountBeforeAdPlayback(){

        try {
            if (!isElementPresent("BITRATE"))
                return false;
        } catch (Exception e) {
            if (!clickOnIndependentElement("MORE_OPTION_ITEM"))
                return false;
            if (!isElementPresent("BITRATE"))
                return false;
        }

        if (!clickOnIndependentElement("BITRATE"))
            return false;
        try {
            beforeAdPlay = driver.findElements(By.xpath(".//a[@class='oo-quality-btn']")).size();
            logger.info("beforeAdPlay :"+beforeAdPlay);
            extentTest.log(LogStatus.INFO,"beforeAdPlay :"+beforeAdPlay);
            return true;
        } catch (Exception e){
            extentTest.log(LogStatus.FAIL,e.getMessage());
            return false;
        }
    }

    public boolean totalBitrateCountAfterAdPlayback(){

        try {
            if (!isElementPresent("BITRATE"))
                return false;
        } catch (Exception e) {
            if (!clickOnIndependentElement("MORE_OPTION_ITEM"))
                return false;
            if (!isElementPresent("BITRATE"))
                return false;
        }

        if (!clickOnIndependentElement("BITRATE"))
            return false;
        try {
            afterAdPlay = driver.findElements(By.xpath(".//a[@class='oo-quality-btn']")).size();
            logger.info("afterAdPlay :"+afterAdPlay);
            extentTest.log(LogStatus.INFO,"afterAdPlay :"+afterAdPlay);
            return true;
        } catch (Exception ex){
            extentTest.log(LogStatus.FAIL,ex.getMessage());
            return false;
        }
    }

    public boolean isBirateOptionsVarying(){
        if (afterAdPlay != beforeAdPlay){
            extentTest.log(LogStatus.FAIL,"Bitrate Options are not matching ...");
            return false;
        }
        return true;
    }
}
