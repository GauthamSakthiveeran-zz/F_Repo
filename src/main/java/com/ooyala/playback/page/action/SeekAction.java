package com.ooyala.playback.page.action;

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

	public void seek(int time, boolean fromLast) throws Exception {
		String seekduration;
		if (fromLast) {
			seekduration = "pp.getDuration()";
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
			if (Double.parseDouble(((JavascriptExecutor) driver).executeScript(
					"return pp.getPlayheadTime();").toString()) > 5) {
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

	public void seekSpecific(Url urlData, int time) throws Exception {
		if (urlData.getAdPlugins().getName().equals("PULSE")) {
			if (urlData.getPlugins().getName().contains("BITMOVIN")
					|| urlData.getPlugins().getName().contains("MAIN")) {
				seek(time, true);
			}
		} else if (urlData.getPlugins().getName().equals("BITMOVIN")
				&& urlData.getAdPlugins().getName().equals("IMA")) {
			seek(time, true);
		}
	}
}
