package com.ooyala.playback.apps.actions;

import java.util.List;

import org.apache.log4j.Logger;
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
public class SwipeUpDownAppAssetsAction extends PlaybackApps implements Actions {

    private Logger logger = Logger.getLogger(SwipeUpDownAppAssetsAction.class);

    public SwipeUpDownAppAssetsAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("appassets");
    }

    // Function to click an AppAsset based on video name text

    @Override
    public boolean startAction(String element) throws Exception {
        // TODO Auto-generated method stub
        Boolean result = true;
        
        if(System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("android"))
        {
        
        try {
            if (((AndroidDriver) driver).currentActivity().contains("ListActivity")) {

                result = result && waitForElement("APP_ASSETS_SCREEN_ANDROID");

                result = result && clickAppAsset(element);

                return result;
            } else {
                extentTest.log(LogStatus.FAIL, "Current Activity is Not in Assets List Activity");
                logger.info("Current Activity is Not in Assets List Activity");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Clicking App Asset");
            logger.info("Exception While Clicking App Asset");
            return false;

        }
        }
        else
        {
            //Code for IOS
            return true;
        }

    }

    public Boolean clickAppAsset(String element) {
        Boolean foundVideoToClick = false;
        
        if(System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("android"))
        {
        try {
            List<WebElement> appVideos = getWebElementsList("APP_ASSETS_ANDROID");

            for (WebElement appVideo : appVideos) {
                if (appVideo.getText().equals(element)) {
                    appVideo.click();
                    foundVideoToClick = true;
                    break;
                }

            }

            if (!foundVideoToClick) {
                swipeBasedOnWebElements(appVideos.get(appVideos.size() - 1), appVideos.get(1));
                Thread.sleep(2000);
                appVideos = getWebElementsList("APP_ASSETS_ANDROID");
                for (WebElement appVideo : appVideos) {
                    if (appVideo.getText().equals(element)) {
                        appVideo.click();
                        foundVideoToClick = true;
                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Selecting App Asset");
            logger.info("Exception While Selecting App Asset");
            return false;
        }

        return foundVideoToClick;
        }
        else
        {
            //Code for IOS
            return foundVideoToClick;
        }
    }

    // To Swipe based on WebElements - UpDown (OR) RightLeft
    public void swipeBasedOnWebElements(WebElement start, WebElement end) {
        TouchAction a2 = new TouchAction((AppiumDriver) driver);
        a2.longPress(start).moveTo(end).release().perform();
        extentTest.log(LogStatus.INFO, "Swipe done on screen");
    }

    // To Swipe Top to Bottom (OR) Bottom to Top
    public void verticalSwipeBasedOnCoordinatePoints(int startX, int startY, int endY) {
        TouchAction a2 = new TouchAction((AppiumDriver) driver);
        a2.press(startX, startY).moveTo(startX, endY).release().perform();
        extentTest.log(LogStatus.INFO, "Vertical Swipe done on screen");
    }

    // To Swipe Right to Left (OR) Left to Right
    public void horizontalSwipeBasedOnCoordinatePoints(int startX, int startY, int endX) {
        TouchAction a2 = new TouchAction((AppiumDriver) driver);
        a2.press(startX, startY).moveTo(endX, startY).release().perform();
        extentTest.log(LogStatus.INFO, "Horizontal Swipe done on screen");
    }

    // Function to click on app Asset using video name Web Element
    public Boolean clickAsset(String element) {
        if(System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("android"))
        {
        try {
            if (waitForElement("APP_ASSETS_SCREEN_ANDROID")) {
                getWebElement(element).click();
                extentTest.log(LogStatus.INFO, "App Asset Clicked");
                logger.info("App Asset Clicked");
                return true;
            } else {
                extentTest.log(LogStatus.FAIL, "App Assets Screen not displayed");
                logger.info("App Assets Screen not displayed");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Failed to click App Asset");
            logger.info("Failed to click App Asset");
            return false;
        }
        }
        else
        {
            //Code for IOS
            return true;
        }
    }

    // Function to Swipe and click on app Asset using video name Web Element
    public Boolean swipeAndClickAsset(String element) {

        Boolean foundVideoToClick = false;
        if(System.getProperty(CommandLineParameters.PLATFORM).equalsIgnoreCase("android"))
        {
        try {
            if (waitForElement("APP_ASSETS_ANDROID")) {

                List<WebElement> appVideos = getWebElementsList("APP_ASSETS_ANDROID");
                swipeBasedOnWebElements(appVideos.get(appVideos.size() - 1), appVideos.get(1));
                Thread.sleep(2000);
                getWebElement(element).click();
                logger.info("App Asset Clicked");
                foundVideoToClick = true;

            } else {
                logger.info("App Assets Screen not displayed");
                foundVideoToClick = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Failed to click App Asset");
            foundVideoToClick = false;
        }
        return foundVideoToClick;
        }
        else
        {
            //Code for IOS
            return foundVideoToClick;
        }
       
    }

    public Boolean waitForElement(String element) {
        Boolean result = false;
        try {

            if (waitOnElement(element, 30000)) {
                WebElement elementToFind = getWebElement(element);
                if (elementToFind.isEnabled()) {
                    extentTest.log(LogStatus.INFO, "Asset List Found");
                    logger.info("Asset List Found");
                    result = true;
                } else {
                    extentTest.log(LogStatus.INFO, "Asset List not Found");
                    logger.info("Asset List not  Found");
                    result = false;

                }
            } else {
                extentTest.log(LogStatus.FAIL, "Element not found");
                result = false;

            }

        } catch (Exception e) {

            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Asset List not Found");
            logger.info("Asset List not Found");
            result = false;

        }
        return result;
    }

}
