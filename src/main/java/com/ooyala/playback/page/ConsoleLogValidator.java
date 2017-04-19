package com.ooyala.playback.page;

import com.ooyala.playback.url.UrlObject;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jitendra on 10/4/17.
 */
public class ConsoleLogValidator extends PlayBackPage implements PlaybackValidator{

    public static Logger logger = Logger.getLogger(ConsoleLogValidator.class);

    public ConsoleLogValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {
        return false;
    }

    public void startRecordingConsoleLogs(){
        driver.executeScript("OO.DEBUG.startRecordingConsoleOutput(\"VC: For video 'ads'\");");
    }

    public void stopRecordingConsoleLog(){
        driver.executeScript(
                "function stopRecording(){\n" +
                        "\twhile(true){\n" +
                        "\tvar len = OO.DEBUG.consoleOutput.length;\n" +
                        "\tif (len == 2) {\n" +
                        "\t\tconsole.log(\"in if loop\");\n" +
                        "\t\tOO.DEBUG.stopRecordingConsoleOutput();\n" +
                        "\t\tbreak;\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "}\n" +
                        "stopRecording();"
        );
    }

    public void getConsoleLogs(){
        ArrayList<String> consoleoutput [] = (ArrayList<String>[])driver.executeScript("return OO.DEBUG.consoleOutput");
    }
}
