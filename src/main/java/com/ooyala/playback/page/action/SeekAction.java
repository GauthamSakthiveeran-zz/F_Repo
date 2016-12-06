package com.ooyala.playback.page.action;

import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author dmanohar
 *
 */
public class SeekAction extends PlayBackPage implements PlayerAction {

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
			throw new Exception(
					"Time to seek needs to be set! or seekTillEnd should be set to true");
		}

		if (!adPlugin.isEmpty()) {
			Map<String, String> data = parseURL();
			if (data.get("ad_plugin") != null
					&& !data.get("ad_plugin").equals(adPlugin)) {
				extentTest.log(LogStatus.SKIP,
						"This particular step is skipped as it is valid only for "
								+ adPlugin);
				return true;
			}

		}

		if (seekTillEnd && factor == 1) {
			seekPlayback();
			seekTillEnd = false;
			;
		} else {
			if (fromLast) {
				seek(time, fromLast);
			} else {
				seek(time + "");
			}
		}

		return true;// need to check for unable to seek and return true or false
					// accordingly;
	}

	public String getDuration() {
		return "pp.getDuration()/" + factor;
	}

	private void seek(int time, boolean fromLast) throws Exception {
		String seekduration;
		if (fromLast) {
			seekduration = getDuration();
		} else {
			seekduration = "";
		}
		seek(seekduration + "-" + time + "");
		factor = 1;
	}

	private void seek(String time) throws Exception {
		((JavascriptExecutor) driver).executeScript("return pp.seek(" + time
				+ ")" + "");
	}

	// forwarding the video to end to complete the testing instead of waiting
	// for whole video to play
	private void seekPlayback() throws Exception {
		while (true) {
			double seekTime = Double.parseDouble(((JavascriptExecutor) driver)
					.executeScript("return pp.getPlayheadTime();").toString());
			if (seekTime == -1) {
				logger.error("Video is in error mode");
				break;
			}
			if (seekTime > 5) {
				// Update after ticket is fixed pp.seek() api is not working if
				// we try to seek less than 31 seconds form end of video
				if (!getBrowser().equalsIgnoreCase("safari")) {
					seek(7, true);
				} else {
					seek(31, true);
				}
				((JavascriptExecutor) driver).executeScript("pp.pause();");
				Thread.sleep(2000);
				((JavascriptExecutor) driver).executeScript("pp.play();");
				break;
			}
		}
		Thread.sleep(10000);
	}

	// As there is problem for pulse asset that if we seek the video then ads
	// get skip therefore adding below condition

	public void seekSpecific(int time) throws Exception {

		Map<String, String> data = parseURL();
		boolean flag = false;

		if (data != null) {
			String videoPlugin = data.get("video_plugins");
			String adPlugin = data.get("ad_plugin");
			if (videoPlugin != null && adPlugin != null) {
				if (adPlugin.contains("pulse")) {
					if (videoPlugin.contains("bit_wrapper")
							|| videoPlugin.contains("main")) {
						flag = true;
					}
				} else if (videoPlugin.contains("bit_wrapper")
						&& adPlugin.contains("ima")) {
					flag = true;
				}
				if (flag) {
					setTime(time).fromLast().startAction();
				}
			}
		}

	}
}
