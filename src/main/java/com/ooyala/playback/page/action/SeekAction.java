package com.ooyala.playback.page.action;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.PlayBackPage;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

/**
 * 
 * @author dmanohar
 *
 */
public class SeekAction extends PlayBackPage implements PlayerAction {

	private static Logger logger = Logger.getLogger(SeekAction.class);
	private int time;
	private boolean fromLast;
	private String adPlugin;
	private int factor;
	private boolean seekTillEnd;

	public SeekAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("play");
		time = 0;
		fromLast = false;
		adPlugin = "";
		factor = 1;
		seekTillEnd = false;
	}

	public SeekAction seekTillEnd() {
		seekTillEnd = true;
		return this;
	}

	public SeekAction setTime(int time) {
		this.time = time;
		return this;
	}

	public SeekAction fromLast() {
		fromLast = true;
		return this;
	}

	public SeekAction setAdPlugin(String adPlugin) {
		this.adPlugin = adPlugin;
		return this;
	}

	public SeekAction setFactor(int factor) {
		this.factor = factor;
		return this;
	}

	@Override
	public boolean startAction() throws Exception {

		if (time == 0 && seekTillEnd == false) {
			throw new Exception("Time to seek needs to be set! or seekTillEnd should be set to true");
		}

		if (!adPlugin.isEmpty()) {
			Map<String, String> data = parseURL();
			if (data.get("ad_plugin") != null && !data.get("ad_plugin").equals(adPlugin)) {
				extentTest.log(LogStatus.INFO, "This particular step is skipped as it is valid only for " + adPlugin);
				return true;
			}

		}

		if (seekTillEnd && factor == 1) {
			seekTillEnd = false;
			return seekPlayback();

		} else {
			if (fromLast) {
				return seek(time, fromLast);
			} else {
				return seek(time + "");
			}
		}
	}

	public String getDurationString() {
		return "pp.getDuration()/" + factor;
	}

	public boolean seek(int time, boolean fromLast) throws Exception {
		String seekduration;
		if (fromLast) {
			seekduration = getDurationString();
		} else {
			seekduration = "";
		}
		factor = 1;
		return seek(seekduration + "-" + time + "");
	}

	public boolean seek(String time) {
		try {
			((JavascriptExecutor) driver).executeScript("return pp.seek(" + time + ")" + "");
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean seekPlayback() {
		try {
			int count = 10;
			while (true) {
				double seekTime = getPlayAheadTime();
				if (seekTime == -1) {
					extentTest.log(LogStatus.INFO, "Video is in error mode");
					logger.error("Video is in error mode");
					seek(31, true);
					break;
				}
				if (seekTime > 5) {
					// Update after ticket is fixed pp.seek() api is not working
					// if
					// we try to seek less than 31 seconds form end of video
					if (getBrowser().equalsIgnoreCase("safari") || getBrowser().equalsIgnoreCase("internet explorer")) {
						seek(31, true);
					} else {
						seek(7, true);
					}
					// ((JavascriptExecutor)
					// driver).executeScript("pp.pause();");
					// Thread.sleep(2000);
					// ((JavascriptExecutor)
					// driver).executeScript("pp.play();");
					break;
				} else {
					seek(4, true);
				}
				if (!loadingSpinner()) {
					extentTest.log(LogStatus.FAIL, "In loading spinner for a really long time while seeking");
					return false;
				}
				if (count <= 0)
					return false;
				count--;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
