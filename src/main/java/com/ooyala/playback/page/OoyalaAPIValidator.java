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

    public boolean validateApi(){
        validateGetItem();
        return validateDescription() && validateEmbedCode() && validateTitle() && validatePlay() && validateVolume() && validatePause()
                && validateSeek() && validateDestroy();
    }

    public boolean validateInitailTime(){
        double initialTime = parseDouble(driver.executeScript("return pp.parameters.initialTime").toString());

        double playHeadTime = parseDouble(driver.executeScript("return pp.getPlayheadTime()").toString());

        if(!(playHeadTime>initialTime)){
            extentTest.log(LogStatus.FAIL,"Video playback not started from initial time");
            logger.error("Video playback not started from initial time");
            return false;
        }
        return true;
    }

    public void validateGetItem(){
        description = driver.executeScript("return pp.getItem().description").toString();
        embedCode = driver.executeScript("return pp.getItem().embed_code").toString();
        title = driver.executeScript("return pp.getItem().title").toString();
    }

    public boolean validateDescription(){
        logger.info("************************************** validating description ******************************************************");
        String expectedDesciption = driver.executeScript("return pp.getDescription()").toString();
        if (!description.equals(expectedDesciption)){
            logger.error("Description is not matching ... Expected : "+description +"\n Actual :"+expectedDesciption);
            extentTest.log(LogStatus.FAIL,"Description is not matching ... Expected : "+description +"\n Actual :"+expectedDesciption);
            return false;
        }
        logger.info("Description is matching ... Expected : "+description +"\n Actual :"+expectedDesciption);
        extentTest.log(LogStatus.PASS,"Description is matching ... Expected : "+description +"\n Actual :"+expectedDesciption);
        return true;
    }

    public boolean validateEmbedCode(){
        logger.info("******************************************* validating embed code ********************************************************");
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

    public boolean validateTitle(){
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

    public boolean validatePlay(){
        logger.info("******************************************* validating play ****************************************************");
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

    public boolean validatePause(){
        logger.info("********************************************* validating pause *****************************************");
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

    public boolean validateSeek(){
        logger.info("*************************************** validating seek *****************************************************");
        boolean isAdPlaying = Boolean.parseBoolean(driver.executeScript("return pp.isAdPlaying()").toString());
        logger.info("isAdPlaying :"+isAdPlaying);
        if (!isAdPlaying){
            boolean isVideoPlaying = Boolean.parseBoolean(driver.executeScript("return pp.isPlaying()").toString());
            logger.info("isVideoPlaying :"+isVideoPlaying);
            if (isVideoPlaying) {
                if (!validatePause()) {
                    return false;
                }
            }
            int seekTime = Integer.parseInt(driver.executeScript("return (pp.getDuration()-10).toFixed()").toString());
            logger.info("seekTime :"+seekTime);
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
                return false;
            }
            logger.info("Seek event is triggered");
            int currentPlayHeadTime = Integer.parseInt(driver.executeScript("return pp.getPlayheadTime().toFixed()").toString());
            logger.info("currentPlayHeadTime = "+currentPlayHeadTime);
            if (currentPlayHeadTime!=seekTime){
                logger.error("current playhead time and seek Time is not matching after seeking the video..");
                return false;
            }
            logger.info("current playhead time and seek Time is matching after seeking the video..");
        }
        return true;
    }

    public boolean validateVolume(){
        logger.info("**************************** Checking Volume *******************************************************");
        // Set volume to mute
        driver.executeScript("pp.setVolume(0)");
        int muteVol = Integer.parseInt(driver.executeScript("return pp.getVolume()").toString());
        if (muteVol != 0){
            logger.error("volume is not getting mute");
            return false;
        }
        if (!waitOnElement(By.id("volumeChanged_1"),10000) && !waitOnElement(By.id("changeVolume_1"),10000)){
            return false;
        }
        logger.info("verified mute volume");

        // Set volume to 0.4
        driver.executeScript("pp.setVolume(0.4)");
        float intMidVol = Float.parseFloat(driver.executeScript("return pp.getVolume()").toString());
        if (intMidVol != 0.4f){
            logger.error("volume is not getting set to 0.4");
            return false;
        }
        if (!waitOnElement(By.id("volumeChanged_2"),10000) && !waitOnElement(By.id("changeVolume_2"),10000)){
            return false;
        }
        logger.info("verified intermediate volume");

        //Set volume to 1
        driver.executeScript("pp.setVolume(1)");
        int highVol = Integer.parseInt(driver.executeScript("return pp.getVolume()").toString());
        if (highVol != 1){
            logger.error("volume is not set to high i.e 1");
            return false;
        }
        if (!waitOnElement(By.id("volumeChanged_3"),10000) && !waitOnElement(By.id("changeVolume_3"),10000)){
            return false;
        }
        logger.info("verified high volume");
        return true;
    }

    public boolean validateDestroy(){

        logger.info("******************************** validating Destroy API **********************************************");

        try{
            driver.executeScript("pp.destroy()");
        } catch (Exception ex){
            return false;
        }

        //Validate Destroy event ...
        if (!waitOnElement(By.id("destroy_1"),20000)
                && !waitOnElement(By.id("videoElementDisposed_1"),20000)
                && !waitOnElement(By.id("videoElementLostFocus_1"),20000)){
            return false;
        }
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

}
