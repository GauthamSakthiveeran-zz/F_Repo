package com.ooyala.playback.page;

import static java.lang.Character.CONTROL;
import static java.net.URLDecoder.decode;
import static org.openqa.selenium.Keys.DELETE;
import static org.testng.Assert.assertEquals;

import com.ooyala.playback.url.UrlObject;
import org.json.simple.parser.JSONParser;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.page.action.PlayerAPIAction;

import java.util.*;

/**
 *
 * @author dmanohar
 *
 */
public class EncodingValidator extends PlayBackPage implements PlaybackValidator {

    private static Logger logger = Logger.getLogger(EncodingValidator.class);

    public EncodingValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("page");
    }

    String supportedMuxFormat = null;
    String videoPlugin = null;
    String defaultEncodingPriority = null;
    boolean isEncodingPriorityNotSet = false;
    String browser = getBrowser();
    WebElement streamElement = null;
    String [] supportedMuxFormats = null;
    String[] videoPlugins = null;
    UrlObject url = null;
    List<String> supportedMuxFormatList;
    List<String> dashHls ;
    List<String> dashHlsHdsMp4 ;
    List<String> hlsMp4 ;
    List<String> dashMp4 ;
    Map<String,String> encodedFormat = new HashMap<>();

    public EncodingValidator getStreamType(UrlObject url){
        supportedMuxFormat = url.getSupportedMuxFormat();
        videoPlugin = url.getVideoPlugins();
        supportedMuxFormats = null;
        videoPlugins = null;
        this.url = url;
        return this;
    }

    public boolean validate(String element, int timeout) throws Exception {

        String result = decode(driver.getCurrentUrl(), "UTF-8");
        if (result == null)
            return false;

        String[] options = result.split("options=");
        if (options == null || options.length < 2)
            return false;

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(options[1]);
        Object expectedEncodings = "";
        if (obj.containsKey("encodingPriority")) {
            Object actualEncodings = obj.get("encodingPriority");
            logger.info("\nActual encodingPriority :\n" + actualEncodings);
            expectedEncodings = new PlayBackFactory(driver, extentTest).getPlayerAPIAction().getEncodingPriority();
            logger.info("\nExpected encodingPriority :\n" + expectedEncodings);
            assertEquals(actualEncodings, expectedEncodings, "Encoding Priorities are as expected");
            if (!actualEncodings.equals(expectedEncodings))
                return false;

            StreamValidator streams = new PlayBackFactory(driver, extentTest).getStreamTypeValidator();

            org.json.simple.JSONArray json = (org.json.simple.JSONArray) obj.get("encodingPriority");

            for (int i = 0; i < json.size(); i++) {
                String encoding = json.get(i).toString();
                if (encoding.contains("hls")) {
                    return streams.setStreamType("m3u8").validate("videoPlayingurl", 6000);
                }
                if (encoding.contains("dash")) {
                    return streams.setStreamType("mpd").validate("videoPlayingurl", 6000);
                }
                if (encoding.contains("mp4")) {
                    return streams.setStreamType("mp4").validate("videoPlayingurl", 6000);
                }
                if (encoding.contains("hds")) {
                    return streams.setStreamType("f4m").validate("videoPlayingurl", 6000);
                }
            }

        }
        return true;

    }

    public String getNewUrl(String parameter, String browser) {
        try {
            clickOnIndependentElement("OPTIONAL");
            waitOnElement("PLAYER_PARAMETER_INPUT", 20000);

            if (browser.equalsIgnoreCase("internet explorer")) {
                WebElement playerParameter = getWebElement("PLAYER_PARAMETER_INPUT");
                playerParameter.sendKeys(CONTROL + "a");
                playerParameter.sendKeys(DELETE);
            } else
                clearTextFromElement("PLAYER_PARAMETER_INPUT");

            writeTextIntoTextBox("PLAYER_PARAMETER_INPUT", parameter);
            clickOnIndependentElement("TEST_VIDEO");
            waitForPage();
        }catch(Exception ex){
            logger.error(ex);
            throw new WebDriverException();
        }

        return driver.getCurrentUrl();
    }

    public boolean validateVCEventsFromConsole(){
        if (!getLogsFromConsole().get(0).isEmpty() && getLogsFromConsole().get(0) != null
                && !getLogsFromConsole().get(1).isEmpty() && getLogsFromConsole().get(1) != null){
            logger.info("console logs are : \n"+getLogsFromConsole());
            return true;
        }

        return false;
    }

    public boolean verifyEncodingPriority(UrlObject url){

        supportedMuxFormatList = null;
        dashHls = new ArrayList<>(Arrays.asList("dash","hls"));
        dashHlsHdsMp4 = new ArrayList<>(Arrays.asList("dash","hls","hds","mp4"));
        hlsMp4 = new ArrayList<>(Arrays.asList("hls","mp4"));
        dashMp4 = new ArrayList<>(Arrays.asList("dash","mp4"));
        encodedFormat.put("hls","m3u8");
        encodedFormat.put("dash","mpd");
        encodedFormat.put("hds","f4m");
        encodedFormat.put("mp4","mp4");

        if(!waitOnElement(By.id("videoPlayingurl"),30000)){
            logger.error("streamElement is not embedded in DOM Something is wrong...");
            extentTest.log(LogStatus.FAIL,"streamElement is not embedded in DOM Something is wrong...");
            return false;
        }

        streamElement = driver.findElement(By.id("videoPlayingurl"));
        logger.info("Streaming url : "+streamElement.getText());

        if (supportedMuxFormat.contains(",")){
            supportedMuxFormats = supportedMuxFormat.split(",");
            supportedMuxFormatList = new ArrayList<>(Arrays.asList(supportedMuxFormats));
        }

        if (videoPlugin.contains(",")){
            videoPlugins = videoPlugin.split(",");
        }
        
        PlayerAPIAction playerAPIAction = new PlayBackFactory(driver, extentTest).getPlayerAPIAction();

		isEncodingPriorityNotSet = playerAPIAction.getEncodingPriority() == null ? true
				: playerAPIAction.getEncodingPriority().toString().equals("undefined");

        /**
         * Verify default encoding priority when priority is not passed from playerParameter
         */
        if (isEncodingPriorityNotSet){
            if(!checkStreamTypeForDefaultEncodingPriority()){
                return false;
            }
        }

        if (!isEncodingPriorityNotSet){
            String encodingPrioritySet = playerAPIAction.getEncodingPriority(0);

             //verify default encoding priority when priority is passed from playerParameter
            if (supportedMuxFormats == null){
                logger.info("Checking default encoding priority when encoding priority is set through player parameters");
                if (!checkStreamTypeForDefaultEncodingPriority()){
                    return false;
                }
            }

            if (supportedMuxFormats != null && videoPlugins == null){
                if (equalLists(dashHls,supportedMuxFormatList)){
                    return dashHlsPriority(encodingPrioritySet);
                }
                if (equalLists(hlsMp4,supportedMuxFormatList)){
                    return hlsMp4Priority(encodingPrioritySet);
                }
                if (equalLists(dashHlsHdsMp4,supportedMuxFormatList)){
                    return dashHlsHdsMp4Priority(encodingPrioritySet);
                }
            }
        }
        return true;
    }

    public boolean checkStreamTypeForDefaultEncodingPriority() {
        if (supportedMuxFormats == null && videoPlugins == null) {
            logger.info("*************************************************************************************");
            logger.info("Checking video streaming type with default encoding priority");
            if (supportedMuxFormat.equals("hls")) {
                if (!verifyHLS()){
                    return false;
                }
            }

            if (supportedMuxFormat.equals("dash")) {
                if (!verifyDASH()){
                    return false;
                }
            }

            if (supportedMuxFormat.equals("hds")) {
                if (!verifyHDS()){
                    return false;
                }
            }
            logger.info("*************************************************************************************");
        }

        /***
         * Checking default encoding priority for video having single video plugin and more that one supported muxing format.
         */
        if (supportedMuxFormats != null && videoPlugins == null){
            // Checking default streaming type for bitmovin when encoding priority is not set from player parameter..
            if (supportedMuxFormats != null && url.getVideoPlugins().toLowerCase().equals("bitmovin")){
                if (!verifyHLS()){
                    return false;
                }
            }

            //TODO test will get failed if videoWillPlay does not contain url having mp4 as a part e.g ["dash","mp4"]
            // Checking default streaming type for main when encoding priority is not set from player parameter..
            if (supportedMuxFormats != null && url.getVideoPlugins().toLowerCase().equals("main")){
                logger.info("************************** Checking default encoding priority for "+browser+" browser having supported muxing format as ["+supportedMuxFormat+"] for video Plugin MAIN ***********************************");
                if (browser.equals("safari")){
                    if (equalLists(supportedMuxFormatList,dashMp4)){
                        if (!verifyMP4()){
                            return false;
                        }
                    }else {
                        if(!verifyHLS()) {
                            return false;
                        }
                    }
                }else {
                    if (!verifyMP4()){
                        return false;
                    }
                }
            }

            // Checking default streaming type for main when encoding priority is not set from player parameter..
            if (getPlatform().toLowerCase().equals("android")
                    && url.getVideoPlugins().toLowerCase().equals("bitmovin")
                    && supportedMuxFormats != null){
                if (!verifyDASH()){
                    return false;
                }
            }
        }

        /***
         * Checking default encoding priority when there are more that one video plugin and muxing fromat supported ....
         */
        if (supportedMuxFormats != null && videoPlugins != null){
            logger.info("************************** Checking default encoding priority for "+browser+" browser having supported muxing format as ["+supportedMuxFormat+"] for video Plugins "+videoPlugins+"**********************************");
            if (equalLists(supportedMuxFormatList,hlsMp4)){
                if (!verifyHLS()){
                    return false;
                }
            }

            if (equalLists(supportedMuxFormatList,dashMp4)){
                if (getBrowser().equalsIgnoreCase("safari")){
                    if (!verifyMP4()){
                        return false;
                    }
                } else {
                    if (!verifyDASH()){
                        return false;
                    }
                }
            }

            if (equalLists(supportedMuxFormatList,dashHlsHdsMp4)) {
                if (url.getVideoPlugins().toLowerCase().contains("bitmovin")) {
                    if (!verifyHLS()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean dashHlsPriority(String encodingPrioritySet){
        if (url.getVideoPlugins().toLowerCase().equals("bitmovin")){
            if (encodingPrioritySet.equals("hls")){
                if (!verifyHLS()){
                    return false;
                }
            }

            if (encodingPrioritySet.equals("dash")){
                if (getBrowser().equalsIgnoreCase("safari")){
                    if (!verifyHLS()){
                        return false;
                    }
                }else{
                    if (!verifyDASH()){
                        return false;
                    }
                }
            }

            /**
             * hds encoding priority is not supported for video having muxing format as hls+dash
             * therefore if hds encoding priority is set through player parameter then video should get served from second mentioned
             * encoding priority in player parameter.
             * e.g if encoding priority is set as ["hds","hls"] then video should get served via hls and similarly for dash.
             */
            if (encodingPrioritySet.equals("hds")){
                logger.info("checking encoding priority for video ");
                String secondEncoPriority = new PlayBackFactory(driver, extentTest).getPlayerAPIAction().getEncodingPriority(1);
                if (!streamElement.getText().contains("f4m")){
                    if (secondEncoPriority.equals("dash")){
                        if (!verifyDASH()){
                            return false;
                        }
                    }
                    if (secondEncoPriority.equals("hls")){
                        if (!verifyHLS()){
                            return false;
                        }
                    }
                }
            }
        }

        if (url.getVideoPlugins().toLowerCase().equals("osmf")){
            logger.info("OSMF only supports for hds so please remove the video plugin as OSMF and try using BITMOVIN or MAIN");
            extentTest.log(LogStatus.WARNING,"OSMF only supports for hds so please remove the video plugin as OSMF and try using BITMOVIN or MAIN");
            return false;
        }

        if (url.getVideoPlugins().toLowerCase().equals("main")){
            if (browser.toLowerCase().equals("safari")){
                if (!verifyHLS()){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hlsMp4Priority(String encodingPrioritySet){

        if (url.getVideoPlugins().toLowerCase().equals("main")){
            if (browser.toLowerCase().equals("safari")){
                if (encodingPrioritySet.equals("hls")) {
                    if (!verifyHLS()){
                        return false;
                    }
                }

                if (encodingPrioritySet.equals("mp4")){
                    if (!verifyMP4()){
                        return false;
                    }
                }
            }else {
                if (!verifyMP4()){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean dashHlsHdsMp4Priority(String encodingPrioritySet){
        if (url.getVideoPlugins().toLowerCase().equals("bitmovin")){
            if (encodingPrioritySet.equals("hls")){
                if (!verifyMP4()){
                    return false;
                }
            }

            if (encodingPrioritySet.equals("dash")){
                if (getBrowser().equalsIgnoreCase("safari")){
                    String secondEncoPriority = new PlayBackFactory(driver, extentTest).getPlayerAPIAction().getEncodingPriority(1);
                    if (secondEncoPriority.equals("mp4")){
                        if (!verifyMP4()){
                            return false;
                        }
                    }else {
                        if (!streamElement.getText().contains(encodedFormat.get(secondEncoPriority))) {
                            logger.error("video is not getting served through encoding priority " + secondEncoPriority);
                            extentTest.log(LogStatus.FAIL, "video is getting served through encoding priority " + secondEncoPriority);
                            return false;
                        }
                        logger.info("video is getting served through encoding priority " + secondEncoPriority);
                        extentTest.log(LogStatus.PASS, "video is not getting served through encoding priority " + secondEncoPriority);
                    }
                }else {
                    if (!verifyDASH()){
                        return false;
                    }
                }
            }

            if (encodingPrioritySet.equals("hds")){
                if (!verifyHDS()){
                    return false;
                }
            }

            if (encodingPrioritySet.equals("mp4")){
                if (!verifyMP4()){
                    return false;
                }
            }

        }

        if (url.getVideoPlugins().toLowerCase().equals("osmf")){
            logger.info("************************** Checking encoding priority for "+browser+" browser having supported muxing format as ["+supportedMuxFormat+"] for video Plugin "+url.getVideoPlugins()+" ***********************************");
            if (!verifyHDS()){
                return false;
            }
        }
        
        PlayerAPIAction playerAPI = new PlayBackFactory(driver, extentTest).getPlayerAPIAction();

        if (url.getVideoPlugins().toLowerCase().equals("main")) {
            if (encodingPrioritySet.equals("dash") || encodingPrioritySet.equals("hds")) {
                String secondEncoPriority = playerAPI.getEncodingPriority(1);
                if (secondEncoPriority.equals("dash") || secondEncoPriority.equals("hds")){
                    String thirdEncoPriority = playerAPI.getEncodingPriority(2);
                    if (!hlsMp4Priority(thirdEncoPriority)){
                        return false;
                    }
                }else {
                    if (!hlsMp4Priority(secondEncoPriority)){
                        return false;
                    }
                }
            }else {
                if (!hlsMp4Priority(encodingPrioritySet)) {
                    return false;
                }
            }
        }
        return true;
    }

    public  boolean equalLists(List<String> a, List<String> b){
        // Check for sizes and nulls
        if ((a.size() != b.size()) || (a == null && b!= null) || (a != null && b== null)){
            return false;
        }

        if (a == null && b == null) return true;

        // Sort and compare the two lists
        Collections.sort(a);
        Collections.sort(b);
        return a.equals(b);
    }

    public boolean validateDRM(){
        try {
            if (isVideoPluginPresent("osmf")) {
                extentTest.log(LogStatus.INFO, "Cannot validate DRM for Adobe access");
                return true;
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
        String text = driver.executeScript("return OO.DEBUG.consoleOutput[0].toString().split(/2\":(.+)/)[1]").toString();
        org.json.JSONObject json = new org.json.JSONObject(text);
        String certificate_url = "";
        if (getBrowser().equalsIgnoreCase("safari")) {
            if (!json.has("hls_drm")) {
                extentTest.log(LogStatus.FAIL, "hls_drm not found.");
                return false;
            }
            org.json.JSONObject hls_drm = json.getJSONObject("hls_drm");
            if (!hls_drm.has("drm")) {
                extentTest.log(LogStatus.FAIL, "drm not found.");
                return false;
            }
            org.json.JSONObject drm = hls_drm.getJSONObject("drm");
            if (!drm.has("fairplay")) {
                extentTest.log(LogStatus.FAIL, "fairplay not found.");
                return false;
            }
            certificate_url = drm.getJSONObject("fairplay").getString("la_url");
            if (!certificate_url.contains("/sas/fps/")) {
                extentTest.log(LogStatus.FAIL, "la_url does not start with player.ooyala.com/sas/fps/");
                return false;
            }
        }

        if (getBrowser().equalsIgnoreCase("chrome") || getBrowser().equalsIgnoreCase("firefox")){
            if (!json.has("dash_drm")) {
                extentTest.log(LogStatus.FAIL, "dash_drm not found.");
                return false;
            }
            org.json.JSONObject dash_drm = json.getJSONObject("dash_drm");
            if (!dash_drm.has("drm")) {
                extentTest.log(LogStatus.FAIL, "drm not found.");
                return false;
            }
            org.json.JSONObject drm = dash_drm.getJSONObject("drm");
            if (!drm.has("widevine")) {
                extentTest.log(LogStatus.FAIL, "widevine not found.");
                return false;
            }
            certificate_url = drm.getJSONObject("widevine").getString("la_url");
            if (!certificate_url.contains("/sas/drm2/")) {
                extentTest.log(LogStatus.FAIL,
                        "certificate_url does not start with http://player.ooyala.com/sas/drm2/");
                return false;
            }
        }

        if (getBrowser().equalsIgnoreCase("internet explorer") || getBrowser().equalsIgnoreCase("microsoftedge")){
            //TODO playready -as of now playready is found emplty
        }
        return true;
    }


    public boolean verifyHLS(){
        if (!streamElement.getText().contains("m3u8")){
            logger.error("video is not getting served through encoding priority hls.");
            extentTest.log(LogStatus.FAIL,"video is not getting served through encoding priority hls.");
            return false;
        }
        logger.info("video is getting served through encoding priority hls.");
        extentTest.log(LogStatus.PASS,"video is getting served through encoding priority hls.");
        return true;
    }

    public boolean verifyDASH(){
        if (!streamElement.getText().contains("mpd")) {
            logger.error("video is not getting served through encoding priority dash.");
            extentTest.log(LogStatus.FAIL, "video is not getting served through encoding priority dash.");
            return false;
        }
        logger.info("video is getting served through encoding priority dash.");
        extentTest.log(LogStatus.PASS, "video is getting served through encoding priority dash.");
        return true;
    }

    public boolean verifyHDS(){
        if (!streamElement.getText().contains("f4m")){
            logger.error("video is not getting served through encoding priority hds.");
            extentTest.log(LogStatus.FAIL,"video is not getting served through encoding priority hds.");
            return false;
        }
        logger.info("video is getting served through encoding priority hds.");
        extentTest.log(LogStatus.PASS,"video is getting served through encoding priority hds.");
        return true;
    }

    public boolean verifyMP4(){
        if (streamElement.getText().contains("m3u8")
                || streamElement.getText().contains("mpd")
                || streamElement.getText().contains("f4m")){
            logger.error("video is not getting served with default encoding priority i.e mp4 on "+browser+" having muxing format as ["+supportedMuxFormat+"]");
            extentTest.log(LogStatus.FAIL,"video is not getting served with default encoding priority i.e MP4 on "+browser+" having muxing format as ["+supportedMuxFormat+"]");
            return false;
        }
        logger.info("video is getting served with default encoding priority i.e mp4 on "+browser+" having muxing format as ["+supportedMuxFormat+"]");
        extentTest.log(LogStatus.PASS,"video is getting served with default encoding priority i.e MP4 on "+browser+" having muxing format as ["+supportedMuxFormat+"]");
        return true;
    }

    public boolean verifySecondEncodingPriority(String secondEncoPriority){
        String streamFormat = encodedFormat.get(secondEncoPriority);
        if (!streamElement.getText().contains(streamFormat)){
            logger.error("video is not getting served through encoding priority "+secondEncoPriority);
            extentTest.log(LogStatus.FAIL,"video is not getting served through encoding priority "+secondEncoPriority);
            return false;
        }
        logger.info("video is getting served through encoding priority hds "+secondEncoPriority);
        extentTest.log(LogStatus.PASS,"video is getting served through encoding priority hds "+secondEncoPriority);
        return true;
    }
}
