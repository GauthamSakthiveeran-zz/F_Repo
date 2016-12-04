package com.ooyala.playback.page.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.page.PlayBackPage;
import com.relevantcodes.extentreports.LogStatus;

public class FullScreenAction extends PlayBackPage implements PlayerAction {

    public FullScreenAction(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("fullscreen");
        addElementToPageElements("pause");
        addElementToPageElements("play");
    }

    @Override
    public boolean startAction() throws Exception {
        WebElement player = getWebElement("OOPLAYER");

        if(player==null) {
        //    extentTest.log(LogStatus.FAIL, "OOPLAYER is null");
            return false;
        }
        moveElement(player);

        clickOnIndependentElement("STATE_SCREEN_SELECTABLE");
        Thread.sleep(1000);
        if(!clickOnIndependentElement("FULLSCREEN_BTN_1")) {
          //  extentTest.log(LogStatus.FAIL, "FULLSCREEN_BTN not found");
            return false;
        }


        if (!(getBrowser().equalsIgnoreCase("safari")
                || getBrowser().equalsIgnoreCase("firefox")
                || getBrowser().equalsIgnoreCase("internet explorer") || getPlatform()
                .equalsIgnoreCase("Android"))) {

            if(!waitOnElement(By.id("fullscreenChanged_true"), 60000)) {
             //   extentTest.log(LogStatus.FAIL, "fullscreenChangedtrue not found");
                return false;
            }

            logger.info("Changed into Fullscreen");

        }
        //extentTest.log(LogStatus.PASS, "Full Screen Validated!");
        return true;

    }
}
