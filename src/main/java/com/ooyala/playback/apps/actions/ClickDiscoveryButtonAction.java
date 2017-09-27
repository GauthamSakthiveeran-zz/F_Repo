package com.ooyala.playback.apps.actions;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

/**
 * Created by Gautham
 */
public class ClickDiscoveryButtonAction extends PlaybackApps implements Actions {

    private Logger logger = Logger.getLogger(ClickDiscoveryButtonAction.class);
    public final static int[] p = new int[2];

    public ClickDiscoveryButtonAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("discovery");
    }

    // Function to click Disciovery button

    @Override
    public boolean startAction(String element) throws Exception {
        // TODO Auto-generated method stub
        Boolean result = true;
        if(System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("android"))
        {
        try {
            if (((AndroidDriver) driver).currentActivity()
                    .equals("com.ooyala.sample.players.OoyalaSkinPlayerActivity")) {

                result = result && tapAndwaitForElementAndClick("MOREOPTIONS_BUTTON_ANDROID");

                result = result && waitForElementAndClick(element);

                return result;
            } else {
                extentTest.log(LogStatus.FAIL, "Player Screen not displayed");
                logger.info("Player Screen not displayed");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Clicking Discovery Button");
            logger.info("Exception While Clicking Discovery Button");
            return false;

        }
        }
        else
        {
            //Code for IOS
            return true;
        }

    }

    // Function to close Discovery Screen and MoreOptions Screen
    public boolean closeDiscoveryScreen(String element) throws Exception {

        Boolean result = true;

        if(System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("android"))
        {
        try

        {
            result = result && waitForElementAndClick(element);

            result = result && waitForElementAndClick("MOREOPTIONSSCREEN_CLOSEBUTTON_ANDROID");

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Closing Discovery Screen");
            logger.info("Exception While Closing Discovery Screen");
            return false;

        }
        }
        else
        {
            //Code for IOS
            return true;
        }

    }

    public boolean waitForElementAndClick(String element) {
        try {

            if (waitOnElement(element, 5000)) {
                WebElement elementToFind = getWebElement(element);
                elementToFind.click();
                extentTest.log(LogStatus.INFO, "Clicked Element");
                return true;
            } else {
                extentTest.log(LogStatus.FAIL, "Element not found");
                return true;

            }

        } catch (Exception e) {

            e.printStackTrace();
            logger.info("Failed to Click Element");
            extentTest.log(LogStatus.FAIL, "Failed to Click Element");
            return false;

        }
    }

    public boolean tapAndwaitForElementAndClick(String element) {
        try {

    		clickPauseButton();
        	
            if (waitOnElement(element, 5000)) {
                WebElement elementToFind = getWebElement(element);
                elementToFind.click();
                extentTest.log(LogStatus.INFO, "Clicked Element");
                return true;
            } else {
            	tapinMoiddleOfPlayerScreen();
            	WebElement elementToFind = getWebElement(element);
                elementToFind.click();
                extentTest.log(LogStatus.FAIL, "Element not found");
                return true;

            }

        } catch (Exception e) {

            e.printStackTrace();
            logger.info("Failed to Click Element");
            extentTest.log(LogStatus.FAIL, "Failed to Click Element");
            return false;

        }
    }

    public void tapinMoiddleOfPlayerScreen() {

        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        extentTest.log(LogStatus.INFO, "Single tap done");

    }

    public void doubletapinMiddleofPlayerScreen() {
        try
        {
        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        Thread.sleep(2000);
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        extentTest.log(LogStatus.INFO, "Double tap done");
        }
        catch(Exception e)
        {
        extentTest.log(LogStatus.FAIL, "Double tap failed");   
        }

    }
    
    public boolean clickPlayButton()
    {
    	try
    	{
 
    	Thread.sleep(2000);
		WebElement Play = getWebElement("PLAY_PAUSE_BUTTON_ANDROID");
		p[0] = Play.getLocation().getX() + Play.getSize().getWidth()/2;
		p[1] = Play.getLocation().getY() + Play.getSize().getHeight()/2;
        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap(p[0],p[1]).perform();
        logger.info("clicked play button");
        return true;
    	}
    	catch (Exception e)
    	{
    	logger.info("failed play button");
    	return false;
    	}
		
    }
    public boolean clickPauseButton()
    {
    	try
    	{
    	Thread.sleep(2000);
        TouchAction touch = new TouchAction((AppiumDriver) driver);
        touch.tap(p[0],p[1]).perform();
        Thread.sleep(2000);
        touch.tap(p[0],p[1]).perform();
        logger.info("clicked pause button");
        return true;
    	}
        catch(Exception e){
        	logger.info("failed pause button");
        	return false;
    		
    	}
		
    }
}
