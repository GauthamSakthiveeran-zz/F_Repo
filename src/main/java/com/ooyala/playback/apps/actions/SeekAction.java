package com.ooyala.playback.apps.actions;

import com.ooyala.playback.apps.PlaybackApps;
import io.appium.java_client.AppiumDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SeekAction extends PlaybackApps implements Actions {

    private static Logger logger = Logger.getLogger(SeekAction.class);

    public SeekAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("seekbar");
    }

    @Override
    public boolean startAction(String element) throws Exception {
        return false;
    }

    public boolean startAction_iOS_V3_Back() throws Exception {
        try {
            if (!seekVideoBack("" , "" )) {
                logger.error("Unable to click on play pause.");
                return false;
            }
        } catch (Exception e) {
            logger.info("seekbar not found. Tapping screen and retrying..");
            tapScreenIfRequired();
            if(!seekVideoBack("" , "")) {
                logger.error("Unable to click on play pause.");
                return false;
            }
        }
        return true;
    }

    public boolean startAction_iOS_V3_Forward(String ele1, String ele2) throws Exception {
        try {
            if (!seekVideoForward(ele1 , ele2 )) {
                logger.error("Unable to seek forward video.");
                return false;
            }
        } catch (Exception e) {
            logger.info("seekbar not found. Tapping screen and retrying..");
            tapScreenIfRequired();
            if(!seekVideoForward(ele1 , ele2 )) {
                logger.error("Unable to seek forward video");
                return false;
            }
        }
        return true;
    }

    public boolean seekVideoAndroid(String element) throws Exception {
        try {
            if(!seekVideo(element)) {
                logger.error("Unable to seek Video, Seekbar was not present");
                return false;
            }
        } catch (Exception e) {
            logger.info("Seekbar is not present , let's tap on screen");
            tapOnScreen();
            if(!seekVideo(element)) {
                logger.error("Unable to click on play pause.");
                return false;
            }
        }
        return true;
    }





}
