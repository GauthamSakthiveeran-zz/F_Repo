package com.ooyala.playback.apps.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by Gautham
 */
public class ClickDiscoveryButtonAction extends PlaybackApps implements Actions {

    private Logger logger = Logger.getLogger(ClickDiscoveryButtonAction.class);

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

        try {
            if (((AndroidDriver) driver).currentActivity()
                    .equals("com.ooyala.sample.players.OoyalaSkinPlayerActivity")) {

                result = result && tapAndwaitForElementAndClick("MOREOPTIONS_BUTTON_ANDROID");

                result = result && waitForElementAndClick("DISCOVERYBUTTON_ANDROID");

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

    // Function to close Discovery Screen and MoreOptions Screen
    public boolean closeDiscoveryScreen(String element) throws Exception {

        Boolean result = true;

        try

        {
            result = result && waitForElementAndClick("DISCOVERYSCREEN_CLOSEBUTTON_ANDROID");

            result = result && waitForElementAndClick("MOREOPTIONSSCREEN_CLOSEBUTTON_ANDROID");

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Closing Discovery Screen");
            logger.info("Exception While Closing Discovery Screen");
            return false;

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

    public void tapPlayerScreen() {

        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        extentTest.log(LogStatus.INFO, "Single tap done");

    }

    public void doubletapPlayerScreen() {

        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        extentTest.log(LogStatus.INFO, "Double tap done");

    }
}
