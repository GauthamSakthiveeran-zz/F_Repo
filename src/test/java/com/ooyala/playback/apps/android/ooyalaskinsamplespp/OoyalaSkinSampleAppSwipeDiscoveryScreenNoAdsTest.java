package com.ooyala.playback.apps.android.ooyalaskinsamplespp;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.AllowAction;
import com.ooyala.playback.apps.actions.AndroidKeyCodeAction;
import com.ooyala.playback.apps.actions.DiscoveryAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.SwipeUpDownAppAssetsAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.validators.DiscoveryValidator;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

 public class OoyalaSkinSampleAppSwipeDiscoveryScreenNoAdsTest extends PlaybackAppsTest {

    private static Logger logger = Logger.getLogger(OoyalaSkinSampleAppSwipeDiscoveryScreenNoAdsTest.class);
    private SelectVideoAction selectVideo;
    private ElementValidator elementValidator;
    private PauseAction pauseAction;
    private PlayAction playAction;
    private SeekAction seekAction;
    private DiscoveryAction clickDiscoveryAction;
    private SwipeUpDownAppAssetsAction appAssetsSelection;
    private DiscoveryValidator discoveryValidator;
    private NotificationEventValidator notificationEventValidator;
    private AndroidKeyCodeAction androidKeyCode;
    private AllowAction allowAction;
    


    @Test(groups = "OoyalaSkinSampleApp", dataProvider = "testData")
    public void testPluginPlayer(String testName, TestParameters test) throws Exception {
        Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
        boolean result = true;
        try {
        	
        	result = result && appAssetsSelection.startAction("Skin Playback");
        	
        	Thread.sleep(3000);
        	
        	result = result && selectVideo.startAction(test.getAsset());
        	
        	Thread.sleep(3000);
        	
        	result = result && allowAction.startAction("ALLOW");
        	
        	result = result && androidKeyCode.startAction("BACK");
        	
        	result = result && selectVideo.startAction(test.getAsset());
        	
        	result = result && clickDiscoveryAction.clickPlayButton();
        	
        	result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
        	
    		result = result && clickDiscoveryAction.startAction("DISCOVERYBUTTON_ANDROID");
    		
    		result = result && discoveryValidator.swipeAndselectVideoFromDiscoveryScreen();
    		
    		result = result && notificationEventValidator.validateEvent(Events.PLAYBACK_STARTED, 20000);
        	

        }
        catch(Exception ex) {
        	ex.printStackTrace();
            logger.error("Here is an exception"+ex);
            result = false;
        }
        Assert.assertTrue(result, "APP:"+test.getApp()+"->Asset:"+test.getAsset());

    }

}
