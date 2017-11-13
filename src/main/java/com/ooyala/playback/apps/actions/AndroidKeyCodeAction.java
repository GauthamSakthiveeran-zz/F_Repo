package com.ooyala.playback.apps.actions;


import java.io.IOException;
import java.time.Duration;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLine;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;


/**
 * Created by Gautham
 */

public class AndroidKeyCodeAction extends PlaybackApps implements Actions {
	
	private Logger logger = Logger.getLogger(AndroidKeyCodeAction.class);

	public AndroidKeyCodeAction(AppiumDriver driver) {
		super(driver);
		addElementToPageElements("playpause");
		// TODO Auto-generated constructor stub
	}

	// Some basic android actions keycodes - like pressing home button, back
	// To run app in background
	// button
	@Override
	public boolean startAction(String element) throws Exception {
		
		try
		{
			if(element.contains("BACK"))
				{
				((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
				return true;
				}
			else if(element.contains("SETTINGS"))
				{
				((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.SETTINGS);
				return true;
				}
			else if(element.contains("HOME"))
				{
				((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.HOME);
				return true;
				}
			else if(element.contains("ENTER"))
				{
				((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.ENTER);
				return true;
				}
			else if(element.contains("DEL"))
				{
					((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.DEL);
					return true;
				}
			else
				{
				logger.info("Key code action not added");
				extentTest.log(LogStatus.INFO, "Key code action not added");
				return false;
				}
		}

		catch(Exception e)
		{
			logger.info("Exception while doing keycode action");
			extentTest.log(LogStatus.FAIL, "Exception while doing keycode action");
			return false;
		}
		}
	
	//put app in background for some seconds
	public void runAppinBackground(long milliseconds)
	{
		((AppiumDriver) driver).runAppInBackground(Duration.ofMillis(milliseconds));		
	}
	
	//Bring app to focus from background
	public void bringBackgroundAppToFocus()
	{
		((AndroidDriver)driver).currentActivity();
	}
		
	   public boolean screenLockUnlock() throws InterruptedException,IOException {

		   boolean flag = false;
	       ((AndroidDriver)driver).lockDevice(); 
	       Thread.sleep(5000);
	       if( ((AndroidDriver)driver).isLocked() ) {
	    	    		logger.info("screen locked");
	       } else {
	    	   		logger.info("screen lock failed");
	       }
	       Thread.sleep(2000);
            //adb command to unlock device
	        String[] final_command=CommandLine.command("adb -s " + System.getProperty(CommandLineParameters.UDID) + " shell am start -n io.appium.unlock/.Unlock");
	        Runtime run = Runtime.getRuntime();
			run.exec(final_command);
			int attempts = 0;
	        while(((AndroidDriver)driver).isLocked() && attempts<10) {
	        		Thread.sleep(3000);
	        		attempts++;
	        }
	       
	        if(((AndroidDriver)driver).isLocked()) {
	        		logger.fatal("screen is not unlocked");
	        		extentTest.log(LogStatus.FAIL, "screen is not unlocked");
	        		return false;
	        } else {  	
	        		logger.info("screen is unlocked successfully");
	        		extentTest.log(LogStatus.PASS, "screen is unlocked successfully");
	        		return true;
	        }
	        
	    }
	   public boolean openAppFromAppSwitchScreen() throws InterruptedException, IOException {
		   String command = "adb -s " + System.getProperty(CommandLineParameters.UDID) + " shell input keyevent KEYCODE_HOME";
           String[] final_command = CommandLine.command(command);
           Runtime run = Runtime.getRuntime();
           run.exec(final_command);
           Thread.sleep(2000);
           command = "adb -s " + System.getProperty(CommandLineParameters.UDID) + " shell input keyevent KEYCODE_APP_SWITCH";
           final_command = CommandLine.command(command);
           run.exec(final_command);
           Thread.sleep(2000);
           logger.info("clicked on recent app switch button");
           if(waitOnElement("APP_SWITCH",5000) ) {
        	   		logger.info("App is visible in app switch screen..clicking on the app");
        	   		clickOnElement("APP_SWITCH");
        	   		return true;
           }
           return false;
	   }
	   
	}


