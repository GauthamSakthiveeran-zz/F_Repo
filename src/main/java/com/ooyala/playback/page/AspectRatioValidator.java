package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/16/16.
 */

public class AspectRatioValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger Log = Logger.getLogger(AspectRatioValidator.class);

	private boolean verticalVideo = false;

	public AspectRatioValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("aspectratio");
	}

	public AspectRatioValidator setVerticalVideo() {
		this.verticalVideo = true;
		return this;
	}

	public boolean validate(String element, int timeout) throws Exception {

		if (isElementPresent(By.id(element))) {

			int width = Integer.parseInt(getWebElement(element).getAttribute(
					"width"));
			int height = Integer.parseInt(getWebElement(element).getAttribute(
					"height"));

			if (verticalVideo) {
				Assert.assertEquals(width, 320, "Width Matches");
				Assert.assertEquals(height, 568, "Heigth Matches");
				extentTest.log(LogStatus.PASS, " Verified Vertical Video");
			} else {
				int diff = width / 4;
				int expectedHeight = width - diff;
				Assert.assertEquals(expectedHeight, height,
						"Video is in 4:3 ratio");
				extentTest.log(LogStatus.PASS, " Verified Aspect Ratio 4:3");

			}
			return true;
		} else {
			extentTest.log(LogStatus.FAIL, "Aspect ratio element not present");
			return false;
		}
	}

}
