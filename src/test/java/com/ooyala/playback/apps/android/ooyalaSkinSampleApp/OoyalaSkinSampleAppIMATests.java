package com.ooyala.playback.apps.android.ooyalaSkinSampleApp;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.AllowAction;
import com.ooyala.playback.apps.actions.AndroidKeyCodeAction;
import com.ooyala.playback.apps.actions.ClickDiscoveryButtonAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.SwipeUpDownAppAssetsAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.ios.PlaybackAppsBasicTest;
import com.ooyala.playback.apps.validators.DiscoveryValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

 public class OoyalaSkinSampleAppIMATests extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(OoyalaSkinSampleAppIMATests.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private PauseAction pauseAction;
    private PlayAction playAction;
    private SeekAction seekAction;
    private ClickDiscoveryButtonAction clickDiscoveryAction;
    private SwipeUpDownAppAssetsAction appAssetsSelection;
    private DiscoveryValidator discoveryValidator;
    private NotificationEventValidator notificationEventValidator;
    private AndroidKeyCodeAction androidKeyCode;
    private AllowAction  allowAction;
    


    @Test(groups = "OoyalaSkinSampleApp", dataProvider = "testData")
    public void testAdRules(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
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
	        	if(test.getAsset().contains("PREROLL")) {
	        		result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
	        		result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
	        	}
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            result = result && clickDiscoveryAction.clickPauseButton();
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED, 70000);
            result = result && clickDiscoveryAction.seekForward("SEEKBAR_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);       
            result = result && clickDiscoveryAction.clickPlayButton();
            if(test.getAsset().contains("MIDROLL") || test.getAsset().contains("POSTROLL")) {
        			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
        			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
            }
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  70000);
        }
        catch(Exception ex) {
        		ex.printStackTrace();
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }
    
    @Test(groups = "OoyalaSkinSampleApp", dataProvider = "testData")
    public void testPoddedAds(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
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
	        	if(test.getAsset().contains("PREROLL") || test.getAsset().contains("PREMIDPOST")) {
	        		result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
	        		result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
	        		result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
	        		result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
	        	}
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            result = result && clickDiscoveryAction.clickPauseButton();
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED, 70000);
            result = result && clickDiscoveryAction.seekForward("SEEKBAR_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);       
            result = result && clickDiscoveryAction.clickPlayButton();
            if(test.getAsset().contains("PREMIDPOST")) {
    				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
    				result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
    				result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
	        		result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
            }
            if(test.getAsset().contains("MIDROLL") || test.getAsset().contains("POSTROLL") || test.getAsset().contains("PREMIDPOST")) {
        			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
        			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
        			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
        			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
            }
            
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  70000);
        }
        catch(Exception ex) {
        		ex.printStackTrace();
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }

    @Test(groups = "OoyalaSkinSampleApp", dataProvider = "testData")
    public void testSkippableAds(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
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
	        	result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
	        result = result && elementValidator.validate("SKIPAD_ANDROID", 10000);
	        result = result && elementValidator.clickOnElement("SKIPAD_ANDROID");
	        	result = result && notificationEventValidator.validateEvent(Events.AD_SKIPPPED,20000);	        
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            result = result && clickDiscoveryAction.clickPauseButton();
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED, 70000);
            result = result && clickDiscoveryAction.seekForward("SEEKBAR_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);       
            result = result && clickDiscoveryAction.clickPlayButton();
            if(test.getAsset().contains("PREMIDPOST")) {
	            	result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
	    	        result = result && elementValidator.validate("SKIPAD_ANDROID", 20000);
	    	        result = result && elementValidator.clickOnElement("SKIPAD_ANDROID");
	    	        	result = result && notificationEventValidator.validateEvent(Events.AD_SKIPPPED,20000);
            }            
           	result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
	        result = result && elementValidator.validate("SKIPAD_ANDROID", 10000);
	        result = result && elementValidator.clickOnElement("SKIPAD_ANDROID");
	        	result = result && notificationEventValidator.validateEvent(Events.AD_SKIPPPED,20000);  
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  70000);
        }
        catch(Exception ex) {
        		ex.printStackTrace();
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }
    
    @Test(groups = "OoyalaSkinSampleApp", dataProvider = "testData")
    public void testNonAdRules(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
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
	        	if(test.getAsset().contains("PREROLL")) {
	        		result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,20000);
	        		result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,20000);
	        	}
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
            result = result && clickDiscoveryAction.clickPauseButton();
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED, 70000);
            result = result && clickDiscoveryAction.seekForward("SEEKBAR_ANDROID");
            result = result && notificationEventValidator.validateEvent(Events.SEEK_STARTED, 20000);
            result = result && notificationEventValidator.validateEvent(Events.SEEK_COMPLETED, 20000);       
            result = result && clickDiscoveryAction.clickPlayButton();
            if(test.getAsset().contains("QUADMID")) {
	    			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
	    			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
	    			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
	    			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
	    			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
	    			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
	    			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
	    			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
            }
            if(test.getAsset().contains("MIDROLL") || test.getAsset().contains("PREMIDMIDPOST")) {
	    			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
	    			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
            }
            if(test.getAsset().contains("PREMIDMIDPOST")) {
    			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
    			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
            }
            if(test.getAsset().contains("PREMIDMIDPOST") || test.getAsset().contains("POSTROLL")) {
        			result = result && notificationEventValidator.validateEvent(Events.AD_STARTED,70000);
        			result = result && notificationEventValidator.validateEvent(Events.AD_COMPLETED,70000);
            }
            result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED,  70000);
        }
        catch(Exception ex) {
        		ex.printStackTrace();
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }
}
