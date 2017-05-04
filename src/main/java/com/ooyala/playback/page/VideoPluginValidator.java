package com.ooyala.playback.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.url.UrlObject;
import com.relevantcodes.extentreports.LogStatus;

public class VideoPluginValidator extends PlayBackPage implements PlaybackValidator {

	public VideoPluginValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}

	private UrlObject urlObject;

	public VideoPluginValidator setUrlObject(UrlObject urlObject) {
		this.urlObject = urlObject;
		return this;
	}
	
	public void getConsoleLogs() {
		driver.executeScript(
				"var oldf = console.log;console.log = function() "
				+ "{"
				+ "oldf.apply(console, arguments);"
				+ "if(arguments[0].includes('Bitmovin player is using technology')) "
				+ "OO.$(\"#ooplayer\").append(\"<p id=bitmovin>\" + arguments[0] + \"</p>\");"
				+ "if(arguments[0].includes('[OSMF]:JFlashBridge: Call:'))"
				+ "OO.$(\"#ooplayer\").append(\"<p id=osmf>\" + arguments[0] + \"</p>\");"
				+ "if(arguments[0].includes('[AkamaiHD]:JFlashBridge: Call:'))"
				+ "OO.$(\"#ooplayer\").append(\"<p id=akamai>\" + arguments[0] + \"</p>\");"
				+ "}");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		
		if(urlObject!=null) {
			if(urlObject.getStreamType().equals("m3u8") || urlObject.getStreamType().equals("mpd")) { // hls and dash
				if(!waitOnElement(By.id("bitmovin"), 1000)) {
					extentTest.log(LogStatus.FAIL, "Bitmovin plugin is not loaded.");
					return false;
				}
				extentTest.log(LogStatus.PASS, "Bitmovin plugin is loaded.");
			}
			/*else if(urlObject.getStreamType().equals("mp4")) { // mp4
				if(waitOnElement(By.id("bitmovin"), 1000) || waitOnElement(By.id("osmf"), 1000) || waitOnElement(By.id("akamai"), 1000)) {
					extentTest.log(LogStatus.FAIL, "Main plugin is not loaded.");
					return false;
				}
				extentTest.log(LogStatus.PASS, "Main plugin is loaded.");
			}*/
			else if(urlObject.getStreamType().equals("f4m") || urlObject.getStreamType().equals("f4f")) {
				if(!waitOnElement(By.id("osmf"), 1000)) { // can also be akamai, need to check for asset
					extentTest.log(LogStatus.FAIL, "Bitmovin plugin is not loaded.");
					return false;
				}
				extentTest.log(LogStatus.PASS, "OSMF plugin is loaded.");
			}
			return true;
		}
		
		return false;
	}

}
