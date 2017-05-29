package com.ooyala.playback.page;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import java.util.ArrayList;

import com.ooyala.playback.factory.PlayBackFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 29/12/16.
 */
public class OoyalaAPIValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(OoyalaAPIValidator.class);

    public OoyalaAPIValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("adclicks");
    }

    @SuppressWarnings("unchecked")
	@Override
    public boolean validate(String element, int timeout) throws Exception {

        /***
         * For android autoPlay does not work and therefore we are not checking autoplay in following
         * if statement
         * Therefore we are playing video explicitly in following else statement....
         */
        if (!getPlatform().equalsIgnoreCase("android")) {
            Boolean autoPlay = (Boolean) (driver.executeScript("return pp.parameters.autoplay"));

            if (!autoPlay) {
                logger.error("Not able to Autoplay");
                extentTest.log(LogStatus.FAIL,"Not able to Autoplay");
                return false;
            }
        } else {
            PlayBackFactory factory = new PlayBackFactory(driver,extentTest);
            factory.getPlayValidator().waitForPage();
            factory.getPlayAction().startAction();
        }

        if(!waitOnElement("AD_PANEL",10000)){
            logger.error("ad is not playing");
            extentTest.log(LogStatus.FAIL,"ad is not playing");
            return false;
        }

        boolean isAdPlaying = (Boolean) (driver.executeScript("return pp.isAdPlaying()"));
        logger.info("is Ad playing :"+isAdPlaying);
        extentTest.log(LogStatus.INFO,"is Ad playing :"+isAdPlaying);
        if(!isAdPlaying){
            logger.error("Ad not playing");
            extentTest.log(LogStatus.FAIL,"Ad not playing");
            return false;
        }

        int showAdSkipBtnTime = parseInt((driver.executeScript("return pp.parameters.linearAdSkipButtonStartTime")).toString());
        while (true) {
            double adPlayHeadTime = parseDouble(driver.executeScript("return pp.getPlayheadTime()").toString());
            if (adPlayHeadTime > (double) showAdSkipBtnTime) {
                driver.executeScript("pp.skipAd();");
                break;
            }

        }

        if(!waitOnElement(By.id("skipAd_1"), 5000)){
            logger.error("Not able to skip the ad");
            extentTest.log(LogStatus.FAIL,"Not able to skip the ad");
            return false;
        }

        if(!loadingSpinner()){
            extentTest.log(LogStatus.FAIL,"loading spinner is present for long time");
            return false;
        }

        if (!waitOnElement(By.id("videoPlaying_1"),10000)){
            logger.error("Video is not playing");
            extentTest.log(LogStatus.FAIL,"Video is not playing");
            return false;
        }

        /***
         * As for Android initialTime parameter not working, we are not checking condition
         * in following if statement for android platform
         */
        if (!getPlatform().equalsIgnoreCase("android")) {
            double initialTime = parseDouble(driver.executeScript("return pp.parameters.initialTime").toString());
            double playHeadTime = parseDouble(driver.executeScript("return pp.getPlayheadTime()").toString());
            if (!(playHeadTime > initialTime)) {
                logger.error("Video playback not started from initial time");
                extentTest.log(LogStatus.FAIL,"Video playback not started from initial time");
                return false;
            }
        }

        double totalTime = parseDouble(driver.executeScript("return pp.getDuration()").toString());
        logger.info("Duration of video is : "+totalTime);
        if(!(totalTime > 0)){
            logger.error("Total time must be greater than 0 but we are getting it as :"+totalTime);
            extentTest.log(LogStatus.FAIL,"Total time must be greater than 0 but we are getting it as :"+totalTime);
            return false;
        }

        if(!loadingSpinner()){
            extentTest.log(LogStatus.FAIL,"loading spinner is present for long time");
            return false;
        }

        driver.executeScript("pp.seek(pp.getDuration()-10);");

        if(!loadingSpinner()){
            extentTest.log(LogStatus.FAIL,"loading spinner is present for long time");
            return false;
        }

        if (!waitOnElement(By.id("seeked_1"),10000)){
            logger.error("Not able to seek the video...");
            extentTest.log(LogStatus.FAIL,"Not able to seek the video...");
            return false;
        }

        String title = driver.executeScript("return pp.getTitle()").toString();
        if (title==null){
            logger.error("Not able to get title of the video");
            extentTest.log(LogStatus.FAIL,"Not able to get title of the video");
            return false;
        }

        String getVolume = driver.executeScript("return pp.getVolume()").toString();
        logger.info("Volume is : "+getVolume);
        if (getVolume==null){
            logger.error("Not able to get volume of the video");
            extentTest.log(LogStatus.FAIL,"Not able to get volume of the video");
            return false;
        }

        String embedCode = driver.executeScript("return pp.getEmbedCode()").toString();
        if (embedCode==null){
            logger.error("Not able to get Embed code");
            extentTest.log(LogStatus.FAIL,"Not able to get Embed code");
            return false;
        }


        ArrayList<String> langList = ((ArrayList<String>)driver
                .executeScript("return pp.getCurrentItemClosedCaptionsLanguages().languages;"));
        for (int i = 0; i < langList.size(); i++) {
            driver.executeScript("pp.setClosedCaptionsLanguage(\"" + langList.get(i) + "\")");
            if (!waitOnElement(By.id("cclanguage_"+langList.get(i)),10000)){
                logger.error("Not able to get "+langList.get(i));
                extentTest.log(LogStatus.FAIL,"Not able to get "+langList.get(i));
                return false;
            }
        }

        return true;
    }
    
    public boolean validateInitailTime(){
    	double initialTime = parseDouble(driver.executeScript("return pp.parameters.initialTime").toString());

        double playHeadTime = parseDouble(driver.executeScript("return pp.getPlayheadTime()").toString());

        if(!(playHeadTime>initialTime)){
            extentTest.log(LogStatus.FAIL,"Video playback not started from initial time");
            logger.error("Video playback not started from initial time");
            return false;
        }
        return true;
    }

}
