package com.ooyala.playback.page;

import com.ooyala.playback.httpserver.SimpleHttpServer;
import com.ooyala.playback.utils.JSScriptInjection;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

/**
 * Created by jitendra on 08/05/17.
 */
public class AnalyticsValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(AnalyticsValidator.class);

    public AnalyticsValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    public boolean validate(String element, int timeout) throws Exception {
        if (!waitOnElement(By.id(element), 5000)) {
            extentTest.log(LogStatus.FAIL, "Event not verified : " + element);
            logger.error("Event not verified : " + element);
            return false;
        }
        extentTest.log(LogStatus.PASS, "Event verified : " + element);
        logger.error("Event verified :" + element);
        return true;
    }

    public void getConsoleLogForAnalytics() throws UnknownHostException {
        JSScriptInjection js = new JSScriptInjection(driver);
        InetAddress inetAdd = InetAddress.getLocalHost();
        String url = "http://" + inetAdd.getHostAddress() + ":"
                + SimpleHttpServer.portNumber + "/js?fileName=analytics/analytics_events.js";
        logger.info("JS - " + url);
        js.scriptToInjectJS(url);
    }
}
