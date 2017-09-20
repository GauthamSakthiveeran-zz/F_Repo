
package com.ooyala.playback.apps.validators;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;

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

	public boolean isUpNextElementDisplayedandPlayUpNextVideo(String element) {
		getPause();

		if (waitOnElement("UPNEXT_CLOSEBUTTON_ANDROID", 50000)) {
			extentTest.log(LogStatus.INFO, "UpNextVideo is displayed");
			logger.info(element + " is not Displayed");
			List<WebElement> imageViews = getWebElementsList("UPNEXT_PLAY_ANDROID");
			for (WebElement elementToClick : imageViews) {
				if (elementToClick.getLocation().x == 0)
					elementToClick.click();
			}

			return true;
		} else {
			extentTest.log(LogStatus.FAIL, "UpNextVideo is not displayed");
			logger.info(element + " is not Displayed");
			return false;
		}
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		Boolean result = true;

		result = result && isUpNextElementDisplayedandPlayUpNextVideo(element);

		return false;
	}
}
