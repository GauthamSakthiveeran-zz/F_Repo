package com.ooyala.playback.page;

import com.ooyala.playback.url.UrlObject;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 10/27/16.
 */
public class SeekValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(SeekValidator.class);

	public SeekValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("play");
	}

	public boolean validate(String element, int timeout) throws Exception {

		(new PlayBackFactory(driver, extentTest)).getSeekAction().seekTillEnd().startAction();
		
		if (!loadingSpinner()){
			extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
			return false;
		}

        if (driver.getCurrentUrl().contains("AnalyticsQEPlugin")){
            if(!(isAnalyticsElementPreset("analytics_video_"+element)
                    && isAnalyticsElementPreset("analytics_video_requested_"+element))){
                return false;
            }
        }

		if (waitOnElement(By.id(element), timeout)) {
			extentTest.log(LogStatus.PASS, "Seek successful.");
			if (!loadingSpinner()){
			    return false;
            }
			return (new PlayBackFactory(driver, extentTest)).getScrubberValidator().validate("", timeout);
		}

		extentTest.log(LogStatus.INFO, "Wait on " + element + " failed after "+ timeout + " ms");
		return false;
	}

}
