package com.ooyala.playback.page;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.PlayerAPIAction;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 29/12/16.
 */
public class PlayerAPIValidator extends PlayBackPage implements PlaybackValidator {

	private static final Logger logger = Logger.getLogger(PlayerAPIValidator.class);
	PlayerAPIAction playerAPI;

	public PlayerAPIValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(webDriver, this);
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("adclicks");
		addElementToPageElements("fullscreen");
		playerAPI = new PlayBackFactory(webDriver, extentTest).getPlayerAPIAction();
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		PlayBackFactory factory = new PlayBackFactory(driver, extentTest);

		/***
		 * For android autoPlay does not work and therefore we are not checking
		 * autoplay in following if statement Therefore we are playing video
		 * explicitly in following else statement....
		 */
		if (!getPlatform().equalsIgnoreCase("android")) {
			Boolean autoPlay = playerAPI.isAutoPlay();

			if (!autoPlay) {
				logger.error("Not able to Autoplay");
				extentTest.log(LogStatus.FAIL, "Not able to Autoplay");
				return false;
			}
		} else {
			if (!(factory.getPlayValidator().waitForPage() && factory.getPlayAction().startAction())) {
				extentTest.log(LogStatus.FAIL, "Unable to play the video");
				return false;
			}
		}

		if (!waitOnElement("AD_PANEL", 10000)) {
			logger.error("ad is not playing");
			extentTest.log(LogStatus.FAIL, "ad is not playing");
			return false;
		}

		Boolean isAdPlaying = playerAPI.isAdPlaying();
		logger.info("is Ad playing :" + isAdPlaying);
		extentTest.log(LogStatus.INFO, "is Ad playing :" + isAdPlaying);
		if (!isAdPlaying) {
			logger.error("Ad not playing");
			extentTest.log(LogStatus.FAIL, "Ad not playing");
			return false;
		}

		int showAdSkipBtnTime = playerAPI.getLinearAdSkipButtonStartTime();

		while (true) {
			double adPlayHeadTime = playerAPI.getPlayAheadTime();
			if (adPlayHeadTime > (double) showAdSkipBtnTime) {
				playerAPI.skipAd();
				break;
			}
		}

		if (!waitOnElement(By.id("skipAd_1"), 5000)) {
			logger.error("Not able to skip the ad");
			extentTest.log(LogStatus.FAIL, "Not able to skip the ad");
			return false;
		}

		if (!loadingSpinner()) {
			extentTest.log(LogStatus.FAIL, "loading spinner is present for long time");
			return false;
		}

		if (!waitOnElement(By.id("videoPlaying_1"), 10000)) {
			logger.error("Video is not playing");
			extentTest.log(LogStatus.FAIL, "Video is not playing");
			return false;
		}

		/***
		 * As for Android initialTime parameter not working, we are not checking
		 * condition in following if statement for android platform
		 */
		if (!getPlatform().equalsIgnoreCase("android")) {

			double initialTime = playerAPI.getInitialTime();
			double playHeadTime = playerAPI.getPlayAheadTime();

			if (!(playHeadTime > initialTime)) {
				logger.error("Video playback not started from initial time");
				extentTest.log(LogStatus.FAIL, "Video playback not started from initial time");
				return false;
			}
		}

		// get the total duration of the video
		double totalTime = playerAPI.getDuration();
		logger.info("Duration of video is : " + totalTime);
		if (!(totalTime > 0)) {
			logger.error("Total time must be greater than 0 but we are getting it as :" + totalTime);
			extentTest.log(LogStatus.FAIL, "Total time must be greater than 0 but we are getting it as :" + totalTime);
			return false;
		}

		if (!loadingSpinner()) {
			logger.error("loading spinner is present for long time");
			extentTest.log(LogStatus.FAIL, "loading spinner is present for long time");
			return false;
		}

		factory.getSeekAction().setTime(10).fromLast().startAction();

		if (!loadingSpinner()) {
			logger.error("loading spinner is present for long time");
			extentTest.log(LogStatus.FAIL, "loading spinner is present for long time");
			return false;
		}

		if (!waitOnElement(By.id("seeked_1"), 10000)) {
			logger.error("Not able to seek the video...");
			extentTest.log(LogStatus.FAIL, "Not able to seek the video...");
			return false;
		}

		String title = playerAPI.getTitle();
		if (title == null) {
			logger.error("Not able to get title of the video");
			extentTest.log(LogStatus.FAIL, "Not able to get title of the video");
			return false;
		}

		String getVolume = playerAPI.getVolume();
		logger.info("Volume is : " + getVolume);
		if (getVolume == null) {
			logger.error("Not able to get volume of the video");
			extentTest.log(LogStatus.FAIL, "Not able to get volume of the video");
			return false;
		}

		String embedCode = playerAPI.getEmbedCode();
		if (embedCode == null) {
			logger.error("Not able to get Embed code");
			extentTest.log(LogStatus.FAIL, "Not able to get Embed code");
			return false;
		}

		ArrayList<String> langList = playerAPI.getCurrentItemClosedCaptionsLanguages();
		for (int i = 0; i < langList.size(); i++) {
			playerAPI.setClosedCaptionLanguage(langList.get(i));
			if (!waitOnElement(By.id("cclanguage_" + langList.get(i)), 10000)) {
				logger.error("Not able to get " + langList.get(i));
				extentTest.log(LogStatus.FAIL, "Not able to get " + langList.get(i));
				return false;
			}
		}

		return true;
	}

	public boolean validatePlayPauseSeekAPI() {
		return validateDescriptionAPI(playerAPI.getItemDescription())
				&& validateEmbedCodeAPI(playerAPI.getItemEmbedCode()) && executeGetItemAPI(playerAPI.getItemTitle())
				&& validatePlayAPI() && validatePauseAPI() && validateSeekAPI();
	}

	public boolean validateInitialTime() {
		double initialTime = playerAPI.getInitialTime();

		double playHeadTime = playerAPI.getPlayAheadTime();

		if (!(playHeadTime > initialTime)) {
			extentTest.log(LogStatus.FAIL, "Video playback not started from initial time");
			logger.error("Video playback not started from initial time");
			return false;
		}
		return true;
	}

	private boolean validateDescriptionAPI(String description) {
		logger.info(
				"************************************** validating description API ******************************************************");
		String expectedDesciption = playerAPI.getDescription();
		if (!description.equals(expectedDesciption)) {
			logger.error(
					"Description is not matching ... Expected : " + description + "\n Actual :" + expectedDesciption);
			extentTest.log(LogStatus.FAIL,
					"Description is not matching ... Expected : " + description + "\n Actual :" + expectedDesciption);
			return false;
		}
		logger.info("Description is matching ... Expected : " + description + "\n Actual :" + expectedDesciption);
		extentTest.log(LogStatus.PASS,
				"Description is matching ... Expected : " + description + "\n Actual :" + expectedDesciption);
		return true;
	}

	private boolean validateEmbedCodeAPI(String embedCode) {
		logger.info(
				"******************************************* validating embed code API ********************************************************");
		String expectedEmbedCode = playerAPI.getEmbedCode();
		if (!embedCode.equals(expectedEmbedCode)) {
			logger.error("Embed Code is not matching ... Expected : " + embedCode + "\n Actual :" + expectedEmbedCode);
			extentTest.log(LogStatus.FAIL,
					"Embed Code is not matching ... Expected : " + embedCode + "\n Actual :" + expectedEmbedCode);
			return false;
		}
		logger.info("Embed Code is matching ... Expected : " + embedCode + "\n Actual :" + expectedEmbedCode);
		extentTest.log(LogStatus.PASS,
				"Embed Code is matching ... Expected : " + embedCode + "\n Actual :" + expectedEmbedCode);
		return true;
	}

	private boolean executeGetItemAPI(String title) {
		logger.info(
				"*********************************** validating title ************************************************************");
		String expectedTitle = playerAPI.getTitle();
		if (!title.equals(expectedTitle)) {
			logger.error("Title is not matching ... Expected : " + title + "\n Actual :" + expectedTitle);
			extentTest.log(LogStatus.FAIL,
					"Title is not matching ... Expected : " + title + "\n Actual :" + expectedTitle);
			return false;
		}
		logger.info("Title is matching ... Expected : " + title + "\n Actual :" + expectedTitle);
		extentTest.log(LogStatus.PASS, "Title is matching ... Expected : " + title + "\n Actual :" + expectedTitle);
		return true;
	}

	public boolean validatePlayAPI() {
		logger.info(
				"******************************************* validating play API ****************************************************");
		try {
			// Start playback
			playerAPI.play();
		} catch (Exception ex) {
			logger.error("getting exception while playing video using pp.play() API");
			extentTest.log(LogStatus.FAIL, "getting exception while playing video using pp.play() API");
			return false;
		}

		// Check playing event
		if (!waitOnElement(By.id("playing_1"), 20000)) {
			logger.error("playing event is not getting triggered");
			extentTest.log(LogStatus.FAIL, "playing event is not getting triggered");
			return false;
		}
		logger.info("playing event gets triggered");
		extentTest.log(LogStatus.PASS, "playing event gets triggered");

		// Check isPlaying api return true or false
		boolean isPlaying = playerAPI.isPlaying();
		if (!isPlaying) {
			logger.error("isPlaying() API returns " + isPlaying + " while video is playing");
			extentTest.log(LogStatus.FAIL, "isPlaying() API returns " + isPlaying + " while video is playing");
			return false;
		}
		logger.info("isPlaying() API returns " + isPlaying + " while video is playing");
		extentTest.log(LogStatus.PASS, "isPlaying() API returns " + isPlaying + " while video is playing");

		// Validate playing State
		String playingState = playerAPI.getState();
		if (!playingState.equalsIgnoreCase("playing")) {
			logger.error("Not getting playing State for pp.getState() API.\n Getting State while video is Playing :"
					+ playingState);
			extentTest.log(LogStatus.FAIL,
					"Not getting playing State for pp.getState() API.\n Getting State while video is Playing :"
							+ playingState);
			return false;
		}
		logger.info("Getting playing State for pp.getState() API as " + playingState);
		extentTest.log(LogStatus.PASS, "Getting playing State for pp.getState() API as " + playingState);

		return new PlayBackFactory(driver, extentTest).getEventValidator().playVideoForSometime(3);

	}

	public boolean validatePauseAPI() {
		logger.info(
				"********************************************* validating pause API *****************************************");
		try {
			// Pause video
			playerAPI.pause();
		} catch (Exception ex) {
			logger.error("getting exception while pausing the video using pp.pause() API");
			return false;
		}

		// Check playing event
		if (!waitOnElement(By.id("paused_1"), 20000)) {
			logger.error("pause event is not getting triggered");
			extentTest.log(LogStatus.FAIL, "pause event is not getting triggered");
			return false;
		}
		logger.info("pause event gets triggered");
		extentTest.log(LogStatus.PASS, "pause event gets triggered");

		// Check isPlaying api return true or false
		boolean isPause = playerAPI.isPlaying();
		if (isPause) {
			logger.error("isPlaying() API returns " + isPause + " while video is pause");
			extentTest.log(LogStatus.FAIL, "isPlaying() API returns " + isPause + " while video is pause");
			return false;
		}
		logger.info("isPlaying() API returns " + isPause + " while video is pause");
		extentTest.log(LogStatus.PASS, "isPlaying() API returns " + isPause + " while video is pause");

		// Validate pause State
		String pauseState = playerAPI.getState();
		if (!pauseState.equalsIgnoreCase("paused")) {
			logger.error("Not getting paused State for pp.getState() API.\n Getting State while video is paused :"
					+ pauseState);
			extentTest.log(LogStatus.FAIL,
					"Not getting paused State for pp.getState() API.\n Getting State while video is paused :"
							+ pauseState);
			return false;
		}
		logger.info("Getting paused State for pp.getState() API as " + pauseState);
		extentTest.log(LogStatus.PASS, "Getting paused State for pp.getState() API as " + pauseState);

		return true;
	}

	public boolean validateSeekAPI() {
		logger.info(
				"*************************************** validating seek API *****************************************************");
		boolean isAdPlaying = playerAPI.isAdPlaying();
		logger.info("isAdPlaying :" + isAdPlaying);
		if (!isAdPlaying) {
			boolean isVideoPlaying = playerAPI.isPlaying();
			logger.info("isVideoPlaying :" + isVideoPlaying);
			if (isVideoPlaying) {
				if (!validatePauseAPI()) {
					logger.error("Not able to pause before seeking the video");
					extentTest.log(LogStatus.FAIL, "Not able to pause before seeking the video");
					return false;
				}
				logger.info("video paused before seeking the video");
				extentTest.log(LogStatus.PASS, "video paused before seeking the video");
			}
			int seekTime = playerAPI.getDurationFixed() - 10;
			logger.info("seekTime :" + seekTime);
			extentTest.log(LogStatus.INFO, "seekTime :" + seekTime);
			try {
				playerAPI.seek(seekTime);
				logger.info("No issue while seeking video using pp.seek() API");
			} catch (Exception ex) {
				logger.error("getting exception while seeking the video");
				extentTest.log(LogStatus.FAIL, "getting exception while seeking the video");
				return false;
			}
			if (!waitOnElement(By.id("seeked_1"), 20000)) {
				logger.error("Seek event is not triggering");
				extentTest.log(LogStatus.FAIL, "Seek event is not triggering");
				return false;
			}
			logger.info("Seek event is triggered");
			extentTest.log(LogStatus.PASS, "Seek event is triggered");

			int currentPlayHeadTime = playerAPI.getPlayAheadTimeFixed();
			logger.info("currentPlayHeadTime = " + currentPlayHeadTime);
			extentTest.log(LogStatus.INFO, "currentPlayHeadTime = " + currentPlayHeadTime);
			if (currentPlayHeadTime != seekTime) {
				logger.error("current playhead time and seek Time is not matching after seeking the video..");
				extentTest.log(LogStatus.FAIL,
						"current playhead time and seek Time is not matching after seeking the video..");
				return false;
			}
			logger.info("current playhead time and seek Time is matching after seeking the video..");
			extentTest.log(LogStatus.PASS, "current playhead time and seek Time is matching after seeking the video..");
		}
		return true;
	}

	public boolean validateVolumeAPI() {
		logger.info(
				"**************************** Validating Volume API *******************************************************");
		// Set volume to mute
		playerAPI.setVolume(0);
		int muteVol = Integer.parseInt(playerAPI.getVolume());
		if (muteVol != 0) {
			logger.error("volume is not getting mute");
			extentTest.log(LogStatus.FAIL, "volume is not getting mute");
			return false;
		}
		if (!waitOnElement(By.id("volumeChanged_1"), 10000) || !waitOnElement(By.id("changeVolume_1"), 10000)) {
			logger.error(
					"Either of the following events didn't gets triggered when volume is set to MUTE: \n 1--> volumeChanged \n 2--> changeVolume");
			extentTest.log(LogStatus.FAIL,
					"Either of the following events didn't gets triggered when volume is set to MUTE: \n 1--> volumeChanged \n 2--> changeVolume");
			return false;
		}
		logger.info("verified mute volume");
		extentTest.log(LogStatus.PASS, "verified mute volume");

		// Set volume to 0.4
		playerAPI.setVolume(0.4f);
		float intMidVol = Float.parseFloat(playerAPI.getVolume());
		if (intMidVol != 0.4f) {
			logger.error("volume is not getting set to 0.4");
			extentTest.log(LogStatus.FAIL, "volume is not getting set to 0.4");
			return false;
		}
		if (!waitOnElement(By.id("volumeChanged_2"), 10000) || !waitOnElement(By.id("changeVolume_2"), 10000)) {
			logger.error(
					"Either of the following events didn't gets triggered when volume is set to intermediate volume level: \n 1--> volumeChanged \n 2--> changeVolume");
			extentTest.log(LogStatus.FAIL,
					"Either of the following events didn't gets triggered when volume is set to intermediate volume level: \n 1--> volumeChanged \n 2--> changeVolume");
			return false;
		}
		logger.info("verified intermediate volume");
		extentTest.log(LogStatus.PASS, "verified intermediate volume");

		// Set volume to 1
		playerAPI.setVolume(1);
		int highVol = Integer.parseInt(playerAPI.getVolume());
		if (highVol != 1) {
			logger.error("volume is not set to high i.e 1");
			return false;
		}
		if (!waitOnElement(By.id("volumeChanged_3"), 10000) || !waitOnElement(By.id("changeVolume_3"), 10000)) {
			logger.error(
					"Either of the following events didn't gets triggered when volume is set to HIGH level: \n 1--> volumeChanged \n 2--> changeVolume");
			extentTest.log(LogStatus.FAIL,
					"Either of the following events didn't gets triggered when volume is set to HIGH level: \n 1--> volumeChanged \n 2--> changeVolume");
			return false;
		}
		logger.info("verified high volume");
		extentTest.log(LogStatus.PASS, "verified high volume");
		return true;
	}

	public boolean validateDestroyAPI() {

		logger.info(
				"******************************** validating Destroy API **********************************************");
		boolean isPlaying = playerAPI.isPlaying();

		if (!isPlaying) {
			playerAPI.play();
			loadingSpinner();
		}

		try {
			playerAPI.destroy();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			extentTest.log(LogStatus.FAIL, ex);
			return false;
		}

		// Validate Destroy event ...
		if (!waitOnElement(By.id("destroy_1"), 20000) || !waitOnElement(By.id("videoElementDisposed_1"), 20000)
				|| !waitOnElement(By.id("videoElementLostFocus_1"), 20000)) {
			logger.error(
					"either of the following event not triggered : \n 1--> Destroy \n 2--> videoElementDisposed_1 \n 3--> videoElementLostFocus");
			extentTest.log(LogStatus.FAIL,
					"either of the following event not triggered : \n 1--> Destroy \n 2--> videoElementDisposed_1 \n 3--> videoElementLostFocus");
			return false;
		}
		logger.info(
				"Following events gets triggered : \n 1--> Destroy \n 2--> videoElementDisposed_1 \n 3--> videoElementLostFocus");
		extentTest.log(LogStatus.PASS,
				"Following event gets triggered : \n 1--> Destroy \n 2--> videoElementDisposed_1 \n 3--> videoElementLostFocus");

		// Validate destroy State
		String destroyState = playerAPI.getState();
		if (!destroyState.equalsIgnoreCase("destroyed")) {
			logger.error("Not getting destroyed State for pp.getState() API.\n Getting State while video is paused :"
					+ destroyState);
			extentTest.log(LogStatus.FAIL,
					"Not getting destroyed State for pp.getState() API.\n Getting State while video is paused :"
							+ destroyState);
			return false;
		}
		logger.info("Getting destroyed State for pp.getState() API as " + destroyState);
		extentTest.log(LogStatus.PASS, "Getting destroyed State for pp.getState() API as " + destroyState);
		return true;
	}

	public boolean validateFullScreenAPI() {
		logger.info(
				"***************************** Validate FullScreen API ****************************************************");
		boolean isNormalScreen = false;
		boolean isFullScreen = false;
		isNormalScreen = playerAPI.isFullscreen();
		logger.info("isNormalScreen : " + isNormalScreen);
		extentTest.log(LogStatus.INFO, "isNormalScreen : " + isNormalScreen);
		if (!isNormalScreen) {
			if (!clickOnIndependentElement("FULLSCREEN_BTN_1")) {
				logger.error("Not able to click on fullscreen button");
				extentTest.log(LogStatus.FAIL, "Not able to click on fullscreen button");
				return false;
			}
			isFullScreen = playerAPI.isFullscreen();
			logger.info("isFullScreen :" + isFullScreen);
			extentTest.log(LogStatus.INFO, "isFullScreen :" + isFullScreen);
			if (!isFullScreen) {
				logger.error("pp.isFullscreen() API does not return true when video is playing in fullscreen mode");
				extentTest.log(LogStatus.FAIL,
						"pp.isFullscreen() API does not return true when video is playing in fullscreen");
				return false;
			}
			logger.info("pp.isFullscreen() API does returns true when video is playing in fullscreen mode");
			extentTest.log(LogStatus.PASS,
					"pp.isFullscreen() API does returns true when video is playing in fullscreen");
			if (!clickOnIndependentElement("NORMAL_SCREEN")) {
				logger.error("Not able to click on normalscreen button");
				extentTest.log(LogStatus.FAIL, "Not able to click on normalscreen button");
				return false;
			}
			isNormalScreen = playerAPI.isFullscreen();
			if (isNormalScreen) {
				logger.info("pp.isFullscreen() API does not return false when video is playing in normalscreen mode");
				extentTest.log(LogStatus.PASS,
						"pp.isFullscreen() API does not return false when video is playing in normalscreen");
				return false;
			}
			logger.info("pp.isFullscreen() API does returns false when video is playing in normalscreen mode");
			extentTest.log(LogStatus.PASS,
					"pp.isFullscreen() API does returns false when video is playing in normalscreen");
		}
		return true;
	}

	public boolean validateDurationForLive() {
		logger.info("********************** Validating Duration for Live VIDEO ************************************");

		/***
		 * Here expected duration for Live video is Infinity
		 */
		String liveDuration = playerAPI.getDuration() + "";
		if (!liveDuration.equalsIgnoreCase("infinity")) {
			logger.error("Total duration for Live video is not Infinity");
			extentTest.log(LogStatus.FAIL, "Total duration for Live video is not Infinity");
			return false;
		}
		logger.info("Total duration for Live video is Infinity");
		extentTest.log(LogStatus.PASS, "Total duration for Live video is Infinity");
		return true;
	}

	public boolean validateDurationAtEndScreen() {
		logger.info(
				"***************************** Validating Duration at end Screen **************************************");
		boolean isPlaying;
		int duration = playerAPI.getDurationFixed();
		// get the total lenght of the video
		logger.info("Total Duration of the video is :" + duration);
		isPlaying = playerAPI.isPlaying();
		if (!isPlaying) {
			if (!validatePlayAPI()) {
				return false;
			}
		}
		playerAPI.seek(duration);
		int playHeadTime = playerAPI.getPlayAheadTimeFixed();
		if (duration != playHeadTime) {
			logger.error("playhead time and total duration is not matching at End Screen");
			extentTest.log(LogStatus.FAIL, "playhead time and total duration is not matching at End Screen");
			return false;
		}
		logger.info("playhead time and total duration is matching at End Screen");
		extentTest.log(LogStatus.PASS, "playhead time and total duration is matching at End Screen");
		return true;
	}

	public boolean validateCloseCaptionAPI() {
		logger.info(
				"***************************** Validating Close Caption CC API *********************************************");

		try {
			boolean isPlaying = playerAPI.isPlaying();
			if (!isPlaying) {
				playerAPI.play();
				if (!loadingSpinner()) {
					logger.error("vodeo is buffering for too long time");
					extentTest.log(LogStatus.FAIL, "vodeo is buffering for too long time");
					return false;
				}
				boolean isPlayingAfterPlayByApi = playerAPI.isPlaying();
				logger.warn("isPlayingAfterPlayByApi :" + isPlayingAfterPlayByApi);
			}
			ArrayList<String> langList = playerAPI.getCurrentItemClosedCaptionsLanguages();
			for (int i = 0; i < langList.size(); i++) {
				playerAPI.setClosedCaptionLanguage(langList.get(i));
				if (!waitOnElement(By.id("cclanguage_" + langList.get(i)), 10000)) {
					logger.error("Not able to get " + langList.get(i));
					extentTest.log(LogStatus.FAIL, "Not able to get " + langList.get(i));
					return false;
				}
				logger.info("close caption is set to language : " + langList.get(i));
				extentTest.log(LogStatus.PASS, "close caption is set to language : " + langList.get(i));
			}
		} catch (Exception ex) {
			logger.error(ex);
			extentTest.log(LogStatus.FAIL, ex.getMessage());
		}
		return true;
	}

	public boolean validateAPIForPreroll() {
		logger.info("*************************** Validate All API's for Preroll Ad *********************************");
		if (!(validateDescriptionAPI(playerAPI.getItemDescription())
				&& validateEmbedCodeAPI(playerAPI.getItemEmbedCode()) && executeGetItemAPI(playerAPI.getItemTitle()))) {
			return false;
		}

		boolean isVideoPlaying = playerAPI.isAdPlaying();
		if (!isVideoPlaying) {
			playerAPI.play();
		}

		if (!loadingSpinner()) {
			logger.error("Loading spinner is present for long time after playing the video");
			extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after playing the video");
			return false;
		}

		if (!waitOnElement(By.id("PreRoll_willPlaySingleAd_1"), 10000)) {
			logger.error("preroll ad is not playing or present for this asset");
			extentTest.log(LogStatus.FAIL, "preroll ad is not playing or present for this asset");
			return false;
		}

		boolean isAdPlaying = playerAPI.isAdPlaying();
		if (!isAdPlaying) {
			logger.error("isAdPlaying() API does not return true when ad is playing");
			extentTest.log(LogStatus.FAIL, "isAdPlaying() API does not return true when ad is playing");
		}
		logger.info("isAdPlaying() API does return true when ad is playing");
		extentTest.log(LogStatus.PASS, "isAdPlaying() API does return true when ad is playing");

		if (isAdPlaying) {
			playerAPI.pause();
			if (!waitOnElement(By.id("videoPausedAds_1"), 10000)) {
				logger.error("Does not found videoPausedAds event after video gets paused");
				extentTest.log(LogStatus.FAIL, "Does not found videoPausedAds event after video gets paused");
				return false;
			}
			logger.info("Found videoPausedAds event after video gets paused");
			extentTest.log(LogStatus.PASS, "Found videoPausedAds event after video gets paused");

			playerAPI.skipAd();
			if (!waitOnElement(By.id("skipAd_1"), 10000)) {
				logger.error("Does not found skipAd event after ad gets skipped");
				extentTest.log(LogStatus.FAIL, "Does not found skipAd event after ad gets skipped");
				return false;
			}
			logger.info("Found skipAd event after ad gets skipped");
			extentTest.log(LogStatus.PASS, "Found skipAd event after ad gets skipped");

			if (!loadingSpinner()) {
				logger.error("Loading spinner is present for long time after skipping the ad");
				extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after skipping the ad");
				return false;
			}

			if (!waitOnElement(By.id("playing_1"), 10000)) {
				logger.error("Does not found Playing event after ad gets skipped");
				extentTest.log(LogStatus.FAIL, "Does not found Playing event after ad gets skipped");
				return false;
			}
			logger.info("Found Playing event after ad gets skipped");
			extentTest.log(LogStatus.PASS, "Found Playing event after ad gets skipped");

			boolean isPlaying = playerAPI.isPlaying();
			if (!isPlaying) {
				logger.error("isPlaying() API returns " + isPlaying + " while video is playing");
				extentTest.log(LogStatus.FAIL, "isPlaying() API returns " + isAdPlaying + " while video is playing");
				return false;
			}
			logger.info("isPlaying() API returns " + isPlaying + " while video is playing");
			extentTest.log(LogStatus.PASS, "isPlaying() API returns " + isPlaying + " while video is playing");
		}
		return true;
	}

	public boolean validateAPIForMidroll() {
		logger.info("*************************** Validate All API's for Midroll Ad *********************************");

		try {
			boolean isVideoPlaying = playerAPI.isPlaying();
			if (!isVideoPlaying) {
				if (!(validateDescriptionAPI(playerAPI.getItemDescription())
						&& validateEmbedCodeAPI(playerAPI.getItemEmbedCode())
						&& executeGetItemAPI(playerAPI.getItemTitle()))) {
					return false;
				}
				playerAPI.play();
			}

			if (!loadingSpinner()) {
				logger.error("Loading spinner is present for long time after playing the video");
				extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after playing the video");
				return false;
			}

			if (!new PlayBackFactory(driver, extentTest).getEventValidator().playVideoForSometime(2)) {
				logger.error("failed to play video for some time");
				extentTest.log(LogStatus.FAIL, "failed to play video for some time");
				return false;
			}

			int seek = playerAPI.getDurationFixed() - 15;
			playerAPI.seek(seek);

			if (!loadingSpinner()) {
				logger.error("Loading spinner is present for long time after playing the video");
				extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after playing the video");
				return false;
			}

			if (!waitOnElement(By.id("MidRoll_willPlayAds"), 30000)) {
				logger.error("Midroll ad is not playing or not present for this asset");
				extentTest.log(LogStatus.FAIL, "Midroll ad is not playing or not present for this asset");
				return false;
			}

			if (!new PlayBackFactory(driver, extentTest).getEventValidator().playVideoForSometime(2)) {
				logger.error("failed to play video for some time");
				extentTest.log(LogStatus.FAIL, "failed to play video for some time");
				return false;
			}

			boolean isAdPlaying = playerAPI.isAdPlaying();
			if (!isAdPlaying) {
				logger.error("isAdPlaying() API does not return true when ad is playing");
				extentTest.log(LogStatus.FAIL, "isAdPlaying() API does not return true when ad is playing");
			}
			logger.info("isAdPlaying() API does return true when ad is playing");
			extentTest.log(LogStatus.PASS, "isAdPlaying() API does return true when ad is playing");

			if (isAdPlaying) {
				playerAPI.pause();
				if (!waitOnElement(By.id("videoPausedAds_1"), 30000)) {
					logger.error("Does not found videoPausedAds event after ad gets paused");
					extentTest.log(LogStatus.FAIL, "Does not found videoPausedAds event after ad gets paused");
					return false;
				}
				logger.info("Found videoPausedAds event after video gets paused");
				extentTest.log(LogStatus.PASS, "Found videoPausedAds event after video gets paused");

				playerAPI.skipAd();
				if (!waitOnElement(By.id("skipAd_1"), 10000)) {
					logger.error("Does not found skipAd event after ad gets skipped");
					extentTest.log(LogStatus.FAIL, "Does not found skipAd event after ad gets skipped");
					return false;
				}
				logger.info("Found skipAd event after ad gets skipped");
				extentTest.log(LogStatus.PASS, "Found skipAd event after ad gets skipped");

				if (!loadingSpinner()) {
					logger.error("Loading spinner is present for long time after skipping the ad");
					extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after skipping the ad");
					return false;
				}

				if (!waitOnElement(By.id("playing_2"), 10000)) {
					logger.error("Does not found Playing event after ad gets skipped");
					extentTest.log(LogStatus.FAIL, "Does not found Playing event after ad gets skipped");
					return false;
				}
				logger.info("Found Playing event after ad gets skipped");
				extentTest.log(LogStatus.PASS, "Found Playing event after ad gets skipped");
				boolean isPlaying = playerAPI.isPlaying();
				if (!isPlaying) {
					logger.error("isPlaying() API returns " + isPlaying + " while video is playing");
					extentTest.log(LogStatus.FAIL, "isPlaying() API returns " + isPlaying + " while video is playing");
					return false;
				}
				logger.info("isPlaying() API returns " + isPlaying + " while video is playing");
				extentTest.log(LogStatus.PASS, "isPlaying() API returns " + isPlaying + " while video is playing");
			}
		} catch (Exception ex) {
			logger.error(ex);
			extentTest.log(LogStatus.FAIL, ex.getMessage());
			return false;
		}
		return true;
	}

	public boolean validateAPIForPostroll() {
		logger.info("*************************** Validate All API's for Postroll Ad *********************************");

		boolean isVideoPlaying = playerAPI.isPlaying();
		if (!isVideoPlaying) {
			if (!(validateDescriptionAPI(playerAPI.getItemDescription())
					&& validateEmbedCodeAPI(playerAPI.getItemEmbedCode())
					&& executeGetItemAPI(playerAPI.getItemTitle()))) {
				return false;
			}
			playerAPI.play();
		}
		if (!loadingSpinner()) {
			logger.error("Loading spinner is present for long time after playing the video");
			extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after playing the video");
			return false;
		}

		new PlayBackFactory(driver, extentTest).getEventValidator().playVideoForSometime(4);
		int seekTime = playerAPI.getDurationFixed() - 10;
		playerAPI.seek(seekTime);

		if (!loadingSpinner()) {
			return false;
		}

		if (!waitOnElement(By.id("PostRoll_willPlayAds"), 30000)) {
			logger.error("Postroll ad is not playing or present for this asset");
			extentTest.log(LogStatus.FAIL, "Postroll ad is not playing or present for this asset");
			return false;
		}

		if (!new PlayBackFactory(driver, extentTest).getEventValidator().playVideoForSometime(2)) {
			logger.error("failed to play video for some time");
			extentTest.log(LogStatus.FAIL, "failed to play video for some time");
			return false;
		}

		boolean isAdPlaying = playerAPI.isAdPlaying();
		if (!isAdPlaying) {
			logger.error("isAdPlaying() API does not return true when ad is playing");
			extentTest.log(LogStatus.FAIL, "isAdPlaying() API does not return true when ad is playing");
		}
		logger.info("isAdPlaying() API does return true when ad is playing");
		extentTest.log(LogStatus.PASS, "isAdPlaying() API does return true when ad is playing");

		if (isAdPlaying) {
			playerAPI.pause();
			if (!waitOnElement(By.id("videoPausedAds_1"), 10000)) {
				logger.error("Does not found videoPausedAds event after video gets paused");
				extentTest.log(LogStatus.FAIL, "Does not found videoPausedAds event after video gets paused");
				return false;
			}
			logger.info("Found videoPausedAds event after video gets paused");
			extentTest.log(LogStatus.PASS, "Found videoPausedAds event after video gets paused");

			playerAPI.skipAd();
			if (!waitOnElement(By.id("skipAd_1"), 10000)) {
				logger.error("Does not found skipAd event after ad gets skipped");
				extentTest.log(LogStatus.FAIL, "Does not found skipAd event after ad gets skipped");
				return false;
			}
			logger.info("Found skipAd event after ad gets skipped");
			extentTest.log(LogStatus.PASS, "Found skipAd event after ad gets skipped");

			if (!loadingSpinner()) {
				logger.error("Loading spinner is present for long time after skipping the ad");
				extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after skipping the ad");
				return false;
			}

			if (!waitOnElement(By.id("played_1"), 10000)) {
				logger.error("Does not found Played event after ad gets skipped");
				extentTest.log(LogStatus.FAIL, "Does not found Played event after ad gets skipped");
				return false;
			}
			logger.info("Found Played event after ad gets skipped");
			extentTest.log(LogStatus.PASS, "Found Played event after ad gets skipped");
		}
		return true;
	}

	public boolean validateAPIForPrerollPodded() {
		logger.info(
				"*************************** Validate All API's for Preroll Podded Ad *********************************");
		if (!(validateDescriptionAPI(playerAPI.getItemDescription())
				&& validateEmbedCodeAPI(playerAPI.getItemEmbedCode()) && executeGetItemAPI(playerAPI.getItemTitle()))) {
			return false;
		}

		boolean isVideoPlaying = playerAPI.isAdPlaying();
		if (!isVideoPlaying) {
			playerAPI.play();
		}

		if (!loadingSpinner()) {
			logger.error("Loading spinner is present for long time after playing the video");
			extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after playing the video");
			return false;
		}

		if (!waitOnElement(By.id("PreRoll_willPlaySingleAd_1"), 10000)) {
			logger.error("preroll podded ad is not playing or present for this asset");
			extentTest.log(LogStatus.FAIL, "preroll podded ad is not playing or present for this asset");
			return false;
		}

		// TODO need to find a way to get ad count down before playing the
		// ad.... Also skipAd() is not working as expected ....
		for (int i = 1; i < 4; i++) {
			boolean isAdPlaying = playerAPI.isAdPlaying();
			if (!isAdPlaying) {
				logger.error("ad is not playing");
				extentTest.log(LogStatus.FAIL, "ad is not playing");
				return false;
			}
			logger.info("isAdPlaying() API does return true when ad is playing");
			extentTest.log(LogStatus.PASS, "isAdPlaying() API does return true when ad is playing");
			if (isAdPlaying) {
				playerAPI.pause();
				if (!waitOnElement(By.id("videoPausedAds_" + i + ""), 10000)) {
					logger.error("Does not found videoPausedAds_" + i + " event after video gets paused");
					extentTest.log(LogStatus.FAIL,
							"Does not found videoPausedAds_" + i + " event after video gets paused");
					return false;
				}
				logger.info("Found videoPausedAds_" + i + " event after video gets paused");
				extentTest.log(LogStatus.PASS, "Found videoPausedAds_" + i + " event after video gets paused");

				playerAPI.skipAd();
				if (!waitOnElement(By.id("skipAd_" + i + ""), 10000)) {
					logger.error("Does not found skipAd_" + i + " event after ad gets skipped");
					extentTest.log(LogStatus.FAIL, "Does not found skipAd_" + i + " event after ad gets skipped");
					return false;
				}

				logger.info("Found skipAd_" + i + " event after ad gets skipped");
				extentTest.log(LogStatus.PASS, "Found skipAd_" + i + " event after ad gets skipped");

				if (!loadingSpinner()) {
					logger.error("Loading spinner is present for long time after skipping the ad");
					extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after skipping the ad");
					return false;
				}

				boolean isPlaying = playerAPI.isPlaying();
				logger.info("isPlaying after playing ad :" + isPlaying);
				if (isPlaying && i != 3) {
					logger.error("pp.skipAd() API Skips all the podded ads...");
					extentTest.log(LogStatus.FAIL, "pp.skipAd() API Skips all the podded ads...");
					return false;
				}
			}
		}
		if (!waitOnElement(By.id("playing_1"), 10000)) {
			logger.error("Does not found Playing event after ad gets skipped");
			extentTest.log(LogStatus.FAIL, "Does not found Playing event after ad gets skipped");
			return false;
		}
		logger.info("Found Playing event after ad gets skipped");
		extentTest.log(LogStatus.PASS, "Found Playing event after ad gets skipped");

		boolean isPlaying = playerAPI.isPlaying();
		if (!isPlaying) {
			logger.error("isPlaying() API returns " + isPlaying + " while video is playing");
			extentTest.log(LogStatus.FAIL, "isPlaying() API returns " + isPlaying + " while video is playing");
			return false;
		}
		logger.info("isPlaying() API returns " + isPlaying + " while video is playing");
		extentTest.log(LogStatus.PASS, "isPlaying() API returns " + isPlaying + " while video is playing");

		return true;
	}

	public boolean validateAPIForMidrollPodded() {
		logger.info(
				"*************************** Validate All API's for Midroll Podded Ad *********************************");

		boolean isVideoPlaying = playerAPI.isPlaying();
		if (!isVideoPlaying) {
			if (!(validateDescriptionAPI(playerAPI.getItemDescription())
					&& validateEmbedCodeAPI(playerAPI.getItemEmbedCode())
					&& executeGetItemAPI(playerAPI.getItemTitle()))) {
				return false;
			}
			playerAPI.play();
		}
		if (!loadingSpinner()) {
			logger.error("Loading spinner is present for long time after playing the video");
			extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after playing the video");
			return false;
		}

		for (int i = 1; i < 4; i++) {
			if (!waitOnElement(By.id("willPlayMidrollAd_" + i + ""), 35000)) {
				logger.error("Midroll podded ad is not playing or present for this asset ... MidRoll_willPlaySingleAd_"
						+ i + " event not found");
				extentTest.log(LogStatus.FAIL,
						"Midroll podded ad is not playing or present for this asset ... MidRoll_willPlaySingleAd_" + i
								+ " event not found ");
				return false;
			}

			boolean isAdPlaying = playerAPI.isAdPlaying();
			if (!isAdPlaying) {
				logger.error("isAdPlaying() API does not return true when ad is playing");
				extentTest.log(LogStatus.FAIL, "isAdPlaying() API does not return true when ad is playing");
			}
			logger.info("isAdPlaying() API does return true when ad is playing");
			extentTest.log(LogStatus.PASS, "isAdPlaying() API does return true when ad is playing");

			if (isAdPlaying) {
				playerAPI.pause();
				if (!waitOnElement(By.id("videoPausedAds_" + i + ""), 10000)) {
					logger.error("Does not found videoPausedAds_" + i + " event after video gets paused");
					extentTest.log(LogStatus.FAIL,
							"Does not found videoPausedAds_" + i + " event after video gets paused");
					return false;
				}
				logger.info("Found videoPausedAds_" + i + " event after video gets paused");
				extentTest.log(LogStatus.PASS, "Found videoPausedAds_" + i + " event after video gets paused");

				playerAPI.skipAd();
				if (!waitOnElement(By.id("skipMidrollAd_" + i + ""), 10000)) {
					logger.error("Does not found skipMidrollAd_" + i + " event after ad gets skipped");
					extentTest.log(LogStatus.FAIL, "Does not found skipAd_" + i + " event after ad gets skipped");
					return false;
				}

				boolean isPlaying = playerAPI.isPlaying();
				if (isPlaying && i != 3) {
					logger.error("pp.skipAd() API Skips all the podded ads...");
					extentTest.log(LogStatus.FAIL, "pp.skipAd() API Skips all the podded ads...");
					return false;
				}
				logger.info("Found skipMidrollAd_" + i + " event after ad gets skipped");
				extentTest.log(LogStatus.PASS, "Found skipAd_" + i + " event after ad gets skipped");

				if (!loadingSpinner()) {
					logger.error("Loading spinner is present for long time after skipping the ad");
					extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after skipping the ad");
					return false;
				}
			}
		}
		if (!waitOnElement(By.id("playing_2"), 10000)) {
			logger.error("Does not found Playing event after ad gets skipped");
			extentTest.log(LogStatus.FAIL, "Does not found Playing event after ad gets skipped");
			return false;
		}
		logger.info("Found Playing event after ad gets skipped");
		extentTest.log(LogStatus.PASS, "Found Playing event after ad gets skipped");

		boolean isPlaying = playerAPI.isPlaying();
		if (!isPlaying) {
			logger.error("isPlaying() API returns " + isPlaying + " while video is playing");
			extentTest.log(LogStatus.FAIL, "isPlaying() API returns " + isPlaying + " while video is playing");
			return false;
		}
		logger.info("isPlaying() API returns " + isPlaying + " while video is playing");
		extentTest.log(LogStatus.PASS, "isPlaying() API returns " + isPlaying + " while video is playing");
		return true;
	}

	public boolean validateAPIForPostrollPodded() {
		logger.info(
				"*************************** Validate All API's for Postroll Podded Ad *********************************");

		boolean isVideoPlaying = playerAPI.isPlaying();
		if (!isVideoPlaying) {
			if (!(validateDescriptionAPI(playerAPI.getItemDescription())
					&& validateEmbedCodeAPI(playerAPI.getItemEmbedCode())
					&& executeGetItemAPI(playerAPI.getItemTitle()))) {
				return false;
			}
			playerAPI.play();
		}
		if (!loadingSpinner()) {
			logger.error("Loading spinner is present for long time after playing the video");
			extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after playing the video");
			return false;
		}

		new PlayBackFactory(driver, extentTest).getEventValidator().playVideoForSometime(4);
		playerAPI.seek(playerAPI.getDurationFixed() - 10);

		if (!loadingSpinner()) {
			logger.error("Loading spinner is present");
			extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time");
			return false;
		}

		// TODO Need to find out exact ad count before it's playback starts....
		for (int i = 1; i < 4; i++) {
			if (!waitOnElement(By.id("willPlayPostrollAd_" + i + ""), 35000)) {
				logger.error(
						"Postroll podded ad is not playing or present for this asset ... PostRoll_willPlaySingleAd_" + i
								+ " event not found");
				extentTest.log(LogStatus.FAIL,
						"Postroll podded ad is not playing or present for this asset ... PostRoll_willPlaySingleAd_" + i
								+ " event not found");
				return false;
			}

			boolean isAdPlaying = playerAPI.isAdPlaying();
			if (!isAdPlaying) {
				logger.error("isAdPlaying() API does not return true when ad is playing");
				extentTest.log(LogStatus.FAIL, "isAdPlaying() API does not return true when ad is playing");
			}
			logger.info("isAdPlaying() API does return true when ad is playing");
			extentTest.log(LogStatus.PASS, "isAdPlaying() API does return true when ad is playing");

			if (isAdPlaying) {
				playerAPI.pause();
				if (!waitOnElement(By.id("videoPausedAds_" + i + ""), 10000)) {
					logger.error("Does not found videoPausedAds_" + i + " event after video gets paused");
					extentTest.log(LogStatus.FAIL,
							"Does not found videoPausedAds_" + i + " event after video gets paused");
					return false;
				}
				logger.info("Found videoPausedAds_" + i + " event after video gets paused");
				extentTest.log(LogStatus.PASS, "Found videoPausedAds_" + i + " event after video gets paused");

				playerAPI.skipAd();
				if (!waitOnElement(By.id("skipPostrollAd_" + i + ""), 10000)) {
					logger.error("Does not found skipPostrollAd_" + i + " event after ad gets skipped");
					extentTest.log(LogStatus.FAIL, "Does not found skipAd_" + i + " event after ad gets skipped");
					return false;
				}

				if (isElementPresent(By.id("played_1")) && i != 3) {
					logger.error("pp.skipAd() API Skips all the podded ads...");
					extentTest.log(LogStatus.FAIL, "pp.skipAd() API Skips all the podded ads...");
					return false;
				}
				logger.info("Found skipPostrollAd_" + i + " event after ad gets skipped");
				extentTest.log(LogStatus.PASS, "Found skipPostrollAd_" + i + " event after ad gets skipped");

				if (!loadingSpinner()) {
					logger.error("Loading spinner is present for long time after skipping the ad");
					extentTest.log(LogStatus.FAIL, "Loading spinner is present for long time after skipping the ad");
					return false;
				}
			}
		}
		if (!waitOnElement(By.id("played_1"), 30000)) {
			logger.error("Does not found Played event after ad gets skipped");
			extentTest.log(LogStatus.FAIL, "Does not found Played event after ad gets skipped");
			return false;
		}
		logger.info("Found Played event after all podded ad gets skipped");
		extentTest.log(LogStatus.PASS, "Found Played event after all podded ad gets skipped");
		return true;
	}

	public boolean validateAPIForPreMidPost() {
		return validateAPIForPreroll() && validateAPIForMidroll() && validateAPIForPostroll();
	}

	public boolean validateAPIForPreMidPostPodded() {
		return validateAPIForPrerollPodded() && validateAPIForMidrollPodded() && validateAPIForPostrollPodded();
	}

	public Boolean isPlayerState(String PlayerState) {
		int time = 0;
		String playerState = playerAPI.getState();
		logger.info("Player State is : " + playerState);

		try {

			while (playerState.equals(("buffering")) || playerState.equals(("loading"))) {

				if (time <= 120) {
					try {
						Thread.sleep(1000);
						time++;
						logger.info("Video Buffering...");
						playerState = playerAPI.getState();
					} catch (Exception e) {
						return false;
					}
				} else {
					logger.info("Video has been buffering for a really long time i.e it occured more that 2 minutes");
					return false;
				}
			}

			if (playerState.equals("ready") && !PlayerState.equals("ready")) {

				logger.info("Waiting for autoplay...");
				Thread.sleep(3000);
				playerState = playerAPI.getState();

			}

			if (playerState.equals(PlayerState)) {
				extentTest.log(LogStatus.PASS, "Player State is Correct");
				logger.info("Player State is : " + playerState);
				return true;
			} else {
				extentTest.log(LogStatus.FAIL, "Player State :" + playerState);
				logger.info("Player State did not match");
				return false;
			}
		} catch (Exception e) {

			extentTest.log(LogStatus.FAIL, "Exception whle verifying Player State");
			return false;

		}
	}

	public Boolean isPlayerVolume(String Volume) {
		String playingVolume = playerAPI.getVolume();
		logger.info("Player Volume is : " + playingVolume);
		if (playingVolume.equals(Volume)) {
			extentTest.log(LogStatus.PASS, "Volume is Correct");
			logger.info("Volume is Correct");
			return true;
		} else {
			extentTest.log(LogStatus.FAIL, "Volume did not match");
			logger.info("Volume did not match");
			return false;
		}
	}

}
