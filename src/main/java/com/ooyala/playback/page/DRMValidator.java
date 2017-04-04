package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

public class DRMValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(DRMValidator.class);

	public DRMValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */

	}
	
	private boolean opt = false;
	
	public DRMValidator opt() {
		opt = true;
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		
		/*if(!waitOnElement(By.id(element), timeout)){
			extentTest.log(LogStatus.FAIL, "wait on " +element + " failed");
			return false;
		}*/
		
		String text = driver.executeScript("return OO.DEBUG.consoleOutput[0].toString().split(/2\":(.+)/)[1]").toString();
		logger.info(text);
		JSONObject json = new JSONObject(text);
		
		String certificate_url = "";
		
		if (getBrowser().equalsIgnoreCase("safari")){
			if (!json.has("hls_drm")){
				extentTest.log(LogStatus.FAIL, "hls_drm not found.");
				return false;
			}
			JSONObject hls_drm = json.getJSONObject("dash_drm");
			if (!hls_drm.has("drm")){
				extentTest.log(LogStatus.FAIL, "drm not found.");
				return false;
			}
			JSONObject drm = hls_drm.getJSONObject("drm");
			if (!drm.has("fairplay")){
				extentTest.log(LogStatus.FAIL, "fairplay not found.");
				return false;
			}
			certificate_url = drm.getJSONObject("fairplay").getString("la_url");
			if (!certificate_url.startsWith("http://player.ooyala.com/sas/fps/")){
				extentTest.log(LogStatus.FAIL, "la_url does not start with player.ooyala.com/sas/fps/");
				return false;
			}
		} else {
			if (!json.has("dash_drm")){
				extentTest.log(LogStatus.FAIL, "dash_drm not found.");
				return false;
			}
			JSONObject dash_drm = json.getJSONObject("dash_drm");
			if (!dash_drm.has("drm")){
				extentTest.log(LogStatus.FAIL, "drm not found.");
				return false;
			}
			JSONObject drm = dash_drm.getJSONObject("drm");
			if (!drm.has("widevine")){
				extentTest.log(LogStatus.FAIL, "widevine not found.");
				return false;
			}
			certificate_url = drm.getJSONObject("widevine").getString("la_url");
			if (!certificate_url.startsWith("http://player.ooyala.com/sas/drm2/")){
				extentTest.log(LogStatus.FAIL, "certificate_url does not start with http://player.ooyala.com/sas/drm2/");
				return false;
			}
		}
		
		if(opt) {
			logger.info(certificate_url);
			if(!certificate_url.contains("ooyala?auth_token=")) {
				extentTest.log(LogStatus.FAIL, "certificate_url does not contain ooyala?auth_token=");
				return false;
			}
		}
		
		return true;
	}
}
