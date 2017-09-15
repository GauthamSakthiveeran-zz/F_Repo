package com.ooyala.playback.page.action;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import com.ooyala.playback.page.PlayBackPage;

public class PlayerAPIAction extends PlayBackPage implements PlayerAction {

	public PlayerAPIAction(WebDriver webDriver) {
		super(webDriver);
	}

	@Override
	public boolean startAction() throws Exception {
		return false;
	}

	public boolean isAutoPlay() {
		return (boolean) executeJsScript("pp.parameters.autoplay", "boolean");
	}

	public boolean isAdPlaying() {
		return (boolean) executeJsScript("pp.isAdPlaying()", "boolean");
	}

	public boolean isPlaying() {
		return (boolean) executeJsScript("pp.isPlaying()", "boolean");
	}

	public int getLinearAdSkipButtonStartTime() {
		return (int) executeJsScript("pp.parameters.linearAdSkipButtonStartTime", "int");
	}

	public double getPlayAheadTime() {
		return (double) executeJsScript("pp.getPlayheadTime()", "double");
	}

	public int getPlayAheadTimeFixed() {
		return (int) executeJsScript("pp.getPlayheadTime().toFixed()", "int");
	}

	public String getPlayerState() {
		return (String) executeJsScript("pp.getState()", "string");
	}

	public double getDuration() {
		return (double) executeJsScript("pp.getDuration()", "double");
	}
	
	public int getDurationFixed() {
		return (int) executeJsScript("pp.getDuration().toFixed()", "int");
	}

	public double getInitialTime() {
		return (double) executeJsScript("pp.parameters.initialTime", "double");
	}

	public String getTitle() {
		return (String) executeJsScript("pp.getTitle()", "string");
	}

	public String getVolume() {
		return (String) executeJsScript("pp.getVolume()", "string");
	}

	public void setVolume(int volume) {
		driver.executeScript("pp.setVolume(" + volume + ")");
	}
	
	public void setVolume(float volume) {
		driver.executeScript("pp.setVolume(" + volume + ")");
	}

	public String getEmbedCode() {
		return (String) executeJsScript("pp.getEmbedCode()", "string");
	}

	public String getDescription() {
		return driver.executeScript("if((typeof pp.getDescription())==='object'){" + "return 'null'" + "}else{"
				+ "return pp.getDescription();" + "}").toString();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getCurrentItemClosedCaptionsLanguages() {
		return ((ArrayList<String>) driver
				.executeScript("return pp.getCurrentItemClosedCaptionsLanguages().languages;"));
	}

	public String getItemTitle() {
		return (String) executeJsScript("pp.getItem().title", "string");
	}

	public String getItemDescription() {
		return driver.executeScript("if((typeof pp.getItem().description)==='object'){" + "return 'null'" + "}else{"
				+ "return pp.getItem().description;" + "}").toString();
	}

	public String getItemEmbedCode() {
		return (String) executeJsScript("pp.getItem().embed_code", "string");
	}

	public void skipAd() {
		driver.executeScript("pp.skipAd()");
	}

	public void setClosedCaptionLanguage(String lang) {
		driver.executeScript("pp.setClosedCaptionsLanguage(\"" + lang + "\")");
	}

	public String getState() {
		return (String) executeJsScript("pp.getState()", "string");
	}

	public void seek(int seekTime) {
		driver.executeScript("pp.seek(" + seekTime + ")");
	}
	
	public void destroy() {
		driver.executeScript("pp.destroy()");
	}
	
	public boolean isFullscreen() {
		return Boolean.parseBoolean(driver.executeScript("return pp.isFullscreen()").toString());
	}
	
	public void play() {
        driver.executeScript("pp.play()");
    }
	
	public void pause() {
        driver.executeScript("pp.pause()");
    }

}
