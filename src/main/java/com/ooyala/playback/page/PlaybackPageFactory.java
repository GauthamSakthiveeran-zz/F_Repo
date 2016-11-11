package com.ooyala.playback.page;

import org.openqa.selenium.WebDriver;

/**
 * Created by soundarya on 10/28/16.
 */

public class PlaybackPageFactory {
	private WebDriver webDriver;
	private static PlaybackPageFactory playbackPageFactory;
	private String browserName;
	private String platformName;
	private PauseValidator pause;
	private PlayValidator play;
	private SeekValidator seek;
	private FullScreenValidator fullScreen;
	private DiscoveryValidator discovery;
	private UpNextValidator upNext;
	private CCValidator closedCaption;

	private PlaybackPageFactory(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public static PlaybackPageFactory getInstance(WebDriver driver) {
		if (playbackPageFactory == null) {
			playbackPageFactory = new PlaybackPageFactory(driver);
		}
		return playbackPageFactory;
	}

	public static void destroyInstance() {
		playbackPageFactory = null;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	// methods to get all validators

	public PauseValidator getPause() {
		if (pause == null) {
			pause = new PauseValidator(webDriver);
		}
		return pause;
	}

	public PlayValidator getPlay() {
		if (play == null) {
			play = new PlayValidator(webDriver);
		}
		return play;
	}

	public SeekValidator getSeek() {
		if (seek == null) {
			seek = new SeekValidator(webDriver);
		}
		return seek;
	}

	public FullScreenValidator getFullScreen() {
		if (fullScreen == null) {
			fullScreen = new FullScreenValidator(webDriver);
		}
		return fullScreen;
	}

	public DiscoveryValidator getDiscovery() {
		if (discovery == null) {
			discovery = new DiscoveryValidator(webDriver);
		}
		return discovery;
	}

	public UpNextValidator getUpNext() {
		if (upNext == null) {
			upNext = new UpNextValidator(webDriver);
		}
		return upNext;
	}

	public CCValidator getClosedCaption() {
		if (closedCaption == null) {
			closedCaption = new CCValidator(webDriver);
		}
		return closedCaption;
	}
}
