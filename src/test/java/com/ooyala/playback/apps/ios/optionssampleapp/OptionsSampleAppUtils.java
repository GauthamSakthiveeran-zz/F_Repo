package com.ooyala.playback.apps.ios.optionssampleapp;

import org.apache.log4j.Logger;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.ClickAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

public class OptionsSampleAppUtils extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(OptionsSampleAppUtils.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private ClickAction clickAction;
	private PlayAction playAction;

	public boolean performAssetSpecificTest(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickAction = pageFactory.getClickAction();
		playAction = pageFactory.getPlayAction();
		
		boolean isSmall=test.getDescription().contains("small")?true:false;

		try {
			result = result && selectVideo.startAction(test.getAsset());
			if (test.getAsset().contains("TV_RATING_CONFIGURATION")){
				result = result && clickAction.waitOnElement("SET_EC", 5000);
				result = result && clickAction.startAction("SET_EC");
			}else if (test.getAsset().contains("PRELOAD") || test.getAsset().contains("TIMEOUT")){
				result = result && clickAction.waitOnElement("BUTTON", 5000);
				result = result && clickAction.startAction("BUTTON");
			}
			
			result = result && elementValidator.handleLoadingSpinner();
			
			if(!test.getAsset().contains("IQ_CONFIGURATION_SAMPLE"))
				result = result && playAction.startAction("PLAY_PAUSE_BUTTON");
			
//			if(!isSmall)
//				result = result && selectVideo.letVideoPlayForSec(2);

			if (test.getAsset().contains("PREROLL")) {
				result = adEventValidator();
			}

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

			if (test.getAsset().contains("MIDROLL")) {
				result = adEventValidator();
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);			
			}
			
			if (!isSmall) {
				result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
				result = result && seekActionEventValidator(true);
				result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);
			}

			if (test.getAsset().contains("POSTROLL")) {
				result = adEventValidator();
						
			}

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 90000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	private boolean adEventValidator() {
		boolean result = true;
		result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
		result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		return result;
	}

	private boolean seekActionEventValidator(boolean isSeekForward) {
		boolean result = true;
		try {
			if (isSeekForward)
				result = result && seekAction.setSlider("SLIDER").seekforward().startAction("SEEK_BAR"); // SeekForward
			else
				result = result && seekAction.setSlider("SLIDER").startAction("SEEK_BAR"); // SeekBack
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
		} catch (Exception e) {
			logger.error("Here is an exception" + e);
			result = false;
		}
		return result;

	}

}
