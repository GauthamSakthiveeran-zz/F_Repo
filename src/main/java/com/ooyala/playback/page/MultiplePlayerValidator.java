package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.SeekAction;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by jitendra on 28/11/16.
 */
public class MultiplePlayerValidator extends PlayBackPage implements PlaybackValidator  {


    public MultiplePlayerValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("multipleplayer");
        addElementToPageElements("replay");
    }

    @Override
    public void validate(String element, int timeout) throws Exception {

        if (element.equalsIgnoreCase("player1_play")){
            waitOnElement("PLAYER1_START_SCREEN",timeout);
            clickOnHiddenElement("PLAYER1_START_SCREEN");
            logger.info("Player 1 playing");
        }

        if (element.contains("player1_pause")){
            waitOnElement("PLAYER1_PAUSE",timeout);
            clickOnHiddenElement("PLAYER1_PAUSE");
            logger.info("Player 1 paused");
        }

        if (element.equalsIgnoreCase("player2_play")){
            waitOnElement("PLAYER2_START_SCREEN",timeout);
            clickOnHiddenElement("PLAYER2_START_SCREEN");
            logger.info("Player 2 playing");
        }

        if (element.contains("player2_pause")){
            waitOnElement("PLAYER2_PAUSE",timeout);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
            clickOnHiddenElement("PLAYER2_PAUSE");
            logger.info("Player 2 paused");
        }

        if (element.contentEquals("seek1")){
            if ((Boolean) ((JavascriptExecutor) driver).executeScript("return pp1.isPlaying();")){
                ((JavascriptExecutor) driver).executeScript("return pp1.seek(pp1.seek(pp1.getDuration()-1));");
                logger.info("Video 1 is Seeked");
            } else {
                clickOnHiddenElement("PLAYER1_START_SCREEN");
                ((JavascriptExecutor) driver).executeScript("return pp1.seek(pp1.seek(pp1.getDuration()-1));");
                logger.info("Video 1 is Seeked");
            }
        }

        if (element.contentEquals("seek2")){
            if ((Boolean) ((JavascriptExecutor) driver).executeScript("return pp2.isPlaying();")){
                ((JavascriptExecutor) driver).executeScript("return pp1.seek(pp2.seek(pp2.getDuration()-1));");
                logger.info("Video 2 is Seeked");
            } else {
                clickOnHiddenElement("PLAYER2_START_SCREEN");
                ((JavascriptExecutor) driver).executeScript("return pp1.seek(pp2.seek(pp2.getDuration()-1));");
                logger.info("Video 2 is Seeked");
            }
        }

    }
}
