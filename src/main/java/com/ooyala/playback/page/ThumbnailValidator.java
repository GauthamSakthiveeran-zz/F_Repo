package com.ooyala.playback.page;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/22/16.
 */
public class ThumbnailValidator extends PlayBackPage implements
		PlaybackValidator {

	public static Logger logger = Logger.getLogger(ThumbnailValidator.class);

	public ThumbnailValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("controlbar");

	}

	public boolean validate(String element, int timeout) throws Exception {

		WebElement element1 = getWebElement("SCRUBBER_BAR");
		if (element1 == null)
			return false;
		moveElement(element1);
		return waitOnElement("THUMBNAIL_CONTAINER", 60000)
				&& validateThumbNailImage();

	}

	private boolean validateThumbNailImage() throws Exception {

		Map<String, String> data = parseURL();
		String embed_code = data.get("ec");

		if (getWebElement("THUMBNAIL_IMAGE") != null) {
			String thumbnail_url = getWebElement("THUMBNAIL_IMAGE")
					.getCssValue("background-image");

			if (thumbnail_url.contains(embed_code)) {
				extentTest.log(LogStatus.PASS, "Thumbnail image verified.");
				return true;
			} else {
				extentTest.log(LogStatus.FAIL,
						"Embed code not found in thumbnail image url.");
			}
		}
		extentTest.log(LogStatus.FAIL, "THUMBNAIL_IMAGE not found.");
		return false;

	}
}
