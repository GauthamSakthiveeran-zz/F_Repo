package com.ooyala.playback.page;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import java.util.ArrayList;

import com.ooyala.playback.factory.PlayBackFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 29/12/16.
 */
public class OoyalaAPIValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(OoyalaAPIValidator.class);

    public OoyalaAPIValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("adclicks");
        addElementToPageElements("fullscreen");
    }


    String description = null;
    String embedCode = null;
    String title = null;

    @SuppressWarnings("unchecked")
    @Override
    public boolean validate(String element, int timeout) throws Exception {

        /***
         * For android autoPlay does not work and therefore we are not checking autoplay in following
         * if statement
         * Therefore we are playing video explicitly in following else statement....
         */
        if (!getPlatform().equalsIgnoreCase("android")) {
            Boolean autoPlay = (Boolean) (driver.executeScript("return pp.parameters.autoplay"));

            if (!autoPlay) {
                logger.error("Not able to Autoplay");
                extentTest.log(LogStatus.FAIL,"Not able to Autoplay");
                return false;
            }
        } else {
            PlayBackFactory factory = new PlayBackFactory(driver,extentTest);
            factory.getPlayValidator().waitForPage();
            factory.getPlayAction().startAction();
        }

        if(!waitOnElement("AD_PANEL",10000)){
            logger.error("ad is not playing");
            extentTest.log(LogStatus.FAIL,"ad is not playing");
            return false;
        }

        boolean isAdPlaying = (Boolean) (driver.executeScript("return pp.isAdPlaying()"));
        logger.info("is Ad playing :"+isAdPlaying);
        extentTest.log(LogStatus.INFO,"is Ad playing :"+isAdPlaying);
        if(!isAdPlaying){
            logger.error("Ad not playing");
            extentTest.log(LogStatus.FAIL,"Ad not playing");
            return false;
        }

        int showAdSkipBtnTime = parseInt((driver.executeScript("return pp.parameters.linearAdSkipButtonStartTime")).toString());
        while (true) {
            double adPlayHeadTime = parseDouble(driver.executeScript("return pp.getPlayheadTime()").toString());
            if (adPlayHeadTime > (double) showAdSkipBtnTime) {
                driver.executeScript("pp.skipAd();");
                break;
            }

        }

        if(!waitOnElement(By.id("skipAd_1"), 5000)){
            logger.error("Not able to skip the ad");
            extentTest.log(LogStatus.FAIL,"Not able to skip the ad");
            return false;
        }

        if(!loadingSpinner()){
            extentTest.log(LogStatus.FAIL,"loading spinner is present for long time");
            return false;
        }

        if (!waitOnElement(By.id("videoPlaying_1"),10000)){
            logger.error("Video is not playing");
            extentTest.log(LogStatus.FAIL,"Video is not playing");
            return false;
        }

        /***
         * As for Android initialTime parameter not working, we are not checking condition
         * in following if statement for android platform
         */
        if (!getPlatform().equalsIgnoreCase("android")) {
            double initialTime = parseDouble(driver.executeScript("return pp.parameters.initialTime").toString());
            double playHeadTime = parseDouble(driver.executeScript("return pp.getPlayheadTime()").toString());
            if (!(playHeadTime > initialTime)) {
                logger.error("Video playback not started from initial time");
                extentTest.log(LogStatus.FAIL,"Video playback not started from initial time");
                return false;
            }
        }

        double totalTime = parseDouble(driver.executeScript("return pp.getDuration()").toString());
        logger.info("Duration of video is : "+totalTime);
        if(!(totalTime > 0)){
            logger.error("Total time must be greater than 0 but we are getting it as :"+totalTime);
            extentTest.log(LogStatus.FAIL,"Total time must be greater than 0 but we are getting it as :"+totalTime);
            return false;
        }

        if(!loadingSpinner()){
            extentTest.log(LogStatus.FAIL,"loading spinner is present for long time");
            return false;
        }

        driver.executeScript("pp.seek(pp.getDuration()-10);");

        if(!loadingSpinner()){
            extentTest.log(LogStatus.FAIL,"loading spinner is present for long time");
            return false;
        }

        if (!waitOnElement(By.id("seeked_1"),10000)){
            logger.error("Not able to seek the video...");
            extentTest.log(LogStatus.FAIL,"Not able to seek the video...");
            return false;
        }

        String title = driver.executeScript("return pp.getTitle()").toString();
        if (title==null){
            logger.error("Not able to get title of the video");
            extentTest.log(LogStatus.FAIL,"Not able to get title of the video");
            return false;
        }

        String getVolume = driver.executeScript("return pp.getVolume()").toString();
        logger.info("Volume is : "+getVolume);
        if (getVolume==null){
            logger.error("Not able to get volume of the video");
            extentTest.log(LogStatus.FAIL,"Not able to get volume of the video");
            return false;
        }

        String embedCode = driver.executeScript("return pp.getEmbedCode()").toString();
        if (embedCode==null){
            logger.error("Not able to get Embed code");
            extentTest.log(LogStatus.FAIL,"Not able to get Embed code");
            return false;
        }


        ArrayList<String> langList = ((ArrayList<String>)driver
                .executeScript("return pp.getCurrentItemClosedCaptionsLanguages().languages;"));
        for (int i = 0; i < langList.size(); i++) {
            driver.executeScript("pp.setClosedCaptionsLanguage(\"" + langList.get(i) + "\")");
            if (!waitOnElement(By.id("cclanguage_"+langList.get(i)),10000)){
                logger.error("Not able to get "+langList.get(i));
                extentTest.log(LogStatus.FAIL,"Not able to get "+langList.get(i));
                return false;
            }
        }

        return true;
    }

    public boolean validatePlayPauseSeekAPI(){
        validateGetItemAPI();
        return validateDescriptionAPI() && validateEmbedCodeAPI() && validateTitleAPI() && validatePlayAPI() && validatePauseAPI()
                && validateSeekAPI();
    }

    public boolean validateInitialTime(){
        double initialTime = parseDouble(driver.executeScript("return pp.parameters.initialTime").toString());

        double playHeadTime = parseDouble(driver.executeScript("return pp.getPlayheadTime()").toString());

        if(!(playHeadTime>initialTime)){
            extentTest.log(LogStatus.FAIL,"Video playback not started from initial time");
            logger.error("Video playback not started from initial time");
            return false;
        }
        return true;
    }

    public void validateGetItemAPI(){
        description = driver.executeScript(
                "if((typeof pp.getItem().description)==='object'){" +
                "return 'null'" +
                "}else{" +
                "return pp.getItem().description;" +
                "}").toString();
        embedCode = driver.executeScript("return pp.getItem().embed_code").toString();
        title = driver.executeScript("return pp.getItem().title").toString();
    }

    public boolean validateDescriptionAPI(){
        logger.info("************************************** validating description API ******************************************************");
        String expectedDesciption = driver.executeScript("if((typeof pp.getDescription())==='object'){" +
                "return 'null'" +
                "}else{" +
                "return pp.getItem().description;" +
                "}").toString();
        if (!description.equals(expectedDesciption)){
            logger.error("Description is not matching ... Expected : "+description +"\n Actual :"+expectedDesciption);
            extentTest.log(LogStatus.FAIL,"Description is not matching ... Expected : "+description +"\n Actual :"+expectedDesciption);
            return false;
        }
        logger.info("Description is matching ... Expected : "+description +"\n Actual :"+expectedDesciption);
        extentTest.log(LogStatus.PASS,"Description is matching ... Expected : "+description +"\n Actual :"+expectedDesciption);
        return true;
    }

    public boolean validateEmbedCodeAPI(){
        logger.info("******************************************* validating embed code API ********************************************************");
        String expectedEmbedCode = driver.executeScript("return pp.getEmbedCode()").toString();
        if (!embedCode.equals(expectedEmbedCode)){
            logger.error("Embed Code is not matching ... Expected : "+embedCode +"\n Actual :"+expectedEmbedCode);
            extentTest.log(LogStatus.FAIL,"Embed Code is not matching ... Expected : "+embedCode +"\n Actual :"+expectedEmbedCode);
            return false;
        }
        logger.info("Embed Code is matching ... Expected : "+embedCode +"\n Actual :"+expectedEmbedCode);
        extentTest.log(LogStatus.PASS,"Embed Code is matching ... Expected : "+embedCode +"\n Actual :"+expectedEmbedCode);
        return true;
    }

    public boolean validateTitleAPI(){
        logger.info("*********************************** validating title ************************************************************");
        String expectedTitle = driver.executeScript("return pp.getTitle()").toString();
        if (!title.equals(expectedTitle)){
            logger.error("Title is not matching ... Expected : "+title +"\n Actual :"+expectedTitle);
            extentTest.log(LogStatus.FAIL,"Title is not matching ... Expected : "+title +"\n Actual :"+expectedTitle);
            return false;
        }
        logger.info("Title is matching ... Expected : "+title +"\n Actual :"+expectedTitle);
        extentTest.log(LogStatus.PASS,"Title is matching ... Expected : "+title +"\n Actual :"+expectedTitle);
        return true;
    }

    public boolean validatePlayAPI(){
        logger.info("******************************************* validating play API ****************************************************");
        try {
            // Start playback
            driver.executeScript("pp.play()");
        } catch (Exception ex){
            logger.error("getting exception while playing video using pp.play() API");
            return false;
        }

        //Check playing event
        if(!waitOnElement(By.id("playing_1"),20000)){
            logger.error("playing event is not getting triggered");
            extentTest.log(LogStatus.FAIL,"playing event is not getting triggered");
            return false;
        }
        logger.info("playing event gets triggered");
        extentTest.log(LogStatus.PASS,"playing event gets triggered");

        //Check isPlaying api return true or false
        boolean isPlaying = Boolean.parseBoolean(driver.executeScript("return pp.isPlaying()").toString());
        if (!isPlaying){
            logger.error("isPlaying() API returns "+isPlaying+" while video is playing");
            extentTest.log(LogStatus.FAIL,"isPlaying() API returns "+isPlaying+" while video is playing");
            return false;
        }
        logger.info("isPlaying() API returns "+isPlaying+" while video is playing");
        extentTest.log(LogStatus.PASS,"isPlaying() API returns "+isPlaying+" while video is playing");

        // Validate playing State
        String playingState = driver.executeScript("return pp.getState()").toString();
        if (!playingState.equalsIgnoreCase("playing")){
            logger.error("Not getting playing State for pp.getState() API.\n Getting State while video is Playing :"+playingState);
            extentTest.log(LogStatus.FAIL,"Not getting playing State for pp.getState() API.\n Getting State while video is Playing :"+playingState);
            return false;
        }
        logger.info("Getting playing State for pp.getState() API as "+playingState);
        extentTest.log(LogStatus.PASS,"Getting playing State for pp.getState() API as "+playingState);

        new PlayBackFactory(driver,extentTest).getEventValidator().playVideoForSometime(3);

        return true;
    }

    public boolean validatePauseAPI(){
        logger.info("********************************************* validating pause API *****************************************");
        try {
            // Pause video
            driver.executeScript("pp.pause()");
        } catch (Exception ex){
            logger.error("getting exception while pausing the video using pp.pause() API");
            return false;
        }

        //Check playing event
        if(!waitOnElement(By.id("paused_1"),20000)){
            logger.error("pause event is not getting triggered");
            extentTest.log(LogStatus.FAIL,"pause event is not getting triggered");
            return false;
        }
        logger.info("pause event gets triggered");
        extentTest.log(LogStatus.PASS,"pause event gets triggered");

        //Check isPlaying api return true or false
        boolean isPause = Boolean.parseBoolean(driver.executeScript("return pp.isPlaying()").toString());
        if (isPause){
            logger.error("isPlaying() API returns "+isPause+" while video is pause");
            extentTest.log(LogStatus.FAIL,"isPlaying() API returns "+isPause+" while video is pause");
            return false;
        }
        logger.info("isPlaying() API returns "+isPause+" while video is pause");
        extentTest.log(LogStatus.PASS,"isPlaying() API returns "+isPause+" while video is pause");

        // Validate pause State
        String pauseState = driver.executeScript("return pp.getState()").toString();
        if (!pauseState.equalsIgnoreCase("paused")){
            logger.error("Not getting paused State for pp.getState() API.\n Getting State while video is paused :"+pauseState);
            extentTest.log(LogStatus.FAIL,"Not getting paused State for pp.getState() API.\n Getting State while video is paused :"+pauseState);
            return false;
        }
        logger.info("Getting paused State for pp.getState() API as "+pauseState);
        extentTest.log(LogStatus.PASS,"Getting paused State for pp.getState() API as "+pauseState);

        return true;
    }

    public boolean validateSeekAPI(){
        logger.info("*************************************** validating seek API *****************************************************");
        boolean isAdPlaying = Boolean.parseBoolean(driver.executeScript("return pp.isAdPlaying()").toString());
        logger.info("isAdPlaying :"+isAdPlaying);
        if (!isAdPlaying){
            boolean isVideoPlaying = Boolean.parseBoolean(driver.executeScript("return pp.isPlaying()").toString());
            logger.info("isVideoPlaying :"+isVideoPlaying);
            extentTest.log(LogStatus.INFO,"isVideoPlaying :"+isVideoPlaying);
            if (isVideoPlaying) {
                if (!validatePauseAPI()) {
                    logger.error("Not able to pause before seeking the video");
                    extentTest.log(LogStatus.FAIL,"Not able to pause before seeking the video");
                    return false;
                }
                logger.info("video paused before seeking the video");
                extentTest.log(LogStatus.PASS,"video paused before seeking the video");
            }
            int seekTime = Integer.parseInt(driver.executeScript("return (pp.getDuration()-10).toFixed()").toString());
            logger.info("seekTime :"+seekTime);
            extentTest.log(LogStatus.INFO,"seekTime :"+seekTime);
            try{
                driver.executeScript("pp.seek("+seekTime+")");
                logger.info("No issue while seeking video using pp.seek() API");
            }catch (Exception ex){
                logger.error("getting exception while seeking the video");
                extentTest.log(LogStatus.FAIL,"getting exception while seeking the video");
                return false;
            }
            if (!waitOnElement(By.id("seeked_1"),20000)){
                logger.error("Seek event is not triggering");
                extentTest.log(LogStatus.FAIL,"Seek event is not triggering");
                return false;
            }
            logger.info("Seek event is triggered");
            extentTest.log(LogStatus.PASS,"Seek event is triggered");

            int currentPlayHeadTime = Integer.parseInt(driver.executeScript("return pp.getPlayheadTime().toFixed()").toString());
            logger.info("currentPlayHeadTime = "+currentPlayHeadTime);
            extentTest.log(LogStatus.INFO,"currentPlayHeadTime = "+currentPlayHeadTime);
            if (currentPlayHeadTime!=seekTime){
                logger.error("current playhead time and seek Time is not matching after seeking the video..");
                extentTest.log(LogStatus.FAIL,"current playhead time and seek Time is not matching after seeking the video..");
                return false;
            }
            logger.info("current playhead time and seek Time is matching after seeking the video..");
            extentTest.log(LogStatus.PASS,"current playhead time and seek Time is matching after seeking the video..");
        }
        return true;
    }

    public boolean validateVolumeAPI(){
        logger.info("**************************** Validating Volume API *******************************************************");
        // Set volume to mute
        driver.executeScript("pp.setVolume(0)");
        int muteVol = Integer.parseInt(driver.executeScript("return pp.getVolume()").toString());
        if (muteVol != 0){
            logger.error("volume is not getting mute");
            extentTest.log(LogStatus.FAIL,"volume is not getting mute");
            return false;
        }
        if (!waitOnElement(By.id("volumeChanged_1"),10000)
                || !waitOnElement(By.id("changeVolume_1"),10000)){
            logger.error("Either of the following events didn't gets triggered when volume is set to MUTE: \n 1--> volumeChanged \n 2--> changeVolume");
            extentTest.log(LogStatus.FAIL,"Either of the following events didn't gets triggered when volume is set to MUTE: \n 1--> volumeChanged \n 2--> changeVolume");
            return false;
        }
        logger.info("verified mute volume");
        extentTest.log(LogStatus.PASS,"verified mute volume");

        // Set volume to 0.4
        driver.executeScript("pp.setVolume(0.4)");
        float intMidVol = Float.parseFloat(driver.executeScript("return pp.getVolume()").toString());
        if (intMidVol != 0.4f){
            logger.error("volume is not getting set to 0.4");
            extentTest.log(LogStatus.FAIL,"volume is not getting set to 0.4");
            return false;
        }
        if (!waitOnElement(By.id("volumeChanged_2"),10000)
                || !waitOnElement(By.id("changeVolume_2"),10000)){
            logger.error("Either of the following events didn't gets triggered when volume is set to intermediate volume level: \n 1--> volumeChanged \n 2--> changeVolume");
            extentTest.log(LogStatus.FAIL,"Either of the following events didn't gets triggered when volume is set to intermediate volume level: \n 1--> volumeChanged \n 2--> changeVolume");
            return false;
        }
        logger.info("verified intermediate volume");
        extentTest.log(LogStatus.PASS,"verified intermediate volume");

        //Set volume to 1
        driver.executeScript("pp.setVolume(1)");
        int highVol = Integer.parseInt(driver.executeScript("return pp.getVolume()").toString());
        if (highVol != 1){
            logger.error("volume is not set to high i.e 1");
            return false;
        }
        if (!waitOnElement(By.id("volumeChanged_3"),10000)
                || !waitOnElement(By.id("changeVolume_3"),10000)){
            logger.error("Either of the following events didn't gets triggered when volume is set to HIGH level: \n 1--> volumeChanged \n 2--> changeVolume");
            extentTest.log(LogStatus.FAIL,"Either of the following events didn't gets triggered when volume is set to HIGH level: \n 1--> volumeChanged \n 2--> changeVolume");
            return false;
        }
        logger.info("verified high volume");
        extentTest.log(LogStatus.PASS,"verified high volume");
        return true;
    }

    public boolean validateDestroyAPI(){

        logger.info("******************************** validating Destroy API **********************************************");
        boolean isPlaying = Boolean.parseBoolean(driver.executeScript("return pp.isPlaying").toString());

        if (!isPlaying){
            driver.executeScript("pp.play()");
            loadingSpinner();
        }

        try{
            driver.executeScript("pp.destroy()");
        } catch (Exception ex){
            logger.error(ex.getMessage());
            extentTest.log(LogStatus.FAIL,ex.getMessage());
            return false;
        }

        //Validate Destroy event ...
        if (!waitOnElement(By.id("destroy_1"),20000)
                || !waitOnElement(By.id("videoElementDisposed_1"),20000)
                || !waitOnElement(By.id("videoElementLostFocus_1"),20000)){
            logger.error("either of the following event not triggered : \n 1--> Destroy \n 2--> videoElementDisposed_1 \n 3--> videoElementLostFocus");
            extentTest.log(LogStatus.FAIL,"either of the following event not triggered : \n 1--> Destroy \n 2--> videoElementDisposed_1 \n 3--> videoElementLostFocus");
            return false;
        }
        logger.info("Following events gets triggered : \n 1--> Destroy \n 2--> videoElementDisposed_1 \n 3--> videoElementLostFocus");
        extentTest.log(LogStatus.PASS,"Following event gets triggered : \n 1--> Destroy \n 2--> videoElementDisposed_1 \n 3--> videoElementLostFocus");

        // Validate destroy State
        String destroyState = driver.executeScript("return pp.getState()").toString();
        if (!destroyState.equalsIgnoreCase("destroyed")){
            logger.error("Not getting destroyed State for pp.getState() API.\n Getting State while video is paused :"+destroyState);
            extentTest.log(LogStatus.FAIL,"Not getting destroyed State for pp.getState() API.\n Getting State while video is paused :"+destroyState);
            return false;
        }
        logger.info("Getting destroyed State for pp.getState() API as "+destroyState);
        extentTest.log(LogStatus.PASS,"Getting destroyed State for pp.getState() API as "+destroyState);
        return true;
    }

    public boolean validateFullScreenAPI(){
        logger.info("***************************** Validate FullScreen API ****************************************************");
        boolean isNormalScreen = false;
        boolean isFullScreen = false;
        isNormalScreen = Boolean.parseBoolean(driver.executeScript("return pp.isFullscreen()").toString());
        logger.info("isNormalScreen : "+isNormalScreen);
        extentTest.log(LogStatus.INFO,"isNormalScreen : "+isNormalScreen);
        if (!isNormalScreen){
            if (!clickOnIndependentElement("FULLSCREEN_BTN_1")){
                logger.error("Not able to click on fullscreen button");
                extentTest.log(LogStatus.FAIL,"Not able to click on fullscreen button");
                return false;
            }
            isFullScreen = Boolean.parseBoolean(driver.executeScript("return pp.isFullscreen()").toString());
            logger.info("isFullScreen :"+isFullScreen);
            extentTest.log(LogStatus.INFO,"isFullScreen :"+isFullScreen);
            if (!isFullScreen){
                logger.error("pp.isFullscreen() API does not return true when video is playing in fullscreen mode");
                extentTest.log(LogStatus.FAIL,"pp.isFullscreen() API does not return true when video is playing in fullscreen");
                return false;
            }
            logger.info("pp.isFullscreen() API does returns true when video is playing in fullscreen mode");
            extentTest.log(LogStatus.PASS,"pp.isFullscreen() API does returns true when video is playing in fullscreen");
            if (!clickOnIndependentElement("NORMAL_SCREEN")){
                logger.error("Not able to click on normalscreen button");
                extentTest.log(LogStatus.FAIL,"Not able to click on normalscreen button");
                return false;
            }
            isNormalScreen = Boolean.parseBoolean(driver.executeScript("return pp.isFullscreen()").toString());
            if (isNormalScreen){
                logger.info("pp.isFullscreen() API does not return false when video is playing in normalscreen mode");
                extentTest.log(LogStatus.PASS,"pp.isFullscreen() API does not return false when video is playing in normalscreen");
                return false;
            }
            logger.info("pp.isFullscreen() API does returns false when video is playing in normalscreen mode");
            extentTest.log(LogStatus.PASS,"pp.isFullscreen() API does returns false when video is playing in normalscreen");
        }
        return true;
    }

    public boolean validateDurationForLive(){
        logger.info("********************** Validating Duration for Live VIDEO ************************************");

        /***
         * Here expected duration for Live video is Infinity
         */
        String liveDuration = null;
        liveDuration = (driver.executeScript("return pp.getDuration().toString()")).toString();
        if (!liveDuration.equalsIgnoreCase("infinity")){
            logger.error("Total duration for Live video is not Infinity");
            extentTest.log(LogStatus.FAIL,"Total duration for Live video is not Infinity");
            return false;
        }
        logger.info("Total duration for Live video is Infinity");
        extentTest.log(LogStatus.PASS,"Total duration for Live video is Infinity");
        return true;
    }

    public boolean validateDurationAtEndScreen(){
        logger.info("***************************** Validating Duration at end Screen **************************************");
        boolean isPlaying = false;
        String duration = null;
        String playHeadTime = null;
        duration = driver.executeScript("return pp.getDuration().toFixed").toString();
        isPlaying = Boolean.parseBoolean((driver.executeScript("return pp.isPlaying")).toString());
        if (!isPlaying){
            if (!validatePlayAPI()){
                return false;
            }
        }
        driver.executeScript("pp.seek(pp.getDuration())");
        playHeadTime = driver.executeScript("return pp.getPlayheadTime().toFixed").toString();
        if (duration != playHeadTime){
            logger.error("playhead time and total duration is not matching at End Screen");
            extentTest.log(LogStatus.FAIL,"playhead time and total duration is not matching at End Screen");
            return false;
        }
        logger.info("playhead time and total duration is matching at End Screen");
        extentTest.log(LogStatus.PASS,"playhead time and total duration is matching at End Screen");
        return true;
    }

    public boolean validateCloseCaptionAPI(){
        logger.info("***************************** Validating Close Caption CC API *********************************************");

        boolean isPlaying = Boolean.parseBoolean(driver.executeScript("return pp.isPlaying").toString());
        if (!isPlaying){
            driver.executeScript("pp.play()");
            loadingSpinner();
        }
        ArrayList<String> langList = ((ArrayList<String>)driver
                .executeScript("return pp.getCurrentItemClosedCaptionsLanguages().languages;"));
        for (int i = 0; i < langList.size(); i++) {
            driver.executeScript("pp.setClosedCaptionsLanguage(\"" + langList.get(i) + "\")");
            if (!waitOnElement(By.id("cclanguage_"+langList.get(i)),10000)){
                logger.error("Not able to get "+langList.get(i));
                extentTest.log(LogStatus.FAIL,"Not able to get "+langList.get(i));
                return false;
            }
            logger.info("close caption is set to language : "+langList.get(i));
            extentTest.log(LogStatus.PASS,"close caption is set to language : "+langList.get(i));
        }
        return true;
    }

    public boolean valdidateAPIForPreroll() {
        logger.info("*************************** Validate All API's for Preroll Ad *********************************");
        validateGetItemAPI();
        if (!(validateDescriptionAPI() && validateEmbedCodeAPI() && validateTitleAPI())){
            return false;
        }

        driver.executeScript("pp.play()");
        if (!loadingSpinner()){
            logger.error("Loading spinner is present for long time after playing the video");
            extentTest.log(LogStatus.FAIL,"Loading spinner is present for long time after playing the video");
            return false;
        }

        if (!waitOnElement(By.id("PreRoll_willPlaySingleAd_1"),10000)){
            logger.error("preroll ad is not playing or present for this asset");
            extentTest.log(LogStatus.FAIL,"preroll ad is not playing or present for this asset");
            return false;
        }

        boolean isAdPlaying = (Boolean) executeJsScript("pp.isAdPlaying()","boolean");
        if (!isAdPlaying){
            logger.error("isAdPlaying() API does not return true when ad is playing");
            extentTest.log(LogStatus.FAIL,"isAdPlaying() API does not return true when ad is playing");
        }
        logger.info("isAdPlaying() API does return true when ad is playing");
        extentTest.log(LogStatus.PASS,"isAdPlaying() API does return true when ad is playing");

        if (isAdPlaying){
            driver.executeScript("pp.pause()");
            if (!waitOnElement(By.id("videoPausedAds_1"),10000)){
                logger.error("Does not found videoPausedAds event after video gets paused");
                extentTest.log(LogStatus.FAIL,"Does not found videoPausedAds event after video gets paused");
                return false;
            }
            logger.info("Found videoPausedAds event after video gets paused");
            extentTest.log(LogStatus.FAIL,"Found videoPausedAds event after video gets paused");

            driver.executeScript("pp.skipAd()");
            if (!waitOnElement(By.id("skipAd_1"),10000)){
                logger.error("Does not found skipAd event after ad gets skipped");
                extentTest.log(LogStatus.FAIL,"Does not found skipAd event after ad gets skipped");
                return false;
            }
            logger.info("Found skipAd event after ad gets skipped");
            extentTest.log(LogStatus.PASS,"Found skipAd event after ad gets skipped");

            if (!loadingSpinner()){
                logger.error("Loading spinner is present for long time after skipping the ad");
                extentTest.log(LogStatus.FAIL,"Loading spinner is present for long time after skipping the ad");
                return false;
            }

            if (!waitOnElement(By.id("playing_1"),10000)){
                logger.error("Does not found Playing event after ad gets skipped");
                extentTest.log(LogStatus.FAIL,"Does not found Playing event after ad gets skipped");
                return false;
            }
            logger.info("Found Playing event after ad gets skipped");
            extentTest.log(LogStatus.PASS,"Found Playing event after ad gets skipped");

            boolean isPlaying = (Boolean) executeJsScript("pp.isPlaying()","boolean");
            if (!isPlaying){
                logger.error("isPlaying() API returns "+isAdPlaying+" while video is playing");
                extentTest.log(LogStatus.FAIL,"isPlaying() API returns "+isAdPlaying+" while video is playing");
                return false;
            }
            logger.info("isPlaying() API returns "+isAdPlaying+" while video is playing");
            extentTest.log(LogStatus.PASS,"isPlaying() API returns "+isAdPlaying+" while video is playing");
        }
        return true;
    }

    public boolean validateAPIForMidroll(){
        logger.info("*************************** Validate All API's for Midroll Ad *********************************");
        validateGetItemAPI();
        if (!(validateDescriptionAPI() && validateEmbedCodeAPI() && validateTitleAPI())){
            return false;
        }

        driver.executeScript("pp.play()");
        if (!loadingSpinner()){
            logger.error("Loading spinner is present for long time after playing the video");
            extentTest.log(LogStatus.FAIL,"Loading spinner is present for long time after playing the video");
            return false;
        }

        if (!waitOnElement(By.id("MidRoll_willPlaySingleAd_1"),30000)){
            logger.error("Midroll ad is not playing or present for this asset");
            extentTest.log(LogStatus.FAIL,"Midroll ad is not playing or present for this asset");
            return false;
        }

        boolean isAdPlaying = (Boolean) executeJsScript("pp.isAdPlaying()","boolean");
        if (!isAdPlaying){
            logger.error("isAdPlaying() API does not return true when ad is playing");
            extentTest.log(LogStatus.FAIL,"isAdPlaying() API does not return true when ad is playing");
        }
        logger.info("isAdPlaying() API does return true when ad is playing");
        extentTest.log(LogStatus.PASS,"isAdPlaying() API does return true when ad is playing");

        if (isAdPlaying){
            driver.executeScript("pp.pause()");
            if (!waitOnElement(By.id("videoPausedAds_1"),10000)){
                logger.error("Does not found videoPausedAds event after video gets paused");
                extentTest.log(LogStatus.FAIL,"Does not found videoPausedAds event after video gets paused");
                return false;
            }
            logger.info("Found videoPausedAds event after video gets paused");
            extentTest.log(LogStatus.FAIL,"Found videoPausedAds event after video gets paused");

            driver.executeScript("pp.skipAd()");
            if (!waitOnElement(By.id("skipAd_1"),10000)){
                logger.error("Does not found skipAd event after ad gets skipped");
                extentTest.log(LogStatus.FAIL,"Does not found skipAd event after ad gets skipped");
                return false;
            }
            logger.info("Found skipAd event after ad gets skipped");
            extentTest.log(LogStatus.PASS,"Found skipAd event after ad gets skipped");

            if (!loadingSpinner()){
                logger.error("Loading spinner is present for long time after skipping the ad");
                extentTest.log(LogStatus.FAIL,"Loading spinner is present for long time after skipping the ad");
                return false;
            }

            if (!waitOnElement(By.id("playing_2"),10000)){
                logger.error("Does not found Playing event after ad gets skipped");
                extentTest.log(LogStatus.FAIL,"Does not found Playing event after ad gets skipped");
                return false;
            }
            logger.info("Found Playing event after ad gets skipped");
            extentTest.log(LogStatus.PASS,"Found Playing event after ad gets skipped");

            boolean isPlaying = (Boolean) executeJsScript("pp.isPlaying()","boolean");
            if (!isPlaying){
                logger.error("isPlaying() API returns "+isAdPlaying+" while video is playing");
                extentTest.log(LogStatus.FAIL,"isPlaying() API returns "+isAdPlaying+" while video is playing");
                return false;
            }
            logger.info("isPlaying() API returns "+isAdPlaying+" while video is playing");
            extentTest.log(LogStatus.PASS,"isPlaying() API returns "+isAdPlaying+" while video is playing");
        }
        return true;
    }

    public Object executeJsScript(String command, String returnType){

        switch (returnType){
            case "boolean":
                return Boolean.parseBoolean(driver.executeScript("return "+command+"").toString());
            case "string":
                driver.executeScript("return "+command+"").toString();
            case "int":
                return Boolean.parseBoolean(driver.executeScript("return "+command+"").toString());
        }
        return null;
    }

    public boolean validateAPIForPostroll() {
        logger.info("*************************** Validate All API's for Postroll Ad *********************************");
        validateGetItemAPI();
        if (!(validateDescriptionAPI() && validateEmbedCodeAPI() && validateTitleAPI())){
            return false;
        }

        driver.executeScript("pp.play()");
        if (!loadingSpinner()){
            logger.error("Loading spinner is present for long time after playing the video");
            extentTest.log(LogStatus.FAIL,"Loading spinner is present for long time after playing the video");
            return false;
        }

        new PlayBackFactory(driver,extentTest).getEventValidator().playVideoForSometime(4);
        driver.executeScript("pp.seek(pp.getDuration()-10)");

        if (!loadingSpinner()){
            return false;
        }

        if (!waitOnElement(By.id("PostRoll_willPlaySingleAd_1"),30000)){
            logger.error("Postroll ad is not playing or present for this asset");
            extentTest.log(LogStatus.FAIL,"Postroll ad is not playing or present for this asset");
            return false;
        }

        boolean isAdPlaying = (Boolean) executeJsScript("pp.isAdPlaying()","boolean");
        if (!isAdPlaying){
            logger.error("isAdPlaying() API does not return true when ad is playing");
            extentTest.log(LogStatus.FAIL,"isAdPlaying() API does not return true when ad is playing");
        }
        logger.info("isAdPlaying() API does return true when ad is playing");
        extentTest.log(LogStatus.PASS,"isAdPlaying() API does return true when ad is playing");

        if (isAdPlaying){
            driver.executeScript("pp.pause()");
            if (!waitOnElement(By.id("videoPausedAds_1"),10000)){
                logger.error("Does not found videoPausedAds event after video gets paused");
                extentTest.log(LogStatus.FAIL,"Does not found videoPausedAds event after video gets paused");
                return false;
            }
            logger.info("Found videoPausedAds event after video gets paused");
            extentTest.log(LogStatus.FAIL,"Found videoPausedAds event after video gets paused");

            driver.executeScript("pp.skipAd()");
            if (!waitOnElement(By.id("skipAd_1"),10000)){
                logger.error("Does not found skipAd event after ad gets skipped");
                extentTest.log(LogStatus.FAIL,"Does not found skipAd event after ad gets skipped");
                return false;
            }
            logger.info("Found skipAd event after ad gets skipped");
            extentTest.log(LogStatus.PASS,"Found skipAd event after ad gets skipped");

            if (!loadingSpinner()){
                logger.error("Loading spinner is present for long time after skipping the ad");
                extentTest.log(LogStatus.FAIL,"Loading spinner is present for long time after skipping the ad");
                return false;
            }

            if (!waitOnElement(By.id("played_1"),10000)){
                logger.error("Does not found Played event after ad gets skipped");
                extentTest.log(LogStatus.FAIL,"Does not found Played event after ad gets skipped");
                return false;
            }
            logger.info("Found Played event after ad gets skipped");
            extentTest.log(LogStatus.PASS,"Found Played event after ad gets skipped");
        }
        return true;
    }
}
