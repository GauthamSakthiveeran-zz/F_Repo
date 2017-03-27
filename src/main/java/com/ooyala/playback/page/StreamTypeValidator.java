package com.ooyala.playback.page;

import com.ooyala.playback.url.UrlGenerator;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by suraj on 3/23/17.
 */
public class StreamTypeValidator extends PlayBackPage implements PlaybackValidator {
    private static Logger logger = Logger.getLogger(StreamTypeValidator.class);

    public StreamTypeValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
    }


    public boolean validate(String element, int timeout) throws Exception {

        return true;
    }

    public boolean validateStream(String element, String description) {
        String streamContains = driver.findElement(By.id(element)).getText();
        String streamType = UrlGenerator.getStreamTypeDetails().get(description);
        if (!streamContains.contains(streamType)){
            logger.info("Stream is not matching as per expected result "+streamContains);
            extentTest.log(LogStatus.PASS,"Stream is not matching as per expected result "+streamContains);
            return  false;
        }
        return true;
    }


}
