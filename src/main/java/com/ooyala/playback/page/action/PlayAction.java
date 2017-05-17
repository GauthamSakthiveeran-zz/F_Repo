package com.ooyala.playback.page.action;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;
import com.relevantcodes.extentreports.LogStatus;

public class PlayAction extends PlayBackPage implements PlayerAction {

    public static Logger logger = Logger.getLogger(PlayAction.class);
    public PlayAction(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        /**
         * Here we will tell Facile to add the page elements of our Login Page
         */
        addElementToPageElements("play");
        addElementToPageElements("startscreen");
    }

    private boolean onScreen = false;

    @Override
    public boolean startAction() throws Exception {
        if (!loadingSpinner()) {
            extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
            return false;
        }
        if (onScreen) {
            return startActionOnScreen();
        }

        if (!loadingSpinner()) {
            extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
            return false;
        }
        if (clickOnIndependentElement("PLAY_BUTTON")) {
            if (!loadingSpinner()) {
                extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
                return false;
            }
            if (getBrowser().contains("safari")) {
                WebElement element1 = getWebElement("PLAY_BUTTON");
                driver.executeScript("arguments[0].click()",element1);
            }
        }
        return true;
    }

    public PlayAction onScreen() {
        onScreen = true;
        return this;
    }

    private boolean startActionOnScreen() throws Exception {
        try {
            if (!clickOnIndependentElement("STATE_SCREEN_SELECTABLE"))
                return false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            moveElement(getWebElement("PLAY_BUTTON"));
            Thread.sleep(5000);
            return startAction();
        }
        return true;
    }
}
