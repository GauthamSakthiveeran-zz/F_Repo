package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by jitendra on 16/03/17.
 */
public class ConcurrentStreamValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(ConcurrentStreamValidator.class);

	public ConcurrentStreamValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);


	}

	public boolean validate(String element, int timeout) throws Exception {
        return true;
	}

	public boolean concurrentStream(WebDriver driver,String url){
        PlayBackFactory factory = new PlayBackFactory(driver,extentTest);

        driver.get(url);
        if (!factory.getPlayValidator().waitForPage()){
            logger.error("Concurrent Stream is failed for first attempt of video playback");
            extentTest.log(LogStatus.FAIL,"");
            return false;
        }

        return true;
	}
}
