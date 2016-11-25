package com.ooyala.playback.page.action;

import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;
import com.ooyala.playback.url.Url;

/**
 * 
 * @author dmanohar
 *
 */
public class SeekAction extends PlayBackPage implements PlayerAction {

	public SeekAction(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("play");
	}

	@Override
	public void startAction() throws Exception {
		// TODO Auto-generated method stub

	}
	
	public String getDuration(int factor){
		return "pp.getDuration()/"+factor;
	}

	public void seek(int time, boolean fromLast) throws Exception {
		String seekduration;
		if (fromLast) {
			seekduration = getDuration(1);
		} else {
			seekduration = "";
		}
		seek(seekduration + "-" + time + "");

	}

	public void seek(String time) throws Exception {
		((JavascriptExecutor) driver).executeScript("return pp.seek(" + time
				+ ")" + "");
	}

	// forwarding the video to end to complete the testing instead of waiting
	// for whole video to play
	public void seekPlayback() throws Exception {
		while (true) {
			double seekTime = Double.parseDouble(((JavascriptExecutor) driver)
					.executeScript("return pp.getPlayheadTime();").toString());
			if (seekTime == -1) {
				logger.error("Video is in error mode");
				break;
			}
			if (seekTime > 5) {
				seek(7, true);
				// loadingSpinner(webDriver);
				((JavascriptExecutor) driver).executeScript("pp.pause();");
				Thread.sleep(2000);
				((JavascriptExecutor) driver).executeScript("pp.play();");
				break;
			}
		}
	}

	// As there is problem for pulse asset that if we seek the video then ads
	// get skip therefore adding below condition

	public void seekSpecific(Map<String,String> data, int time) throws Exception {
		boolean flag = false;
		
		if(data!=null){
			String videoPlugin = data.get("video_plugins");
			String adPlugin = data.get("ad_plugin");
			if(videoPlugin!=null && adPlugin!=null){
				if (adPlugin.contains("pulse")) {
					if (videoPlugin.contains("bit_wrapper")
							|| videoPlugin.contains("main")) {
						flag = true;
					}
				} else if (videoPlugin.contains("bit_wrapper")
						&& adPlugin.contains("ima")) {
					flag = true;
				}
				if(flag){
					seek(time, true);
				}
			}
		}
		
		
	}
}
