package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DRMValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(DRMValidator.class);

	public DRMValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */

	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		
		if(!waitOnElement(By.id(element), timeout)){
			return false;
		}
		
		String text = driver.findElement(By.id("drm_tag")).getText();
		
		
		JSONObject json = new JSONObject(text);
		
		if (getBrowser().equalsIgnoreCase("chrome") || getBrowser().equalsIgnoreCase("firefox")) {
			if (!json.has("dash_drm"))
				return false;
			JSONObject dash_drm = json.getJSONObject("dash_drm");
			if (!dash_drm.has("drm"))
				return false;
			JSONObject drm = dash_drm.getJSONObject("drm");
			if (!drm.has("widevine"))
				return false;
			String la_url = drm.getJSONObject("widevine").getString("la_url");
			if (!la_url.startsWith("http://player.ooyala.com/sas/drm2/"))
				return false;

		}
		
		if (getBrowser().equalsIgnoreCase("safari")){
			if (!json.has("hls_drm"))
				return false;
			JSONObject hls_drm = json.getJSONObject("dash_drm");
			if (!hls_drm.has("drm"))
				return false;
			JSONObject drm = hls_drm.getJSONObject("drm");
			if (!drm.has("fairplay"))
				return false;
			String la_url = drm.getJSONObject("fairplay").getString("la_url");
			if (!la_url.startsWith("http://player.ooyala.com/sas/fps/"))
				return false;
			String certificate_url = drm.getJSONObject("fairplay").getString("certificate_url");
			if (!certificate_url.startsWith("http://player.ooyala.com/sas/fps/"))
				return false;
		}
		
		return true;
	}
}
