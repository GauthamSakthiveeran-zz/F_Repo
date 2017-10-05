package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by jitendra on 1/3/17.
 */
public class InitalTimeValidator extends PlayBackPage implements
        PlaybackValidator {

    public static Logger logger = Logger.getLogger(InitalTimeValidator.class);

    public InitalTimeValidator(WebDriver webDriver){
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("controlbar");
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {
        PlayBackFactory factory = new PlayBackFactory(driver,extentTest);

        if (factory.getEventValidator().validate(element,timeout) && !factory.getEventValidator().validate(element+"_false",2000)){
            logger.info("Initial time matches");
            extentTest.log(LogStatus.PASS, "Initial time matches");
            return true;
        }
        extentTest.log(LogStatus.FAIL, "video is not starting from "+ element+" sec");
        return false;
    }

    public boolean validatePlayHeadTime(String element) throws InterruptedException {

        if (!loadingSpinner()){
            return false;
        }

        int initTimeAfterPlayback = 0;
        int initTime = 0;
        boolean flag =false;
        if(!driver.executeScript("return pp.parameters.initialTime").toString().equals("undefined")) {
            onmouseOver(getWebElement("SCRUBBER_BAR"));
            initTime = Integer.parseInt(driver.executeScript("return pp.parameters.initialTime").toString());
        }else {
            logger.info("Add initialTime parameter in Player Configuration Parameters under Options");
            extentTest.log(LogStatus.SKIP,"Add initialTime parameter in Player Configuration Parameters under Options");
        }

        for (int i = 0 ; i<10 ; i++){
            onmouseOver(getWebElement("SCRUBBER_BAR"));
            String playhead_time = getWebElement("PLAYHEAD_TIME").getText();

            if (!playhead_time.equals("")) {
                String[] aa = playhead_time.split(":");
                initTimeAfterPlayback = 60 * Integer.parseInt(aa[0]) + Integer.parseInt(aa[1]);
            }

            if (initTimeAfterPlayback >= initTime && initTimeAfterPlayback <= (initTime+5)) {
                logger.info("Video is started from "+initTimeAfterPlayback+" sec.");
                extentTest.log(LogStatus.PASS,"Video i started from "+initTimeAfterPlayback+" sec.");
                flag = true;
                break;
            }

            Thread.sleep(1000);
        }

        if (!flag){
            logger.error("Video is starting from "+ initTimeAfterPlayback +" sec.");
            extentTest.log(LogStatus.FAIL,"Video is started from "+ initTimeAfterPlayback +" sec.");
            return false;
        }

        return true;
    }
}
