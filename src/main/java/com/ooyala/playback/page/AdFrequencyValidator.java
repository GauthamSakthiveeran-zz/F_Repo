package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.relevantcodes.extentreports.LogStatus;

public class AdFrequencyValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(AdFrequencyValidator.class);

	public AdFrequencyValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
	}

	int firstAdPlay = 0;
	int adFrequency = 0;

	public AdFrequencyValidator split(UrlObject url) {

		if (url != null && url.getAdFirstPlay() != null && url.getAdFrequency() != null
				&& !url.getAdFirstPlay().isEmpty() && !url.getAdFrequency().isEmpty()) {
			firstAdPlay = Integer.parseInt(url.getAdFirstPlay());
			adFrequency = Integer.parseInt(url.getAdFrequency());
		} else {
			logger.error("Insufficient data");
			extentTest.log(LogStatus.FAIL, "Insufficient data");
			assert false;
		}
		return this;
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {

		if (firstAdPlay == 0 || adFrequency == 0) {
			return false;
		}
		
		PlayBackFactory factory = new PlayBackFactory(driver, extentTest);
		
		PlayAction play = factory.getPlayAction();
		EventValidator event = factory.getEventValidator();
		SeekAction seek = factory.getSeekAction();
		ReplayValidator replay = factory.getReplayValidator();

		int i = 1;
		play.startAction();
		while (i < firstAdPlay) {
			event.validate("playing_1", 10000);
			logger.info("Play #" + i);
			extentTest.log(LogStatus.INFO, "Play #" + i);
			if (adPlaying(true)) {
				logger.error("Ad should not be playing right now.");
				extentTest.log(LogStatus.FAIL, "Ad should not be playing right now.");
				return false;
			}
			if (!seek.seekTillEnd().startAction()) {
				logger.error(i + " Seek failed.");
				extentTest.log(LogStatus.FAIL, i + " Seek failed.");
				return false;
			}

			if (!replay.validate("replay_" + i, 5000)) {
				logger.error(i + " Replay failed.");
				extentTest.log(LogStatus.FAIL, i + " Replay failed.");
				return false;
			}
			i++;
		}

		if (!adPlaying(true)) {
			logger.error("Ad should be playing right now.");
			extentTest.log(LogStatus.FAIL, "Ad should be playing right now.");
			return false;
		}

		adPlaying(false);
		logger.info("Validated if ad played.");
		extentTest.log(LogStatus.PASS, "Validated if ad played.");
		
		if (!seek.seekTillEnd().startAction()) {
			logger.error(i + " Seek failed.");
			extentTest.log(LogStatus.FAIL, i + " Seek failed.");
			return false;
		}
		if (!replay.validate("replay_" + i, 5000)) {
			logger.error(i + " Replay failed.");
			extentTest.log(LogStatus.FAIL, i + " Replay failed.");
			return false;
		}

		logger.info("Ad played for the first time");
		extentTest.log(LogStatus.PASS, "Ad played for the first time");

		int j = 1;
		while (j < adFrequency * 2) {
			logger.info("Play #" + i);
			extentTest.log(LogStatus.INFO, "Play #" + i);
			if (j % adFrequency == 0) {
				if (!adPlaying(true)) {
					logger.error("Ad should be playing right now.");
					extentTest.log(LogStatus.FAIL, "Ad should be playing right now.");
					return false;
				}
				adPlaying(false);
			} else{
				if (adPlaying(true)) {
					logger.error("Ad should not be playing right now.");
					extentTest.log(LogStatus.FAIL, "Ad should not be playing right now.");
					return false;
				}
			}
			if (!seek.seekTillEnd().startAction()) {
				logger.error(i + " Seek failed.");
				extentTest.log(LogStatus.FAIL, i + " Seek failed.");
				return false;
			}
			if (!replay.validate("replay_" + i, 5000)) {
				logger.error(i + " Replay failed.");
				extentTest.log(LogStatus.FAIL, i + " Replay failed.");
				return false;
			}
			j++;
			i++;
		}

		return true;
	}

	public boolean validateAdCapFrequency(String testDescription,String firstShown,String adFrequency,int timeout){
		boolean result=false;
		if (testDescription.contains("VAST"))
			return validateForContentTreeFetchedEvent(firstShown,adFrequency,timeout);
		if (testDescription.contains("IMA"))
			return validateForMetadataFetchedEvent(firstShown,adFrequency,timeout);
		logger.error("Please check the ad Manager");
		return result;
	}

	private boolean validateForMetadataFetchedEvent(String firstShown,String adFrequency,int timeout){
		boolean result=false;
		try {
			logger.info("Inside validateMetadataFetchedEvent method");
			Thread.sleep(timeout);
			String consoleOutput = driver.executeScript("return OO.DEBUG.consoleOutput[0].toString()").toString();
			logger.info(consoleOutput);
			if (consoleOutput.contains("metadataFetched")) {
				logger.info("metadataFetched event found in consoleOutput");
				JSONObject jsonObject1= new JSONObject(consoleOutput.split("Message Bus Event: metadataFetched ")[1]);
				int firstShownValue = jsonObject1.getJSONObject("1").getJSONObject("modules").getJSONObject("google-ima-ads-manager").getJSONObject("metadata").getJSONArray("all_ads").getJSONObject(0).getInt("first_shown");
				int frequencyValue = jsonObject1.getJSONObject("1").getJSONObject("modules").getJSONObject("google-ima-ads-manager").getJSONObject("metadata").getJSONArray("all_ads").getJSONObject(0).getInt("frequency");
				logger.info("Retrieved the firstShown : "+firstShown+" and frequency : "+adFrequency+" values from metadataFetched event" );
				if (Integer.parseInt(firstShown)==firstShownValue && Integer.parseInt(adFrequency)==frequencyValue)
					result = true;
			}else {
				logger.error("metadataFetched event not found in consoleOutput");
			}
		} catch (InterruptedException e) {
			logger.trace(e);
			Thread.currentThread().interrupt();
		}
		return result;
	}

	private boolean validateForContentTreeFetchedEvent(String firstShown,String adFrequency,int timeout){
		boolean result=false;
		try {
			logger.info("Inside validateContentTreeFetchedEvent method");
			Thread.sleep(timeout);
			String consoleOutput = driver.executeScript("return OO.DEBUG.consoleOutput[0].toString()").toString();
			logger.info(consoleOutput);
			if (consoleOutput.contains("contentTreeFetched")) {
				logger.info("contentTreeFetched event found in consoleOutput");
				result = true;
				JSONObject jsonObject1= new JSONObject(consoleOutput.split("Message Bus Event: contentTreeFetched ")[1]);
				int firstShownValue = jsonObject1.getJSONObject("1").getJSONArray("ads").getJSONObject(0).getInt("first_shown");
				int frequencyValue = jsonObject1.getJSONObject("1").getJSONArray("ads").getJSONObject(0).getInt("frequency");
				logger.info("Retrieved the firstShown : "+firstShown+" and frequency : "+adFrequency+" values from contentTreeFetched event" );
				if (Integer.parseInt(firstShown)==firstShownValue && Integer.parseInt(adFrequency)==frequencyValue)
					result = true;
			}else {
				logger.error("contentTreeFetched event not found in consoleOutput");
			}
		} catch (InterruptedException e) {
			logger.trace(e);
			Thread.currentThread().interrupt();
		}
		return result;
	}

}
