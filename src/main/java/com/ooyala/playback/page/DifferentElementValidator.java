package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * Created by snehal on 28/11/16.
 */
public class DifferentElementValidator extends PlayBackPage implements
		PlaybackValidator {
	public static Logger logger = Logger.getLogger(DestroyValidator.class);

	public DifferentElementValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("element");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		List<WebElement> ele = getWebElementsList(element);
		String element1_id = ele.get(0).getAttribute("id");
		String element2_id = ele.get(1).getAttribute("id");
		if (element1_id.equals(element2_id)){return false;}
		return true;
	}

	public boolean validateMainVideoElementId(String element,int timeout){
        if (!waitOnElement(element,timeout)){
            logger.error("Element is not present for main video");
            extentTest.log(LogStatus.FAIL,"Element is not present for main video");
            return false;
        }
        logger.info("Element is present for video");
		return true;
	}

	public boolean validateAdElementId(String element,int timeout){
        if (!isElementPresent(element)){
            logger.error("Element is not present for ad ");
            extentTest.log(LogStatus.FAIL,"Element is not present for ad ");
            return false;
        }
        logger.info("Element is present for ad ");
        return true;
	}
}
