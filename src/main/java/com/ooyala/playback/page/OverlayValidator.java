package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

import java.util.Set;

public class OverlayValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(OverlayValidator.class);

	public OverlayValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adoverlay");
		addElementToPageElements("fullscreen");
	}
	
	private boolean checkInFullScreen(String element, int timeout) throws Exception{
		
		/*logger.info("No close button for Overlay");
		extentTest.log(LogStatus.INFO, "No close button seen in normal screen on Overlay....trying in Fullscreen.");
		
		FullScreenAction fullScreenAction = PlayBackFactory.getInstance(driver).getFullScreenAction();
		
		if (!fullScreenAction.startAction())
			return false;

		if (!getBrowser().equalsIgnoreCase("safari") && !getPlatform().equalsIgnoreCase("Android")) {
			if (!waitOnElement("OVERLAY_CLOSE_BTN", 40000))
				return false;
			if (!clickOnIndependentElement("OVERLAY_CLOSE_BTN"))
				return false;
			logger.info("Clicked on overlay close button in fullscreen screen \n");
			logger.info("Overlay gets closed");
		}

		if (!waitOnElement(By.id(element), timeout))
			return false;*/

		return true;
	}
	
	private boolean validateOverlayCloseButton(String element, int timeout) throws Exception{
		if (!isElementPresent("OVERLAY_CLOSE_BTN")) {
			return checkInFullScreen(element, timeout);
		}

//		if (!waitOnElement("OVERLAY_CLOSE_BTN", 20000))
//			return false;
		
		if (!clickOnIndependentElement("OVERLAY_CLOSE_BTN"))
			return false;
		
		return true;
		
	}

	public boolean validate(String element, int timeout) throws Exception {
		
		if (!waitOnElement(By.id(element), timeout))
			return false;
		
		if(isElementPresent("OVERLAY_RECALL_BTN")){
			extentTest.log(LogStatus.PASS, "OVERLAY_RECALL_BTN present.");
			if(clickOnIndependentElement("OVERLAY_RECALL_BTN")){
				validateOverlayCloseButton(element,timeout);
			}else{
				extentTest.log(LogStatus.FAIL, "OVERLAY_RECALL_BTN is present but not clickable..");
			}
		}
		
		return true;

	}

	public boolean validateClickThrough(String element, int timeout) throws Exception{
	    if(element.contains("Freewheel")){
            WebElement frame = driver.findElement(By.xpath("//iframe[contains(@id,'fw_ad_container')]"));
            driver.switchTo().frame(frame);
            WebElement element1 = driver.findElement(By.xpath("//*[local-name()='img']"));
            element1.click();
            driver.switchTo().defaultContent();
            logger.info("Clicked on Freewheel Overlay");
            return true;
        }

        if(!element.contains("Freewheel")){validateOverlayCloseButton(element,timeout);}

        return false;
    }

	public boolean validateOverlayRenderingEvent(int timeout){
		boolean result=false;
		try {
			logger.info("Inside validateOverlayRenderingEvent method");
			Thread.sleep(timeout);
			String consoleOutput = driver.executeScript("return OO.DEBUG.consoleOutput[0].toString()").toString();
			logger.info(consoleOutput);
			if (consoleOutput.contains("overlayRendering")) {
				logger.info("overlayRendering event found in consoleOutput");
				result = true;
			}else {
				logger.error("overlayRendering event not found in consoleOutput");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

}
