package com.ooyala.playback.factory;

import java.lang.reflect.Field;

import com.ooyala.playback.apps.actions.*;
import org.openqa.selenium.WebElement;

import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

import io.appium.java_client.AppiumDriver;

public class PlayBackFactory {

	private AppiumDriver driver;
	private PauseAction pauseAction;
	private SelectVideoAction selectVideo;
	private NotificationEventValidator notificationEventValidator;
	private QAModeSwitchAction qaModeSwitchAction;
	private SeekAction seekAction;
	private ElementValidator elementValidator;
	private LaunchAction launchAction;



	public PlayBackFactory(AppiumDriver driver) {
		this.driver = driver;
	}

	public LaunchAction getLaunchAction() {
		if (launchAction == null) {
			launchAction = new LaunchAction(driver);
		}
		return launchAction;
	}

	public QAModeSwitchAction getQAModeSwitchAction() {
		if (qaModeSwitchAction == null) {
			qaModeSwitchAction = new QAModeSwitchAction(driver);
		}
		return qaModeSwitchAction;
	}

	public PauseAction getPauseAction() {
		if (pauseAction == null) {
			pauseAction = new PauseAction(driver);
		}
		return pauseAction;
	}

	public SelectVideoAction getSelectVideoAction() {
		if (selectVideo == null) {
			selectVideo = new SelectVideoAction(driver);
		}
		return selectVideo;
	}

	public SeekAction getSeekAction() {
		if(null == seekAction) {
			seekAction = new SeekAction(driver);
		}
		return seekAction;
	}


	public NotificationEventValidator getNotificationEventValidator() {
		if (notificationEventValidator == null) {
			notificationEventValidator = new NotificationEventValidator(driver);
		}
		return notificationEventValidator;
	}
	
	public ElementValidator getEventValidator() {
		if (elementValidator == null) {
			elementValidator = new ElementValidator(driver);
		}
		return elementValidator;
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
