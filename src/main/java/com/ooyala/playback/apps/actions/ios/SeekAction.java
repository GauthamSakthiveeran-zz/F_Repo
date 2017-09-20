package com.ooyala.playback.apps.actions.ios;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.actions.Actions;

import io.appium.java_client.AppiumDriver;

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

    public boolean seekBack() throws Exception {
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

    public boolean seekForward(String ele1, String ele2) throws Exception {
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

}
