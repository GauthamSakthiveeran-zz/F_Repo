package com.ooyala.playback.apps.android.optionssampleapp;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.AllowAction;
import com.ooyala.playback.apps.actions.AndroidKeyCodeAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.FileEventValidator;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

 public class OptionsSampleAppTests extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(OptionsSampleAppTests.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private NotificationEventValidator notificationEventValidator;
    private PauseAction pauseAction;
    private PlayAction playAction;
    private SeekAction seekAction;
    private AllowAction allowAction;
    private AndroidKeyCodeAction androidAction;


    @Test(groups = "optionssampleapp", dataProvider = "testData")
    public void testCuePointsAndAdControls(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {

            result = result && selectVideo.startAction(test.getAsset());
            result = result && allowAction.startAction("ALLOW");
            result = result && androidAction.startAction("BACK");
            result = result && selectVideo.startAction(test.getAsset());
            if(test.getDescription().contains("PRELOAD:false") && test.getDescription().contains("PROMOIMAGE:true")) {
            	 	playAction.toggleButton("PRELOAD", "false");
            	 	playAction.toggleButton("SHOWPROMOIMAGE", "true");
            } else if (test.getDescription().contains("PRELOAD:true") && test.getDescription().contains("PROMOIMAGE:true")) {
            		playAction.toggleButton("PRELOAD", "true");
            		playAction.toggleButton("SHOWPROMOIMAGE", "true");
            }
            result = result && playAction.createVideo("CREATE_VIDEO",20000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
            if(test.getDescription().contains("AdControls=ON")) {
            	    result = result && playAction.playPauseAd("AD_PAUSE");
            	    result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 10000);
            	    result = result && playAction.playPauseAd("AD_PLAY");
            }      
            result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
            result = result && seekAction.startAction("SEEK_BAR_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000); 
            result = result && androidAction.screenLockUnlock();
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_SUSPENDED, 20000);
            result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.AD_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
            result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,  40000);
            result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED, 40000);
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  80000);
        }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }
    

    @Test(groups = "optionssampleapp", dataProvider = "testData")
    public void preloadWithInitialTime(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
	        	result = result && selectVideo.startAction(test.getAsset());
	        	result = result && allowAction.startAction("ALLOW");
	        	result = result && androidAction.startAction("BACK");
	        	result = result && selectVideo.startAction(test.getAsset());
	        	result = result && playAction.createVideo("CREATE_VIDEO",20000);
	        	result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
	        	result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
	        result = result && androidAction.screenLockUnlock();
	        result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_SUSPENDED, 20000);
	        result = result && androidAction.openAppFromAppSwitchScreen();	        
	        result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_SUSPENDED, 20000);
	        	result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  80000);
	    }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }
    
    @Test(groups = "optionssampleapp", dataProvider = "testData")
    public void serverSideTvRatings(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
	        	result = result && selectVideo.startAction(test.getAsset());
	        	result = result && allowAction.startAction("ALLOW");
	        	result = result && androidAction.startAction("BACK");
	        	result = result && selectVideo.startAction(test.getAsset());
			result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			result = result && seekAction.startAction("SEEK_BAR_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && androidAction.screenLockUnlock();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_SUSPENDED, 20000);
			result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  80000);
        }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }
   
    
    @Test(groups = "optionssampleapp", dataProvider = "testData")
    public void  tvratingsConfig(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
	        	result = result && selectVideo.startAction(test.getAsset());
	        	result = result && allowAction.startAction("ALLOW");
	        	result = result && androidAction.startAction("BACK");
	        	result = result && selectVideo.startAction(test.getAsset());
	        	result = result && playAction.createVideo("CREATE_VIDEO",20000);
			result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
			result = result && pauseAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED_ANDRD, 70000);
			
			result = result && seekAction.startAction("SEEK_BAR_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
			result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);
			result = result && androidAction.openAppFromAppSwitchScreen();
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_SUSPENDED, 20000);
			result = result && playAction.startAction("PLAY_PAUSE_ANDROID");
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 80000);
        }
        catch(Exception ex) {
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }
    


}
