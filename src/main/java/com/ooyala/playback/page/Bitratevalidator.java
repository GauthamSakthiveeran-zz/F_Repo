package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.relevantcodes.extentreports.LogStatus;

import java.util.List;
import java.util.Map;

public class Bitratevalidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(Bitratevalidator.class);

    public Bitratevalidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("bitrate");
    }

    private boolean findAndClickOnBitrateIcon() {
        try {
            if (!isElementPresent("BITRATE")) {
                if (!clickOnIndependentElement("MORE_OPTION_ITEM")) {
                    extentTest.log(LogStatus.FAIL, "Unable to click on MORE_OPTION_ITEM");
                    return false;
                }
                if (!isElementPresent("BITRATE")) {
                    extentTest.log(LogStatus.FAIL, "Bitrate icon not found in more option item.");
                    return false;
                }
            }
        } catch (Exception e) {
            extentTest.log(LogStatus.FAIL, e);
            return false;
        }

        if (!clickOnIndependentElement("BITRATE")) {
            extentTest.log(LogStatus.FAIL, "Unable to click on bitrate icon");
            return false;
        }

        return true;
    }

    public boolean validate(String element, int timeout) throws Exception {

        if (!findAndClickOnBitrateIcon())
            return false;

        PlayerAPIAction playerAPIAction = new PlayBackFactory(driver, extentTest).getPlayerAPIAction();

        int length = playerAPIAction.getBitratesAvailableLength();
        if (length > 1) {
            boolean flag = true;
            for (int i = 0; i < length; i++) {
                String bitrate = playerAPIAction.getBitratesAvailable(i, "bitrate");
                String id = playerAPIAction.getBitratesAvailable(i, "id");

                logger.info("value of bitrate id is : " + id);

                playerAPIAction.setTargetBitrate(id);

                playerAPIAction.seek(10);

                playerAPIAction.play();
                if (!("0".equals(bitrate))) {

                    if (!waitOnElement(By.id("bitrateChanged_" + (bitrate)), 50000)) {
                        extentTest.log(LogStatus.INFO, "Bitrate changed element not found for bitrate " + bitrate);
                        flag = false;
                    }
                } else {
                    String currentBitrate = playerAPIAction.getCurrentBitrate();
                    if (currentBitrate == null) {
                        extentTest.log(LogStatus.FAIL, "pp.getCurrentBitrate()[\"bitrate\"] returns null.");
                        return false;
                    }
                }
            }

            if (!flag) {
                extentTest.log(LogStatus.FAIL, "Bitrate changed element not found");
                return false;
            }

        } else {
            String currentBitrate = playerAPIAction.getCurrentBitrate();
            playerAPIAction.play();
            if (currentBitrate == null) {
                extentTest.log(LogStatus.FAIL, "return pp.getCurrentBitrate()[\"bitrate\"] returns null.");
                return false;
            }

        }

        extentTest.log(LogStatus.PASS, "Bitrate validated.");
        return true;

    }

    int beforeAdPlay;
    int afterAdPlay;

    public boolean totalBitrateCountBeforeAdPlayback() {

        if (!findAndClickOnBitrateIcon())
            return false;

        try {
            beforeAdPlay = getWebElementsList("BITRATE_SELECTION").size();
            logger.info("beforeAdPlay :" + beforeAdPlay);
            extentTest.log(LogStatus.INFO, "beforeAdPlay :" + beforeAdPlay);
            return true;
        } catch (Exception e) {
            extentTest.log(LogStatus.FAIL, e);
            return false;
        }
    }

    public boolean totalBitrateCountAfterAdPlayback() {

        if (!findAndClickOnBitrateIcon())
            return false;

        try {
            afterAdPlay = getWebElementsList("BITRATE_SELECTION").size();
            logger.info("afterAdPlay :" + afterAdPlay);
            extentTest.log(LogStatus.INFO, "afterAdPlay :" + afterAdPlay);
            return true;
        } catch (Exception ex) {
            extentTest.log(LogStatus.FAIL, ex);
            return false;
        }
    }

    public boolean isBirateOptionsVarying() {
        if (afterAdPlay != beforeAdPlay) {
            extentTest.log(LogStatus.FAIL, "Bitrate Options are not matching ...");
            return false;
        }
        return true;
    }

    public boolean validateSingleDynamicFilter(String biterateFilter) {

        logger.info("Bitrate Filter Value :"+biterateFilter);
        String minBitrate="";
        String maxBitrate="";
        //Check if the Bitrate filter value is range or not
        //if its range then split the values as minBitrate and maxBitrate
        if(biterateFilter.contains(",")){
            minBitrate = biterateFilter.split(",")[0];
            maxBitrate = biterateFilter.split(",")[1];
        }
        //get the available bitrates through api and store it in a List of Map of key as integer and value as string
        List<Map<Integer,String>> availableBitrate = (List<Map<Integer, String>>) driver.executeScript("return pp.getBitratesAvailable()");
        logger.info(availableBitrate);
        //check if the available bitrates are more than 2
        //By default the first bitrate value is 0, so we are not considering it.
        if(availableBitrate.size()>2){
            String minAvailableBitrate = availableBitrate.get(1).values().toArray()[1].toString();
            String maxAvailableBitrate = availableBitrate.get(availableBitrate.size()-1).values().toArray()[1].toString();
            //Check if the minBitrate values and maxBitrate values are matching or not
            if((!minAvailableBitrate.equals(minBitrate))&&(!maxAvailableBitrate.equals(maxBitrate))){
                logger.error("Bitrate Filter Value :"+biterateFilter+ " and Current Bitrate Value :"+availableBitrate+" do not match");
                extentTest.log(LogStatus.FAIL,"Bitrate Filter Value :"+biterateFilter+ " and Current Bitrate Value :"+availableBitrate+" do not match");
                return false;
            }
        }else {
            if(!biterateFilter.equals(availableBitrate.get(1).values().toArray()[1].toString())){
                logger.error("Bitrate Filter Value :"+biterateFilter+ " and Current Bitrate Value :"+availableBitrate+" do not match");
                extentTest.log(LogStatus.FAIL,"Bitrate Filter Value :"+biterateFilter+ " and Current Bitrate Value :"+availableBitrate+" do not match");
                return false;
            }
        }
        logger.info("Bitrate Filter Value :"+biterateFilter+ " and Current Bitrate Value :"+availableBitrate+" do match");
        extentTest.log(LogStatus.PASS,"Bitrate Filter Value :"+biterateFilter+ " and Current Bitrate Value :"+availableBitrate+" do match");
        return true;
    }

    public boolean validateExistingInvalidFilter(String filter){
        logger.info("Filter Value : "+filter);
        String errorCode = "";
        try{
            errorCode = driver.executeScript("return pp.getErrorCode()").toString().toLowerCase();
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("pp.getErrorCode() returns null");
            extentTest.log(LogStatus.ERROR,"pp.getErrorCode() returns null");
        }
        if(!("stream".equals(errorCode))){
            logger.error("error code is not matching expected error code : stream but getting error code as :"+errorCode);
            extentTest.log(LogStatus.FAIL,"error code is not matching expected error code : stream but getting error code as :"+errorCode);
            return false;
        }
        logger.info("error code is matching");
        extentTest.log(LogStatus.PASS,"error code is matching");
        return true;
    }

    public boolean validateNonExistingInvalidFilter(String filter){
        logger.info(filter);
        boolean isPlaying = (boolean) driver.executeScript("return pp.isPlaying()");
        if(isPlaying){
            logger.info("Playback is fine");
            extentTest.log(LogStatus.PASS,"Playback is fine");
            return true;
        }
        logger.error("Something went wrong, check filter");
        extentTest.log(LogStatus.FAIL,"Something went wrong, check filter");
        return false;
    }
}
