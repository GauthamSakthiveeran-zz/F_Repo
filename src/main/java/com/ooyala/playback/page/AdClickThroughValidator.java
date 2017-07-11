package com.ooyala.playback.page;

import static com.relevantcodes.extentreports.LogStatus.PASS;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

public class AdClickThroughValidator extends PlayBackPage implements
        PlaybackValidator {

    public static Logger log = Logger.getLogger(AdClickThroughValidator.class);

    public AdClickThroughValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("adclicks");
        addElementToPageElements("adoverlay");
    }

    boolean overlay = false;

    public AdClickThroughValidator overlay() {
        overlay = true;
        return this;
    }

	private boolean validateOverlayClickThrough() {
        if (isElementPresent("OVERLAY_IMAGE")) {
        	if(!getBrowser().equalsIgnoreCase("safari"))
            if (clickOnIndependentElement("OVERLAY_IMAGE")) {
                if (!waitOnElement(By.id("adsClickThroughOpened"), 10000)) {
                    extentTest.log(LogStatus.FAIL, "adsClickThroughOpened not found.");
                } else {
                    extentTest.log(LogStatus.PASS, "adsClickThroughOpened found.");
                }
            } else {
                extentTest.log(LogStatus.FAIL, "OVERLAY_IMAGE not clickable.");
            }
        } else {
            extentTest.log(LogStatus.FAIL, "OVERLAY_IMAGE not found.");
        }
        return true;
    }

    public boolean validate(String element, int timeout) throws Exception {
    	
    	if(getBrowser().equalsIgnoreCase("safari")) return true;

        if (!loadingSpinner()) {
            extentTest.log(LogStatus.FAIL, "In Loading spinner for a really long time.");
            return false;
        }

        String baseWindowHdl = driver.getWindowHandle();

        if (overlay) {
            validateOverlayClickThrough();
            closeOtherWindows(baseWindowHdl);
            ((JavascriptExecutor) driver).executeScript("pp.play()");
            return true;
        }

        Map<String, String> data = parseURL();

        if (data == null) {
            throw new Exception("Map is null");
        }

        String value = data.get("ad_plugin");
        String video_plugin = data.get("video_plugins");

        if (value != null) {
            boolean flag = true;
            ((JavascriptExecutor) driver).executeScript("pp.play()");

            if (isElementPresent("AD_SCREEN_PANEL")) {
                if (!clickOnIndependentElement("AD_SCREEN_PANEL"))
                    return false;

                if (!waitOnElement(By.id("videoPausedAds_2"), 5000)) {
                    log.info("unable to verify event videoPaused");
                    extentTest.log(LogStatus.FAIL, "unable to verify event videoPaused");
                    return false;
                }

            } else if (isElementPresent("AD_PANEL_1")) {
                if (!(clickOnIndependentElement("AD_PANEL_1") && waitOnElement(By.id("adsClickThroughOpened"), 2000))) {
                    return false;
                }
                flag = false;
            } else {
                if (!clickOnIndependentElement("AD_PANEL"))
                    return false;
            }

            if (flag) {
                if (!waitOnElement(By.id("adsClicked_1"), 2000))
                    return false;
                if (!waitOnElement(By.id("adsClicked_videoWindow"), 2000))
                    return false;
            }
            
            validateNoOfTabsOpened();

            extentTest.log(PASS, "AdsClicked by clicking on the ad screen");
            log.info("AdsClicked by clicking on the ad screen");

            if (!value.contains("ima")) {
                if (getBrowser().contains("internet explorer")) {
                    if (value.contains("freewheel") && video_plugin.contains("main") && !video_plugin.contains("osmf")
                            && !video_plugin.contains("bit")) {
                        if (!(waitOnElement("LEARN_MORE_IE", 10000) && clickOnIndependentElement("LEARN_MORE_IE")))
                            return false;
                    } else {
                        if (!(waitOnElement("LEARN_MORE", 10000) && clickOnHiddenElement("LEARN_MORE")))
                            return false;
                    }

                } else {
                    if (!(waitOnElement("LEARN_MORE", 10000) && clickOnIndependentElement("LEARN_MORE")))
                        return false;
                }
                if (!waitOnElement(By.id("adsClicked_learnMoreButton"), 2000))
                    return false;

                if (!waitOnElement(By.id("videoPausedAds_2"), 5000)) {
                    log.info("unable to verify event videoPaused");
                    extentTest.log(LogStatus.FAIL, "unable to verify event videoPaused");
                    return false;
                }

                validateNoOfTabsOpened();

            }
            extentTest.log(PASS, "AdsClicked by clicking on the learn more button");
            log.info("AdsClicked by clicking on the learn more button");

            closeOtherWindows(baseWindowHdl);

            ((JavascriptExecutor) driver).executeScript("pp.play()");
            return true;

        } else {
            throw new Exception("Ad plugin not present in test url");
        }

    }
    
    private void validateNoOfTabsOpened() {
    	int windowHandleCount = getWindowHandleCount();

        if (windowHandleCount <= 1) {
            log.info("New tab did not open on ad click.");
            extentTest.log(LogStatus.FAIL, "New tab did not open on ad click.");
        }
        
        if (windowHandleCount > 1) {
            log.info("No of tabs opened - " + windowHandleCount);
//            extentTest.log(LogStatus.FAIL, "Too many tabs opened - No of tabs : " + windowHandleCount);
        }
    }

    public boolean isAdPlaying() {
        Boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver)
                .executeScript("return pp.isAdPlaying()"));
        return isAdplaying;
    }
}
