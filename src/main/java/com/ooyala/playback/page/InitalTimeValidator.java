package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
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
}
