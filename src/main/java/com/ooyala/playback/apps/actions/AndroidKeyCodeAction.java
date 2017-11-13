package com.ooyala.playback.apps.actions;


import java.time.Duration;

import org.apache.log4j.Logger;

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
			else if(element.equals("LOCK"))
			{
				((AndroidDriver)driver).lockDevice();
				return true;
			}
			else if(element.equals("UNLOCK"))
			{
					String[] unLockCommand=CommandLine.command("adb -s " + System.getProperty(CommandLineParameters.UDID) + " shell am start -n io.appium.unlock/.Unlock");
			        Runtime run = Runtime.getRuntime();
					run.exec(unLockCommand);
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
	
	//Lock the Device
	public Boolean lockTheDevice()
	{
		try
		{
			
			
		((AndroidDriver)driver).lockDevice();
		
		if(((AndroidDriver)driver).isLocked())
		{
			logger.info("Device Locked Properly");
			extentTest.log(LogStatus.INFO, "Device Locked Properly");
			return true;
		}
		else
		{
			logger.info("Device not Locked Properly");
			extentTest.log(LogStatus.FAIL, "Device not Locked Properly");
			return false;
		}
		}
		catch(Exception e)
		{
			logger.info("Exception while locking device");
			extentTest.log(LogStatus.FAIL, "Exception while locking device");
			return false;
		}
	
	}
	
	//UnLock the Device
	public Boolean unlockTheDevice()
	{
		try
		{
		((AndroidDriver)driver).unlockDevice();
		Thread.sleep(5000);
		if(!((AndroidDriver)driver).isLocked())
		{
			logger.info("Device UnLocked Properly");
			extentTest.log(LogStatus.INFO, "Device UnLocked Properly");
			return true;
		}
		else
		{
			logger.info("Device not UnLocked Properly");
			extentTest.log(LogStatus.FAIL, "Device not UnLocked Properly");
			return false;
		}
		}
		catch(Exception e)
		{
			logger.info("Device not UnLocked Properly");
			extentTest.log(LogStatus.FAIL, "Exception while Unlocking device");
			return false;
		}
		
	
	}
		
		
	}


