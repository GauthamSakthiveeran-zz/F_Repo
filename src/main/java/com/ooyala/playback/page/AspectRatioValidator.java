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

		waitOnElement(By.id(element),40000);

		if (isElementPresent(By.id(element))) {

			int width = Integer.parseInt(getWebElement(element).getAttribute(
					"width"));
			int height = Integer.parseInt(getWebElement(element).getAttribute(
					"height"));

			Log.info("Width : "+width+" Height : "+height);

			int factor = greatestCommonFactor(width, height);
			int widthRatio = width / factor;
			int heightRatio = height / factor;

			Log.info("Resolution: " + width + "x" + height);
			Log.info("Aspect Ratio: " + widthRatio + ":" + heightRatio);
			Log.info("Decimal Equivalent: " + widthRatio / heightRatio);

			if (verticalVideo){
				Assert.assertEquals(widthRatio, 16, "Width Matches");
				Assert.assertEquals(heightRatio, 9, "Heigth Matches");
				extentTest.log(LogStatus.PASS, " Verified Vertical Video");
			} else {
				Assert.assertEquals(widthRatio, 4, "Width Matches");
				Assert.assertEquals(heightRatio, 3, "Heigth Matches");
				extentTest.log(LogStatus.PASS, "Verified Aspect Ratio Video");
			}
			return true;
		} else {
			Log.error(element+" is not found.");
			extentTest.log(LogStatus.FAIL, "Aspect ratio element not present");
			return false;
		}
	}

	public int greatestCommonFactor(int width, int height) {
		return (height == 0) ? width : greatestCommonFactor(height, width % height);
	}

}
