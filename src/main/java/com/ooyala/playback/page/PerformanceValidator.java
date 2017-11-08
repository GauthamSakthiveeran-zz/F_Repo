package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class PerformanceValidator extends PlayBackPage implements PlaybackValidator {
	
	 private final static Logger logger = Logger.getLogger(PerformanceValidator.class);

	public PerformanceValidator(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@");
		logger.info(driver.findElementById("player_load_time").getText());
		logger.info(driver.findElementById("playback_start_time").getText());
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@");
		return true;
	}

	public void getConsoleLogs() {
		driver.executeScript(
				"var oldf = console.log;"
				+ "console.log = function() "
				+ "{"
				+ "oldf.apply(console, arguments);"
				+ "if(arguments[0].includes('(PERF) PLAYER LOAD TIME')) OO.$(\"#ooplayer\").append(\"<p id=player_load_time>\" + arguments[0] + \"</p>\");"
				+ "if(arguments[0].includes('(PERF) PLAYBACK START TIME')) OO.$(\"#ooplayer\").append(\"<p id=playback_start_time>\" + arguments[0] + \"</p>\");"
				+ "}");
	}
	
	

}
