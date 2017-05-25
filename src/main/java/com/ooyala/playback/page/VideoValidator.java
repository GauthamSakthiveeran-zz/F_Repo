package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class VideoValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(VideoValidator.class);

	public VideoValidator(WebDriver webDriver) {
		super(webDriver);
	}

	public VideoValidator getConsoleLogs() {
		driver.executeScript(
				"var oldf = console.log;console.log = function() {oldf.apply(console, arguments);if(arguments[0].includes('video/mp4')) OO.$(\"#ooplayer\").append(\"<p id=video_mp4>\" + arguments[0] + \"</p>\");}");
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
//TODO
		/*if (!isVideoPluginPresent("bit_wrapper")) {
			return true;
		}

		if (!waitOnElement(By.id("video_mp4"), 20000)) {
			extentTest.log(LogStatus.FAIL, "Black Screen");
			return false;
		}*/
		return true;
	}

}
