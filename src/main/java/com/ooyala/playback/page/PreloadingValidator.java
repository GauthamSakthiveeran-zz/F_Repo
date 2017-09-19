package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by suraj on 9/14/17.
 */
public class PreloadingValidator extends PlayBackPage implements PlaybackValidator {

    private final static Logger logger = Logger.getLogger(PreloadingValidator.class);

    public PreloadingValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
    }


    @Override
    public boolean validate(String element, int timeout) throws Exception {
        if (!loadingSpinner()) {
            extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
            return false;
        }
        try {
            if (!waitOnElement(By.id(element), timeout)) {
                logger.error(element + " not found");
                extentTest.log(LogStatus.FAIL, element + " not found");
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.info(element + " found");
        return true;
    }

    public boolean validateFalse(String element, int timeout) throws Exception {
        if (waitOnElement(By.id(element), timeout)) {
            logger.error(element + " found, which should not be the case");
            extentTest.log(LogStatus.FAIL, element + " found, which should not be the case");
            return false;
        }
        return true;
    }

    public void getConsoleLogs() {
        driver.executeScript(
                "var oldf = console.log;\n" +
                        "console.log = function() {\n" +
                        "\toldf.apply(console, arguments);\n" +
                        "\tif(arguments[0].includes('VAST: No \"firstQuartile\"')) \n" +
                        "\t\tOO.$(\"#ooplayer\").append(\"<p id=firstQuartile>\" + arguments[0] + \"</p>\");\n" +
                        "}");
        driver.executeScript(
                "var oldf = console.log;\n" +
                        "console.log = function() {\n" +
                        "\toldf.apply(console, arguments);\n" +
                        "\tif(arguments[0].includes('VAST: No \"firstQuartile\"')) \n" +
                        "\t\tOO.$(\"#ooplayer\").append(\"<p id=thirdQuartile>\" + arguments[0] + \"</p>\");\n" +
                        "}");
        driver.executeScript(
                "var oldf = console.log;\n" +
                        "\tvar counter=1;\n" +
                        "\tconsole.log = function() {\n" +
                        "\toldf.apply(console, arguments);\n" +
                        "\tif(arguments[0].includes('bitplayer: onSegmentRequestFinished')) {\n" +
                        "\t\tOO.$(\"#ooplayer\").append(\"<p id=segments_\"+counter+\">\" + arguments[0] + \"</p>\");\n" +
                        "\t\tcounter++;\t\n" +
                        "\t}\n" +
                        "}");
        driver.executeScript(
                "var oldf = console.log;\n" +
                        "\tvar counter=1;\n" +
                        "\tconsole.log = function() {\n" +
                        "\toldf.apply(console, arguments);\n" +
                        "\tif(arguments[0].includes('bitplayer: onDownloadFinished')) {\n" +
                        "\t\tOO.$(\"#ooplayer\").append(\"<p id=downloads_\"+counter+\">\" + arguments[0] + \"</p>\");\n" +
                        "\t\tcounter++;\t\n" +
                        "\t}\n" +
                        "}");
    }

    public boolean verifyMp4Segments() {
        String data = driver.findElement(By.id("downloads_3")).getText();
        if (!data.contains("mp4")) {
            logger.error("mp4 not found in segments");
            extentTest.log(LogStatus.FAIL, "mp4 not found in segments");
            return false;
        }
        logger.info("mp4 found in segments");
        extentTest.log(LogStatus.PASS, "mp4 found in segments");
        return true;
    }

    public boolean verifyFragementsAndm3u8Segments() {
        String data = driver.findElement(By.id("downloads_3")).getText();
        if (!(data.contains("m3u8") && (data.contains("Fragments")))) {
            logger.error("m3u8 and Fragments not found in segments");
            extentTest.log(LogStatus.FAIL, "m3u8 and Fragments not found in segments");
            return false;
        }
        logger.info("m3u8 and Fragments found in segments");
        extentTest.log(LogStatus.PASS, "m3u8 and Fragments found in segments");
        return true;
    }

    public boolean verifyHLSSegments() {
        String data = driver.findElement(By.id("downloads_3")).getText();
        if (!data.contains(".ts")) {
            logger.error(".ts not found in segments");
            extentTest.log(LogStatus.FAIL, ".ts not found in segments");
            return false;
        }
        logger.info(".ts found in segments");
        extentTest.log(LogStatus.PASS, ".ts found in segments");
        return true;
    }
}
