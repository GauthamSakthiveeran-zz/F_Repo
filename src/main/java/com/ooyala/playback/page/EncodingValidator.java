package com.ooyala.playback.page;

import static java.lang.Character.CONTROL;
import static java.net.URLDecoder.decode;
import static org.openqa.selenium.Keys.DELETE;
import static org.testng.Assert.assertEquals;

import com.ooyala.playback.url.UrlObject;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.factory.PlayBackFactory;

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
            expectedEncodings = ((JavascriptExecutor) driver).executeScript("return pp.parameters.encodingPriority");
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

        if(!waitOnElement(By.id("videoPlayingurl"),30000)){
            logger.error("streamElement is not embedded in DOM Something is wrong...");
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

        isEncodingPriorityNotSet = driver.executeScript("return typeof pp.parameters.encodingPriority").toString().equals("undefined");

        /**
         * Verify default encoding priority when priority is not passed from playerParameter
         */
        if (isEncodingPriorityNotSet){
            if(!checkStreamTypeForDefaultEncodingPriority()){
                return false;
            }
        }

        if (!isEncodingPriorityNotSet){
            String encodingPrioritySet = driver.executeScript("return pp.parameters.encodingPriority[0]").toString();

            /**
             * Verify default encoding priority when priority is passed from playerParameter
             */
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
            }
        }

        return true;
    }

    public boolean checkStreamTypeForDefaultEncodingPriority() {
        if (supportedMuxFormats == null && videoPlugins == null) {
            logger.info("*************************************************************************************");
            logger.info("Checking video streaming type with default encoding priority");
            if (supportedMuxFormat.equals("hls")) {
                if (!streamElement.getText().contains("m3u8")) {
                    logger.error("video is not playing with default encoding priority... i.e hls having muxing format as only hls.");
                    extentTest.log(LogStatus.FAIL, "video is not playing with default encoding priority... i.e m3u8 having muxing format as only hls.");
                    return false;
                }
                logger.info("video is playing with streaming type as m3u8");
            }

            if (supportedMuxFormat.equals("dash")) {
                if (!streamElement.getText().contains("mpd")) {
                    logger.error("video is not playing with default encoding priority... i.e dash having muxing format as only dash.");
                    extentTest.log(LogStatus.FAIL, "video is not playing with default encoding priority... i.e mpd having muxing format as only dash.");
                    return false;
                }
                logger.info("video is playing with streaming type as mpd");
            }

            if (supportedMuxFormat.equals("hds")) {
                if (!streamElement.getText().contains("f4m")) {
                    logger.error("video is not playing with default encoding priority... i.e hds having muxing format as only hds.");
                    extentTest.log(LogStatus.FAIL, "video is not playing with default encoding priority... i.e mpd having muxing format as only dash.");
                    return false;
                }
                logger.info("video is playing with streaming type as f4m");
            }
            logger.info("*************************************************************************************");
        }

        /***
         * Checking default encoding priority for video having single video plugin and more that one supported muxing format.
         */
        if (supportedMuxFormats != null && videoPlugins == null){
            // Checking default streaming type for bitmovin when encoding priority is not set from player parameter..
            if (supportedMuxFormats != null && url.getVideoPlugins().toLowerCase().equals("bitmovin")){
                if (!streamElement.getText().contains("m3u8")){
                    return false;
                }
            }

            //TODO test will get failed if videoWillPlay does not contain url having mp4 as a part e.g ["dash","mp4"]
            // Checking default streaming type for main when encoding priority is not set from player parameter..
            if (supportedMuxFormats != null && url.getVideoPlugins().toLowerCase().equals("main")){
                if (browser.equals("safari")){
                    if (!streamElement.getText().contains("m3u8")){
                        logger.error("video is not getting served with default encoding priority i.e hls on safari having muxing format as hls+mp4");
                        extentTest.log(LogStatus.FAIL,"video is not getting served with default encoding priority i.e hls on safari having muxing format as hls+mp4");
                        return false;
                    }
                    logger.info("video is getting served with default encoding priority i.e hls on safari having muxing format as hls+mp4");
                    extentTest.log(LogStatus.PASS,"video is getting served with default encoding priority i.e hls on safari having muxing format as hls+mp4");
                }else {
                    if (!streamElement.getText().contains("mp4")){
                        logger.error("video is not getting served with default encoding priority i.e mp4 on "+browser+" having muxing format as hls+mp4");
                        extentTest.log(LogStatus.FAIL,"video is not getting served with default encoding priority i.e MP4 on "+browser+" having muxing format as hls+mp4");
                        return false;
                    }
                    logger.info("video is getting served with default encoding priority i.e mp4 on "+browser+" having muxing format as hls+mp4");
                    extentTest.log(LogStatus.PASS,"video is getting served with default encoding priority i.e MP4 on "+browser+" having muxing format as hls+mp4");
                }
                return true;
            }

            // Checking default streaming type for main when encoding priority is not set from player parameter..
            if (getPlatform().toLowerCase().equals("android")
                    && url.getVideoPlugins().toLowerCase().equals("bitmovin")
                    && supportedMuxFormats != null){
                if (!streamElement.getText().contains("mpd")){
                    return false;
                }
            }
        }

        /***
         * Checking default encoding priority when there are more that one video plugin and muxing fromat supported ....
         */
        if (supportedMuxFormats != null && videoPlugins != null){
            // TODO
            if (equalLists(supportedMuxFormatList,hlsMp4)){
                if (!streamElement.getText().contains("m3u8")){
                    logger.error("video is not getting served through hls for encoding priority : "+hlsMp4.toString());
                    extentTest.log(LogStatus.FAIL,"video is not getting served through hls for encoding priority : "+hlsMp4.toString());
                    return false;
                }
                logger.info("video is getting served through hls for encoding priority : "+hlsMp4.toString());
                extentTest.log(LogStatus.PASS,"video is getting served through hls for encoding priority : "+hlsMp4.toString());
            }

            if (equalLists(supportedMuxFormatList,dashMp4)){
                if (!streamElement.getText().contains("mpd")){
                    logger.error("video is not getting served through dash for encoding priority : "+dashMp4.toString());
                    extentTest.log(LogStatus.FAIL,"video is not getting served through dash for encoding priority : "+dashMp4.toString());
                    return false;
                }
                logger.info("video is getting served through dash for encoding priority : "+dashMp4.toString());
                extentTest.log(LogStatus.PASS,"video is getting served through dash for encoding priority : "+dashMp4.toString());
            }
        }
        return true;
    }

    public boolean dashHlsPriority(String encodingPrioritySet){
        if (url.getVideoPlugins().toLowerCase().equals("bitmovin")){
            if (encodingPrioritySet.equals("hls")){
                if (!streamElement.getText().contains("m3u8")){
                    logger.error("video is not getting served through encoding priority hls.");
                    extentTest.log(LogStatus.FAIL,"video is not getting served through encoding priority hls.");
                    return false;
                }
                logger.info("video is getting served through encoding priority hls.");
                extentTest.log(LogStatus.PASS,"video is getting served through encoding priority hls.");
            }

            if (encodingPrioritySet.equals("dash")){
                if (!streamElement.getText().contains("mpd")){
                    logger.error("video is not getting served through encoding priority dash.");
                    extentTest.log(LogStatus.FAIL,"video is not getting served through encoding priority dash.");
                    return false;
                }
                logger.info("video is getting served through encoding priority dash.");
                extentTest.log(LogStatus.PASS,"video is getting served through encoding priority dash.");
            }

            /**
             * hds encoding priority is not supported for video having muxing format as hls+dash
             * therefore if hds encoding priority is set through player parameter then video should get served from second mentioned
             * encoding priority in player parameter.
             * e.g if encoding priority is set as ["hds","hls"] then video should get served via hls and similarly for dash.
             */
            if (encodingPrioritySet.equals("hds")){
                logger.info("checking encoding priority for video ");
                String secondEncoPriority = driver.executeScript("return pp.parameters.encodingPriority[1]").toString();
                if (!streamElement.getText().contains("f4m")){
                    if (secondEncoPriority.equals("dash")){
                        if (!streamElement.getText().contains("mpd")){
                            logger.error("video is not playing with dash second encoding priority when first encoding priority is set to non supported muxing format");
                            extentTest.log(LogStatus.FAIL, "video is not playing with dash second encoding priority when first encoding priority is set to non supported muxing format");
                            return false;
                        }
                        logger.info("video is playing with streaming type as dash which is set as 2nd encoding priority when first encoding priority is not supported for muxing format hls+dash");
                        extentTest.log(LogStatus.PASS,"video is playing with streaming type as dash which is set as 2nd encoding priority when first encoding priority is not supported for muxing format hls+dash");
                    }
                    if (secondEncoPriority.equals("hls")){
                        if (!streamElement.getText().contains("m3u8")){
                            logger.error("video is not playing with hls second encoding priority when first encoding priority is set to non supported muxing format");
                            extentTest.log(LogStatus.FAIL, "video is not playing with hls second encoding priority when first encoding priority is set to non supported muxing format");
                            return false;
                        }
                        logger.info("video is playing with streaming type as hls which is set as 2nd encoding priority when first encoding priority is not supported for muxing format hls+dash");
                        extentTest.log(LogStatus.PASS,"video is playing with streaming type as hls which is set as 2nd encoding priority when first encoding priority is not supported for muxing format hls+dash");
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
                if (!streamElement.getText().contains("m3u8")){
                    logger.error("video is not getting served through hls on safari for MAIN video plugin");
                    extentTest.log(LogStatus.FAIL,"video is not getting served through hls on safari for MAIN video plugin");
                    return false;
                }
                logger.info("video is getting served through hls on safari for MAIN video plugin");
                extentTest.log(LogStatus.PASS,"video is getting served through hls on safari for MAIN video plugin");
            }
        }
        return true;
    }

    public boolean hlsMp4Priority(String encodingPrioritySet){

        if (url.getVideoPlugins().toLowerCase().equals("main")){

            if (browser.toLowerCase().equals("safari")){

                if (encodingPrioritySet.equals("hls")) {
                    if (!streamElement.getText().contains("m3u8")) {
                        logger.error("video is not getting served through hls on safari for MAIN video plugin");
                        extentTest.log(LogStatus.FAIL, "video is not getting served through hls on safari for MAIN video plugin");
                        return false;
                    }
                    logger.info("video is getting served through hls on safari for MAIN video plugin");
                    extentTest.log(LogStatus.PASS, "video is getting served through hls on safari for MAIN video plugin");
                }

                if (encodingPrioritySet.equals("mp4")){
                    if (!streamElement.getText().contains("mp4")) {
                        logger.error("video is not getting served through mp4 on "+browser+" for MAIN video plugin");
                        extentTest.log(LogStatus.FAIL, "video is not getting served through mp4 on "+browser+" for MAIN video plugin");
                        return false;
                    }
                    logger.info("video is getting served through mp4 on "+browser+" for MAIN video plugin");
                    extentTest.log(LogStatus.PASS, "video is getting served through mp4 on "+browser+" for MAIN video plugin");
                }
            }else {
                if (!streamElement.getText().contains("mp4")){
                    logger.error("video is not getting served through mp4 on "+browser+" for MAIN video plugin");
                    extentTest.log(LogStatus.FAIL, "video is not getting served through mp4 on "+browser+" for MAIN video plugin");
                    return false;
                }
                logger.info("video is getting served through mp4 on "+browser+" for MAIN video plugin");
                extentTest.log(LogStatus.PASS, "video is getting served through mp4 on "+browser+" for MAIN video plugin");
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
}
