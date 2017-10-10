package com.ooyala.playback.apps.android.ooyalaskinsamplespp;

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
import com.ooyala.playback.apps.actions.AndroidKeyCodeAction;
import com.ooyala.playback.apps.actions.ClickDiscoveryButtonAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.ShareAction;
import com.ooyala.playback.apps.actions.SwipeUpDownAppAssetsAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.ios.PlaybackAppsBasicTest;
import com.ooyala.playback.apps.validators.DiscoveryValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;
import com.ooyala.playback.apps.validators.ShareValidator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

 public class OoyalaSkinSampleSkinPlayBackShareTest extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(OoyalaSkinSampleSkinPlayBackShareTest.class);
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
    private ShareValidator sharevalidator;
    private ShareAction shareAction;


    @Test(groups = "OoyalaSkinSampleApp", dataProvider = "testData")
    public void testSocialMediaShare(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
        	
        	result = result && appAssetsSelection.startAction("Skin Playback");
        	
        	Thread.sleep(3000);
        	
        	result = result && selectVideo.startAction(test.getAsset());
        	
        	Thread.sleep(3000);
        	
        	result = result && appAssetsSelection.handleAccessMedia();
        	
        	result = result && androidKeyCode.startAction("BACK");
        	
        	result = result && selectVideo.startAction(test.getAsset());
        	
        	result = result && shareAction.clickPlayButton();
        	
        	result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
        	
    		result = result && shareAction.startAction("SHAREBUTTON_ANDROID");
    		
			result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_PAUSED, 70000);
    		
    		result = result && sharevalidator.validate("", 2000);
    		
    		result = result && shareAction.closeMoreOptionsScreen();
    		
    		Thread.sleep(2000);
    		
    		clickDiscoveryAction.tapinMiddleOfPlayerScreen();
    		
    		result = result && clickDiscoveryAction.seekToEnd("SEEKBAR_ANDROID");
    		
    		result = result && shareAction.clickPlayButton();
    		
    		result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_COMPLETED, 70000); 
        	

        }
        catch(Exception ex) {
        	ex.printStackTrace();
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }

}
