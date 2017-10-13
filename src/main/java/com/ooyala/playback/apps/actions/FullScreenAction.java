package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by Gautham
 */
public class FullScreenAction extends PlaybackApps implements Actions {

    private Logger logger = Logger.getLogger(FullScreenAction.class);

    public FullScreenAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("fullscreen");
    }

    // Function to click FullScreen button

    @Override
    public boolean startAction(String element) throws Exception {
        // TODO Auto-generated method stub
        Boolean result = true;
        if(getPlatform().contains("android"))
        {
        try {
            if (((AndroidDriver) driver).currentActivity()
                    .equals("com.ooyala.sample.players.OoyalaSkinPlayerActivity")) {

                result = result && tapAndwaitForElementAndClick("MOREOPTIONS_BUTTON_ANDROID");

                result = result && waitForElementAndClick("element");

                return result;
            } else {
                extentTest.log(LogStatus.FAIL, "Player Screen not displayed");
                logger.info("Player Screen not displayed");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Clicking FullScreen");
            logger.info("Exception While Clicking FullScreen");
            return false;

        }
        }
        else
        {
            //Code for IOS
        	try
        	{
        		result = result && waitForElementAndClick(element);
        		
        		return result;
        	}
        	catch(Exception e)
        	{
        		
        		if(singletapPlayerScreen () && waitOnElement(element, 2000))
        		{
        			return waitForElementAndClick(element);
        			
        		}
        		else
        		{
                extentTest.log(LogStatus.FAIL, "Exception While Clicking FullScreen");
                logger.info("Exception While Clicking FullScreen");
                return false;
        		}

        	}
            
        }

    }

    // Function to close Discovery Screen and MoreOptions Screen
    public boolean closeFullScreen(String element) throws Exception {

        Boolean result = true;

        if(getPlatform().equalsIgnoreCase("android"))
        {
        try

        {
            result = result && waitForElementAndClick(element);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Clicking Close FullScreen");
            logger.info("Exception While Clicking Close FullScreen");
            return false;

        }
        }
        else
        {
            //Code for IOS
        	try
        	{
            result = result && waitForElementAndClick(element);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Clicking Close FullScreen");
            logger.info("Exception While Clicking Close FullScreen");
            return false;

        }
            
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
    
    public boolean singletapPlayerScreen() {
        try
        {
        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        Thread.sleep(1000);
        extentTest.log(LogStatus.INFO, "Single tap done");
        return true;
        }
        catch(Exception e)
        {
        extentTest.log(LogStatus.FAIL, "Single tap failed");   
        return false;
        }

    }


    public void doubletapPlayerScreen() {
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
    public boolean tapAndwaitForElementAndClick(String element) {
        try {

            doubletapPlayerScreen();

            if (waitOnElement(element, 3000)) {
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
}


