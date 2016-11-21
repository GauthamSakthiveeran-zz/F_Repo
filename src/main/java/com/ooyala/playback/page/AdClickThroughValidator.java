package com.ooyala.playback.page;


import static java.lang.Thread.sleep;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.url.PlayerPropertyValue;

public class AdClickThroughValidator extends BaseValidator {

	public static Logger Log = Logger.getLogger(AdClickThroughValidator.class);
	
	public AdClickThroughValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("adclicks");
	}

	@Override
	public void validate(String element, int timeout) throws Exception {
		waitOnElement(element, timeout);
	}

	public void clickThroughAds(PlayerPropertyValue value) throws Exception{
		String baseWindowHdl = driver.getWindowHandle();
		if(value!=null){
			if(!getPlatform().equalsIgnoreCase("Android")) {// we skipping this code for IMA and Vast (Android)
				if(value!=PlayerPropertyValue.FREEWHEEL){
					if(value==PlayerPropertyValue.VAST){
		                clickOnIndependentElement("adScreenPanel");
					} else{
						WebElement adPanel = getWebElementsList("adScreenPanel1").get(0);
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click()", adPanel);
					}
					waitOnElement("adsClicked_1", 10);
					waitOnElement("adsClicked_videoWindow", 10);
					
				}
			}
			if(value!=PlayerPropertyValue.IMA){
				try {
	                clickOnHiddenElement("learnMore");
	                waitOnElement("adsClicked_learnMoreButton", 5);
	            }catch (Exception e) {
	            	clickOnIndependentElement("learnMore");
	                waitOnElement("adsClicked_learnMoreButton", 20);
	            }
			}
			sleep(2000);
			//java.util.Set<java.lang.String> windowHandles = driver.getWindowHandles();
	        //int count = windowHandles.size();
	        
	        for (String winHandle : driver.getWindowHandles()) {
	              if(!winHandle.equals(baseWindowHdl)) {
	                  driver.switchTo().window(winHandle);
	                  driver.close();
	                  driver.switchTo().window(baseWindowHdl);
	               }
	        }
	        
	        boolean isAd = isAdPlaying(driver);
	        if(isAd) {

	            if(getPlatform().equalsIgnoreCase("Android") ) //TODO : || Description.contains("HLS")
	            {
	                ((JavascriptExecutor) driver).executeScript("pp.play()");
	            }
	            else
	            {
	            	WebElement adPanel = getWebElementsList("adPanel").get(0);
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click()", adPanel);
	            }

	        }
	        
		}else{
			throw new Exception("Ad manager should not be null.");
		}
	}
	
	public static boolean isAdPlaying(WebDriver webDriver){
        Boolean isAdplaying = (Boolean)(((JavascriptExecutor) webDriver).executeScript("return pp.isAdPlaying()"));
        return isAdplaying;
    }
}
