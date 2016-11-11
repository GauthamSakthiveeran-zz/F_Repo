package com.ooyala.playback.factory;

import org.openqa.selenium.WebDriver;

import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.DiscoveryValidator;
import com.ooyala.playback.page.FullScreenValidator;
import com.ooyala.playback.page.PauseValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.UpNextValidator;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;

public class PlayBackFactory {

	private static PlayBackFactory playbackFactory;
	private WebDriver driver;

	private CCValidator ccValidator;
	private DiscoveryValidator discoveryValidator;
	private FullScreenValidator fullScreenValidator;
	private PauseValidator pauseValidator;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private UpNextValidator upNextValidator;
	private PauseAction pauseAction;
	private PlayAction playAction;

	private PlayBackFactory(WebDriver driver) {
		this.driver = driver;
	}

	public PauseAction getPauseAction() {
		if (pauseAction == null)
			pauseAction = new PauseAction(driver);
		return pauseAction;
	}

	public PlayAction getPlayAction() {
		if (playAction == null)
			playAction = new PlayAction(driver);
		return playAction;
	}

	public CCValidator getCCValidator() {
		if (ccValidator == null)
			ccValidator = new CCValidator(driver);
		return ccValidator;
	}

	public UpNextValidator getUpNextValidator() {
		if (upNextValidator == null)
			upNextValidator = new UpNextValidator(driver);
		return upNextValidator;
	}

	public SeekValidator getSeekValidator() {
		if (seekValidator == null)
			seekValidator = new SeekValidator(driver);
		return seekValidator;
	}

	public PlayValidator getPlayValidator() {
		if (playValidator == null)
			playValidator = new PlayValidator(driver);
		return playValidator;
	}

	public PauseValidator getPauseValidator() {
		if (pauseValidator == null)
			pauseValidator = new PauseValidator(driver);
		return pauseValidator;
	}

	public FullScreenValidator getFullScreenValidator() {
		if (fullScreenValidator == null)
			fullScreenValidator = new FullScreenValidator(driver);
		return fullScreenValidator;
	}

	public DiscoveryValidator getDiscoveryValidator() {
		if (discoveryValidator == null)
			discoveryValidator = new DiscoveryValidator(driver);
		return discoveryValidator;
	}

	public static PlayBackFactory getInstance(WebDriver driver) {
		if (playbackFactory == null || playbackFactory.getDriver() == null) {
			playbackFactory = new PlayBackFactory(driver);
		}
		return playbackFactory;
	}

	public static void destroyInstance() {
		playbackFactory = null;
	}

	public WebDriver getDriver() {
		return driver;
	}
}
