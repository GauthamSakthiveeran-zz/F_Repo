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

    public EncodingValidator getStreamType(UrlObject url){
        supportedMuxFormat = url.getSupportedMuxFormat();
        videoPlugin = url.getVideoPlugins();
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

        if(!waitOnElement(By.id("videoPlayingurl"),30000)){
            logger.error("streamElement is not embedded in DOM Something is wrong...");
            return false;
        }

        streamElement = driver.findElement(By.id("videoPlayingurl"));
        logger.info("Streaming url : "+streamElement.getText());

        if (supportedMuxFormat.contains(",")){
            supportedMuxFormats = supportedMuxFormat.split(",");
        }

        if (videoPlugin.contains(",")){
            videoPlugins = videoPlugin.split(",");
        }

        isEncodingPriorityNotSet = driver.executeScript("return typeof pp.parameters.encodingPriority").toString().equals("undefined");

        // Verify default encoding priority when priority is not passed from playerParameter
        if (isEncodingPriorityNotSet){
            if(!checkStreamTypeForDefaultEncodingPriority()){
                return false;
            }
        }

        if (!isEncodingPriorityNotSet){
            // Verify default encoding priority when priority is passed from playerParameter
            if (supportedMuxFormats == null){
                if (!checkStreamTypeForDefaultEncodingPriority()){
                    return false;
                }
            }
            if (supportedMuxFormats != null && videoPlugins == null){

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
        return true;
    }
}
