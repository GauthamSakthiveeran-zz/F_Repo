package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 10/27/16.
 */
public class PauseValidator extends PlayBackPage implements PlaybackValidator {

    private static Logger logger = Logger.getLogger(PauseValidator.class);

    public PauseValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("pause");
        addElementToPageElements("discovery");
    }

    public boolean validate(String element, int timeout) throws Exception {
        if (isElementPresent("HIDDEN_CONTROL_BAR")) {
            logger.info("hovering mouse over the player");
            moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
        }
        if (!clickOnIndependentElement("PAUSE_BUTTON")) {
            extentTest.log(LogStatus.FAIL, "Failed to click on PAUSE_BUTTON");
            logger.error("Failed to click on PAUSE_BUTTON");
            return false;
        }
        // If discovery is enabled then we are not able to see pause screen and therefore handled that scenario
        // for discovery
        if (getAdditionalPlugin().toLowerCase().contains("discovery")) {
            if (!waitOnElement("CONTENT_SCREEN", 2000)) {
                if (!waitOnElement("PAUSE_SCREEN", 2000)) {
                /*if (getBrowser().contains("safari")) {
                    clickOnHiddenElement("PAUSE_BUTTON");
				} else {
					extentTest.log(LogStatus.FAIL, "Wait on PAUSE_SCREEN failed");
					return false;
				}*/
                    extentTest.log(LogStatus.FAIL, "Wait on PAUSE_SCREEN failed");
                    logger.error("Wait on PAUSE_SCREEN failed");
                }
            }
        }
        if (!waitOnElement(By.id(element), timeout)) {
            extentTest.log(LogStatus.FAIL, element + " not found.");
            logger.error(element + " not found.");
            return false;
        }
        if (isVideoPluginPresent("ANALYTICS")) {
            if (!(isAnalyticsElementPreset("analytics_video_" + element)
                    && isAnalyticsElementPreset("analytics_video_requested_" + element))) {
                logger.error("analytics_video_requested_" + element + " not found");
                extentTest.log(LogStatus.FAIL, "analytics_video_requested_" + element + " not found");
            }
        }
        //return new PlayBackFactory(driver, extentTest).getScrubberValidator().validate("", 1000);
        return true;
    }
}
