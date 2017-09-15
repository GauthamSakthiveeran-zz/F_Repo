package com.ooyala.playback.page;

import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class DVRLiveValidator extends PlayBackPage implements PlaybackValidator {

    private static Logger logger = Logger.getLogger(DVRLiveValidator.class);

    public DVRLiveValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("controlbar");
        addElementToPageElements("live");
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {
        return false;
    }

    public boolean validatePlayerControl(){

        if (isElementPresent("HIDDEN_CONTROL_BAR")) {
            logger.info("hovering mouse over the player");
            moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
        }

        ArrayList<String> controlBarElement = new ArrayList<String>();
        controlBarElement.addAll(Arrays.asList("PLAY_HEAD", "PLAY_PAUSE", "VOLUME_BUTTON", "SHARE_BTN", "FULLSCREEN_BTN"));
        boolean isControlShown = isElementPresent("CONTROL_BAR");

        if (!isControlShown) {
            extentTest.log(LogStatus.INFO, "Control bar is hidden hence mouse hovering on it");
            moveElement(getWebElement("CONTROL_BAR"));
        }

        if (!isElementPresent("LIVE")) {
            extentTest.log(LogStatus.FAIL, "Live button not shown in control bar.");
        }

        String livePlayHeadTime = new PlayBackFactory(driver, extentTest).getPlayerAPIAction().getPlayAheadTime()+"";

        if (!livePlayHeadTime.equals("1740")){
            extentTest.log(LogStatus.FAIL,"Scrubber pointer is not at right side \n Actual :"+livePlayHeadTime+" " +
                    "\n Expected : 1740");
            return false;
        }
        return true;
    }

    public boolean validateSeek(){
        String playHeadTimeAfterSeek;

        try {
            if (!performActionByJs("pp.seek(15)")){
                return false;
            }

            if (!loadingSpinner()){
                extentTest.log(LogStatus.FAIL,"It seems that video is buffering for long time after seeking it");
                return false;
            }

            if (!waitOnElement(By.id("videoSeek_1"),10000)){
                extentTest.log(LogStatus.FAIL,"seek event is not found after seeking the live DVR video");
                return false;
            }

            playHeadTimeAfterSeek = (String) executeJsScript("pp.getPlayheadTime()","string");

            if (!playHeadTimeAfterSeek.equals("15")){
                extentTest.log(LogStatus.FAIL," playhead time after seek is not matching \n Expected time : 15 \n Actual Time :"+playHeadTimeAfterSeek);
                return false;
            }

        } catch (Exception ex){
            extentTest.log(LogStatus.FAIL,ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean validatePlayerTime(){
        String playHeadTimeAfterSeek;
        int playHeadTimeAfterPausePlay;
        int videoPausedFor=0;

        try {
            if (!performActionByJs("pp.seek(150)")){
                return false;
            }

            if (!loadingSpinner()){
                extentTest.log(LogStatus.FAIL,"It seems that video is buffering for long time after seeking it");
                return false;
            }

            if (!waitOnElement(By.id("videoSeek_1"),10000)){
                extentTest.log(LogStatus.FAIL,"seek event is not found after seeking the live DVR video");
                return false;
            }

            playHeadTimeAfterSeek = (String) executeJsScript("pp.getPlayheadTime()","string");

            if (!performActionByJs("pp.pause()")){
                extentTest.log(LogStatus.FAIL,"unable to pause the video using JS");
                return false;
            }

            // Wait for 10 secs
            extentTest.log(LogStatus.INFO,"waiting 10 sec in paused state");
            while(videoPausedFor<10){
                Thread.sleep(1000);
                videoPausedFor++;
            }

            if (!performActionByJs("pp.play()")){
                extentTest.log(LogStatus.FAIL,"unable to play the video using JS");
                return false;
            }

            if (!loadingSpinner()){
                return false;
            }

            playHeadTimeAfterPausePlay = (Integer) executeJsScript("pp.getPlayheadTime().toFixed()","int");

            if (!((playHeadTimeAfterPausePlay >= (Integer.parseInt(playHeadTimeAfterSeek) - videoPausedFor)-1)
                    && playHeadTimeAfterPausePlay <= (Integer.parseInt(playHeadTimeAfterSeek) - videoPausedFor)+1)){
                extentTest.log(LogStatus.FAIL,
                        "playhead time is not matching after play/pause action \n " +
                                "Expected : "+(Integer.parseInt(playHeadTimeAfterSeek) - videoPausedFor)+"" +
                                "\n Actual :"+playHeadTimeAfterPausePlay);
                return false;
            }

        } catch (Exception ex){
            extentTest.log(LogStatus.FAIL,ex.getMessage());
            return false;
        }
        return true;
    }
}
