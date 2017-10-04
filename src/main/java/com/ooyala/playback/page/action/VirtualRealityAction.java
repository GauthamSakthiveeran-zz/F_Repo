package com.ooyala.playback.page.action;

import com.ooyala.playback.page.PlayBackPage;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import java.awt.*;
import java.util.List;

public class VirtualRealityAction extends PlayBackPage implements PlayerAction {

    private Logger logger = Logger.getLogger(VirtualRealityAction.class);

    public VirtualRealityAction(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        // Here we will tell Facile to add the page elements of our Login Page
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("controlbar");
        addElementToPageElements("vr_controls");
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
        return true;
    }

    public VirtualRealityAction onScreen(){
        onScreen = true;
        return this;
    }

    public boolean startActionOnScreen() throws AWTException {
        Actions a = new Actions(driver);

      try {
          if (isElementPresent("HIDDEN_CONTROL_BAR")) {
              logger.info("hovering mouse over the player");
              moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
          }

          WebElement screen = getWebElement("STATE_SCREEN_SELECTABLE");

          if(!isElementPresent("CONTROL_BAR")){
              extentTest.log(LogStatus.FAIL, "Pause button not found.");
              return false;
          }

          a.dragAndDropBy(screen, 100, 100);
          Action act = a.build();
          act.perform();

          return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    public boolean startActionOnScreenUsingControls(String controlButton){
        try {
            if (isElementPresent("HIDDEN_CONTROL_BAR")) {
                logger.info("hovering mouse over the player");
                moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
            }

            if(!isElementPresent("VR_CONTROLS")){
                extentTest.log(LogStatus.FAIL, "VR controls not found.");
                return false;
            }
            clickOnIndependentElement(controlButton);
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    public boolean startActionUsingKeys(String keyButton){
        try {
            if(!isElementPresent("STATE_SCREEN_SELECTABLE")){
                extentTest.log(LogStatus.FAIL, "Player not found.");
                return false;
            }

            Actions actions = new Actions(driver);
            WebElement screen = getWebElement("STATE_SCREEN_SELECTABLE");
            actions.moveToElement(screen).perform();
            actions.sendKeys(keyButton).perform();
            actions.build().perform();

            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    public boolean startActionByMouse(){
        try {
            Actions actions = new Actions(driver);
            WebElement screen = getWebElement("STATE_SCREEN_SELECTABLE");
            actions.click(screen);
            actions.build().perform();
            return true;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

    public boolean validateElement(String element, int timeout) throws Exception {
        if (driver.findElementsById(element).size()!=0) {
            logger.info("Wait on element Success : " + element);
            extentTest.log(LogStatus.PASS, "Wait on element : " + element);
            return true;
        }
        logger.error("Wait on element failed : " + element);
        extentTest.log(LogStatus.FAIL, "Wait on element : " + element);
        return false;
    }

    public boolean verifyCardboardIcon() {
        String platformNameActual = driver.getCapabilities().getPlatform().name();
        logger.info("Platform is " + platformNameActual);
        if(platformNameActual.equalsIgnoreCase("ANDROID") | platformNameActual.equalsIgnoreCase("IOS")) {
            WebElement cardboardIcon = driver.findElementById("CARDBOARD_ICON");
            logger.info("For web platform cardboard icon should not be visible");
            return cardboardIcon.getAttribute("data-focus-id").equalsIgnoreCase("stereo");
        }
        else {
            logger.info("For mobile platform cardboard icon should be visible");
            return driver.findElementsById("CARDBOARD_ICON").size() < 1;
        }
    }

}


