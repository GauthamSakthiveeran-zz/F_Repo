package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by snehal on 29/11/16.
 */
public class IsAdPlayingValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger Log = Logger.getLogger(PlayValidator.class);

    public IsAdPlayingValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {
        boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver)
                .executeScript("return pp.isAdPlaying()"));
        return isAdplaying;
    }

    public void skipAd() throws Exception {
       ((JavascriptExecutor) driver)
                    .executeScript("pp.skipAd()");
    }

}
