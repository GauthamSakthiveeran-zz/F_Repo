
package com.ooyala.playback.apps.validators;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

/**
 * Created by Gautham
 */
public class UpNextValidator extends PlaybackApps implements Validators {

	private Logger logger = Logger.getLogger(UpNextValidator.class);

	public UpNextValidator(AppiumDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("upnext");
	}

	public boolean isUpNextElementDisplayedandPlayUpNextVideo() {
	    doubletapPlayerScreen();
	    if(System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("android"))
        {
		if (waitOnElement("UPNEXT_CLOSEBUTTON_ANDROID", 50000)) {
			extentTest.log(LogStatus.INFO, "UpNextVideo is displayed");
			List<WebElement> imageViews = getWebElementsList("UPNEXT_PLAY_ANDROID");
			for (WebElement elementToClick : imageViews) {
				if (elementToClick.getLocation().x == 0)
					elementToClick.click();
			}

			return true;
		} else {
			extentTest.log(LogStatus.FAIL, "UpNextVideo is not displayed");
			return false;
		}
        }
	    else
	    {
	        //Code for IOS
	        return true;
	    }
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		Boolean result = true;

		result = result && isUpNextElementDisplayedandPlayUpNextVideo();

		return false;
	}
	
    public void doubletapPlayerScreen() {
        try
        {
        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        Thread.sleep(2000);
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        extentTest.log(LogStatus.INFO, "Double tap done");
        }
        catch(Exception e)
        {
        extentTest.log(LogStatus.FAIL, "Double tap failed");   
        }

    }
}
