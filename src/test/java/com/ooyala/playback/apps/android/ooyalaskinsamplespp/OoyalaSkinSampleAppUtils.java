package com.ooyala.playback.apps.android.ooyalaskinsamplespp;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.AllowAction;
import com.ooyala.playback.apps.actions.AndroidKeyCodeAction;
import com.ooyala.playback.apps.actions.CCAction;
import com.ooyala.playback.apps.actions.ClickAction;
import com.ooyala.playback.apps.actions.DiscoveryAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.SwipeUpDownAppAssetsAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.validators.AdValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.ooyala.playback.apps.validators.PoddedAdValidator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;

public class OoyalaSkinSampleAppUtils extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(OoyalaSkinSampleAppUtils.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private DiscoveryAction clickDiscoveryAction;
	private PlayAction playAction;
	private AndroidKeyCodeAction androidKeyCode;
    private AllowAction  allowAction;
    private SwipeUpDownAppAssetsAction appAssetsSelection;
	//pageFactory = new pageFactory((AppiumDriver) driver, extentTest);
    private AdValidator adValidator;
    private PoddedAdValidator poddedAdValidator;

	public boolean freeWheelTests(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
        adValidator = pageFactory.getAdEventValidator();
		try {
			result = result && appAssetsSelection.startAction("Freewheel Integration");
	    		Thread.sleep(3000);
	    		result = result && selectVideo.startAction(test.getAsset());
	    		Thread.sleep(3000);
	    		result = result && allowAction.startAction("ALLOW");       	
	        	result = result && androidKeyCode.startAction("BACK");        	
	        	result = result && selectVideo.startAction(test.getAsset());
	        	Thread.sleep(5000);
	        	result = result && clickDiscoveryAction.clickPlayButton();
	        	result = result && androidKeyCode.screenLockUnlock();
		    result = result && androidKeyCode.openAppFromAppSwitchScreen();
	        result = result && adValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).validate("", 2000);
	        result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}
	
	public boolean  testIMAAdRules(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		adValidator = pageFactory.getAdEventValidator();
		try {
			result = result && appAssetsSelection.startAction("Google IMA Integration");
	    		Thread.sleep(3000);
	    		result = result && selectVideo.startAction(test.getAsset());
	    		Thread.sleep(3000);
	    		result = result && allowAction.startAction("ALLOW");       	
	        	result = result && androidKeyCode.startAction("BACK");        	
	        	result = result && selectVideo.startAction(test.getAsset());
	        	Thread.sleep(5000);
	        	result = result && clickDiscoveryAction.clickPlayButton();
	        	result = result && androidKeyCode.screenLockUnlock();
	        	result = result && androidKeyCode.openAppFromAppSwitchScreen();
	        	result = result && adValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).validate("", 2000);
	        result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean  testIMAPoddedAds(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		poddedAdValidator = pageFactory.getPoddedAdValidator();
		try {
			result = result && appAssetsSelection.startAction("Google IMA Integration");
	    		Thread.sleep(3000);
	    		result = result && selectVideo.startAction(test.getAsset());
	    		Thread.sleep(3000);
	    		result = result && allowAction.startAction("ALLOW");       	
	        	result = result && androidKeyCode.startAction("BACK");        	
	        	result = result && selectVideo.startAction(test.getAsset());
	        	Thread.sleep(5000);
	        	result = result && clickDiscoveryAction.clickPlayButton();
	        	result = result && androidKeyCode.screenLockUnlock();
	        	result = result && poddedAdValidator.isOoyalaSkinSampleAsset(true).setNoOfAds("2").setTestParameters(test).validate("", 2000);   
	        result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	public boolean  testIMASkippableAds(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		adValidator = pageFactory.getAdEventValidator();
		try {
			result = result && appAssetsSelection.startAction("Google IMA Integration");
	    		Thread.sleep(3000);
	    		result = result && selectVideo.startAction(test.getAsset());
	    		Thread.sleep(3000);
	    		result = result && allowAction.startAction("ALLOW");       	
	        	result = result && androidKeyCode.startAction("BACK");        	
	        	result = result && selectVideo.startAction(test.getAsset());
	        	Thread.sleep(5000);
	        	result = result && clickDiscoveryAction.clickPlayButton();
			result = result && androidKeyCode.openAppFromAppSwitchScreen();
	        	result = result &&  adValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).validate("", 2000);
	        result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}
	
	public boolean  testIMANonAdRules(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickDiscoveryAction = pageFactory.getClickDiscoveryButtonAction();
		playAction = pageFactory.getPlayAction();
		androidKeyCode = pageFactory.getAndroidKeyCodeAction();
		allowAction = pageFactory.getAllow();
		appAssetsSelection = pageFactory.getSwipeUpDownAppAssetsAction();
		adValidator = pageFactory.getAdEventValidator();
		try {
			result = result && appAssetsSelection.startAction("Google IMA Integration");
	    		Thread.sleep(3000);
	    		result = result && selectVideo.startAction(test.getAsset());
	    		Thread.sleep(3000);
	    		result = result && allowAction.startAction("ALLOW");       	
	        	result = result && androidKeyCode.startAction("BACK");        	
	        	result = result && selectVideo.startAction(test.getAsset());
	        	Thread.sleep(5000);
	        	result = result && clickDiscoveryAction.clickPlayButton();
	        	result = result && androidKeyCode.screenLockUnlock();
	     	result = result && adValidator.isOoyalaSkinSampleAsset(true).setTestParameters(test).validate("", 2000);
	        result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  70000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}


}