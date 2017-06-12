package com.ooyala.playback.page;

import com.ooyala.playback.url.UrlObject;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;

public class PlayValidator extends PlayBackPage implements PlaybackValidator {

    private static Logger logger = Logger.getLogger(PlayValidator.class);

    public PlayValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("play");
    }

    private void errorDescription() {
        if (isElementPresent("ERROR_SCREEN")) {
            String text = getWebElement("ERROR_DESCRIPTION").getText();
            extentTest.log(LogStatus.FAIL, text);
        } else{
        	extentTest.log(LogStatus.FAIL, "Player did not load.");
        }
        
    }

    @Override
    public boolean waitForPage() {
        boolean errorScreen = false;
        try {

            if (!loadingSpinner()) {
                extentTest.log(LogStatus.FAIL, "In loading spinner for a very long time.");
                return false;
            }

            if (!waitOnElement("PLAY_BUTTON", 90000)) {
                errorDescription();
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            driver.navigate().refresh();
            if (!waitOnElement("INNER_WRAPPER", 60000))
                return false;
            errorScreen = isElementPresent("ERROR_SCREEN");
            if (errorScreen)
                driver.navigate().refresh();
            if (!waitOnElement("PLAY_BUTTON", 60000)) {
                errorDescription();
                return false;
            }
        }
        logger.info("Page is loaded completely");
        return true;
    }

    public boolean validate(String element, int timeout) throws Exception {
        // if(!PlayBackFactory.getInstance(driver).getPlayAction().startAction())
        // return false;
        if (!clickOnIndependentElement("PLAY_BUTTON")) {
            extentTest.log(LogStatus.FAIL, "FAILED to click on PLAY_BUTTON.");
            return false;
        }

        if (!loadingSpinner()) {
            extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
            return false;
        }

        if (!waitOnElement("PLAYING_SCREEN", 4000)) {
            if (getBrowser().contains("safari")) {
                int count = 4;
                boolean flag = false;
                while (count >= 0) {
                	if (waitOnElement(By.id(element), 4000)) {
                        extentTest.log(LogStatus.PASS,
                                "Video Playing and validation of element " + element + " is successful");
                        flag = true;
                        break;
                    }
                    if (!clickOnIndependentElement("PLAY_BUTTON")) {
                        extentTest.log(LogStatus.FAIL, "FAILED to click on PLAY_BUTTON.");
                        return false;
                    }
                    count--;
                }
                if(!flag)
                	clickOnHiddenElement("PLAY_BUTTON");
            } else {
                return false;
            }
        }

        if (!getBrowser().equalsIgnoreCase("internet explorer")) {
            if (!waitOnElement(By.id(element), timeout))
                return false;
        }

        if (UrlObject.getUrlObject().getVideoPlugins().contains("ANALYTICS")){
            if(!(isAnalyticsElementPreset("analytics_video_"+element)
                    && isAnalyticsElementPreset("analytics_video_requested_"+element))){
                return false;
            }
        }

        extentTest.log(LogStatus.PASS, "Video Playing and validation of element " + element + " is successful");

        if (!new PlayBackFactory(driver, extentTest).getVideoValidator().getConsoleLogs().validate("", timeout)) {
            return false;
        }
        return true;
    }
}
