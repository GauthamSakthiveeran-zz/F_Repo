package com.ooyala.playback.factory;

import java.lang.reflect.Field;

import com.ooyala.playback.apps.actions.AllowAction;
import com.ooyala.playback.apps.actions.CCAction;
import com.ooyala.playback.apps.actions.ClickAction;
import com.ooyala.playback.apps.actions.LaunchAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.QAModeSwitchAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.android.SeekAction;
import com.ooyala.playback.apps.validators.AdEventValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.FileEventValidator;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.ooyala.playback.apps.validators.OverlayValidator;
import com.ooyala.playback.apps.validators.SeekValidator;
import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.AppiumDriver;

public class PlayBackFactory {

	private AppiumDriver driver;
	private PlayAction playAction;
	private PauseAction pauseAction;
	private SelectVideoAction selectVideo;
	private NotificationEventValidator notificationEventValidator;
	private QAModeSwitchAction qaModeSwitchAction;
	private SeekAction seekAction;
	private ElementValidator elementValidator;
	private LaunchAction launchAction;
	private CCAction ccAction;
	private AllowAction allowAction;
	private AdEventValidator adEventValidator;
	private SeekValidator seekValidator;
	private ClickAction clickAction;
	
	private ExtentTest extentTest;

	private OverlayValidator overlayValidator;
	private FileEventValidator fileEventValidator;

	public PlayBackFactory(AppiumDriver driver, ExtentTest extentTest) {
		this.driver = driver;
		this.extentTest = extentTest;
	}
	
	public ClickAction getClickAction() {
		if(clickAction == null) {
			clickAction = new ClickAction(driver);
			clickAction.setExtentTest(extentTest);
		}
		return clickAction;
	}
	
	public SeekValidator getSeekValidator() {
		if(seekValidator == null) {
			seekValidator = new SeekValidator(driver);
			seekValidator.setExtentTest(extentTest);
		}
		return seekValidator;
	}
	
	public AdEventValidator getAdEventValidator() {
		if(adEventValidator == null) {
			adEventValidator = new AdEventValidator(driver);
			adEventValidator.setExtentTest(extentTest);
		}
		return adEventValidator;
	}

	public LaunchAction getLaunchAction() {
		if (launchAction == null) {
			launchAction = new LaunchAction(driver);
			launchAction.setExtentTest(extentTest);
		}
		return launchAction;
	}

	public QAModeSwitchAction getQAModeSwitchAction() {
		if (qaModeSwitchAction == null) {
			qaModeSwitchAction = new QAModeSwitchAction(driver);
			qaModeSwitchAction.setExtentTest(extentTest);
		}
		return qaModeSwitchAction;
	}

	public PlayAction getPlayAction() {
		if (playAction == null) {
			playAction = new PlayAction(driver);
			playAction.setExtentTest(extentTest);
		}
		return playAction;
	}

    public AllowAction getAllow() {
        if (allowAction == null) {
            allowAction = new AllowAction(driver);
            allowAction.setExtentTest(extentTest);
        }
        return allowAction;
    }

	public PauseAction getPauseAction() {
		if (pauseAction == null) {
			pauseAction = new PauseAction(driver);
			pauseAction.setExtentTest(extentTest);
		}
		return pauseAction;
	}

	public SelectVideoAction getSelectVideoAction() {
		if (selectVideo == null) {
			selectVideo = new SelectVideoAction(driver);
			selectVideo.setExtentTest(extentTest);
		}
		return selectVideo;
	}

	public SeekAction getSeekAction() {
		if(null == seekAction) {
			seekAction = new SeekAction(driver);
			seekAction.setExtentTest(extentTest);
		}
		return seekAction;
	}


	public NotificationEventValidator getNotificationEventValidator() {
		if (notificationEventValidator == null) {
			notificationEventValidator = new NotificationEventValidator(driver);
			notificationEventValidator.setExtentTest(extentTest);
		}
		return notificationEventValidator;
	}
	
	public ElementValidator getEventValidator() {
		if (elementValidator == null) {
			elementValidator = new ElementValidator(driver);
			elementValidator.setExtentTest(extentTest);
		}
		return elementValidator;
	}
	public CCAction getCcAction() {
		if (ccAction == null) {
			ccAction = new CCAction(driver);
			ccAction.setExtentTest(extentTest);
		}
		return ccAction;
	}
	public OverlayValidator getOverlayValidator() {
		
		if(overlayValidator == null) {
			overlayValidator = new OverlayValidator(driver);
		}
		return overlayValidator;
	}
	
	public FileEventValidator getFileEventValidator() {
		
		if(fileEventValidator == null) {
			fileEventValidator = new FileEventValidator(driver);
		}
		return fileEventValidator;
	}
	

	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> validator) throws Exception {

		Field[] fs = this.getClass().getDeclaredFields();
		fs[0].setAccessible(true);
		for (Field property : fs) {
			if (property.getType().isAssignableFrom(validator)) {
				if (property.get(this) == null)
					property.set(this, validator.getConstructor(AppiumDriver.class).newInstance(driver));
				return (T) property.get(this);
			}

		}
		return null;
	}

}
