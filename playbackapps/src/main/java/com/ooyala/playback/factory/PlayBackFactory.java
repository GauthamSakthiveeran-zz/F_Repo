package com.ooyala.playback.factory;

import java.lang.reflect.Field;

import org.openqa.selenium.WebElement;

import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.QAModeSwitchAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

import io.appium.java_client.AppiumDriver;

public class PlayBackFactory {

	private AppiumDriver driver;
	private PauseAction pauseAction;
	private SelectVideoAction selectVideoAction;
	private NotificationEventValidator NotificationEventValidator;
	private QAModeSwitchAction qaModeSwitchAction;

	public PlayBackFactory(AppiumDriver driver) {
		this.driver = driver;
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
		if (selectVideoAction == null) {
			selectVideoAction = new SelectVideoAction(driver);
		}
		return selectVideoAction;
	}

	public NotificationEventValidator getNotificationEventValidator() {
		if (NotificationEventValidator == null) {
			NotificationEventValidator = new NotificationEventValidator(driver);
		}
		return NotificationEventValidator;
	}
	
	public AppiumDriver getDriver() {
		return driver;
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
