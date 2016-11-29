package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

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

		Actions action = new Actions(driver);

		WebElement element1 = getWebElement("SCRUBBER_BAR");
		if(element1==null) return false;
		action.moveToElement(element1).build().perform();
		return waitOnElement("THUMBNAIL_CONTAINER", 60);

	}
	
	public void validateThumbNailImage(String embedCode) throws Exception{
		
        String Thumbnail_url = getWebElement("THUMBNAIL_IMAGE").getCssValue("background-image");
       
        Assert.assertTrue(Thumbnail_url.contains(embedCode));
	}
}
