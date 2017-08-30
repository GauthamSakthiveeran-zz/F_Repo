package com.ooyala.playback.page;

import com.ooyala.facile.page.WebPage;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

public class VastPageLevelOverridingValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger LOG = Logger.getLogger(VastPageLevelOverridingValidator.class);

    public VastPageLevelOverridingValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("time");
        addElementToPageElements("pause");
    }

    public String adType;

    public VastPageLevelOverridingValidator setAdType(String adType) {
        this.adType = adType;
        return this;
    }

    boolean isMoreAds = false;

    public VastPageLevelOverridingValidator isMoreThanOneAd(boolean isMoreAds) {
        this.isMoreAds = isMoreAds;
        return this;
    }

    long videoDuration;

    public void getTotalDuration() {
        videoDuration = Integer.parseInt(driver.executeScript("return pp.getDuration()").toString());
        videoDuration = TimeUnit.MILLISECONDS.toSeconds(videoDuration);
    }

    /**
     * Validating adPosition type and ad position and based on it checking if that particular ad starts from given ad position....
     * ad postion type would be either "t" or "p" where t=time which varies from 0 to 100 and
     *
     * @param element
     * @param timeout
     * @return true if pass and false if fails
     * @throws Exception
     */
    public boolean validate(String element, int timeout) throws Exception {
        String adPositionType = "";
        long adPosition = 0L;
        adType = adType.toLowerCase();
        if (!adType.equalsIgnoreCase("postroll")) {
            driver.executeScript("pp.pause()");
        }
        extentTest.log(LogStatus.INFO, "Video duration is :" + videoDuration);
        LOG.info("Video duration is :" + videoDuration);

        if (isMoreAds) {
            if (adType.equalsIgnoreCase("preroll")) {
                adPositionType = (String) executeJsScript("pp.parameters.vast.all_ads[2].position_type", "string");
                adPosition = (Long) executeJsScript("pp.parameters.vast.all_ads[2].position", "long");
            }
            if (adType.equalsIgnoreCase("midroll")) {
                adPositionType = (String) executeJsScript("pp.parameters.vast.all_ads[1].position_type", "string");
                adPosition = (Long) executeJsScript("pp.parameters.vast.all_ads[1].position", "long");
            }
            if (adType.equalsIgnoreCase("postroll")) {
                adPositionType = (String) executeJsScript("pp.parameters.vast.all_ads[0].position_type", "string");
                adPosition = (Long) executeJsScript("pp.parameters.vast.all_ads[0].position", "long");
            }
        } else {
            adPositionType = (String) executeJsScript("pp.parameters.vast.all_ads[0].position_type", "string");
            adPosition = (Long) executeJsScript("pp.parameters.vast.all_ads[0].position", "long");
        }

        if (adPositionType.equalsIgnoreCase("t")) {
            adPosition = TimeUnit.MILLISECONDS.toSeconds(adPosition);
            extentTest.log(LogStatus.INFO, "Ad Position type is t");
            LOG.info("Ad Position type is t");
            switch (adType) {
                case "preroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for preroll");
                    LOG.info("Checking Ad Position type for preroll");
                    if (adPosition != 0) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        LOG.error("adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for preroll");
                    LOG.info("Checked Ad Position type for preroll");
                    break;
                case "midroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for midroll");
                    LOG.info("Checking Ad Position type for midroll");
                    if (!(adPosition > 0 && adPosition < videoDuration)) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        LOG.error("adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for midroll");
                    LOG.info("Checked Ad Position type for midroll");
                    break;
                case "postroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for postroll");
                    LOG.info("Checking Ad Position type for postroll");
                    String totalVideoTimeInString = getWebElement("PLAYHEAD_TIME").getText();
                    int totalVideoTime = (60 * Integer.parseInt(totalVideoTimeInString.split(":")[0])) + Integer.parseInt(totalVideoTimeInString.split(":")[1]);
                    if (adPosition != totalVideoTime) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be " + adPosition + " but getting " + videoDuration + " milliseconds");
                        LOG.error("adPosition must be " + adPosition + " but getting " + videoDuration + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for postroll");
                    LOG.info("Checked Ad Position type for postroll");
                    break;

                case "preroll overlay":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for preroll overlay");
                    LOG.info("Checking Ad Position type for preroll overlay");
                    if (adPosition != 0) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        LOG.error("adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for preroll overlay");
                    LOG.info("Checked Ad Position type for preroll overlay");
                    break;

                case "midroll overlay":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for midroll overlay");
                    LOG.info("Checking Ad Position type for midroll overlay");
                    if (!(adPosition > 0 && adPosition < videoDuration)) {
                        WebPage.logger.info("adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        extentTest.log(LogStatus.FAIL, "adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        LOG.error("adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for midroll overlay");
                    LOG.info("Checked Ad Position type for midroll overlay");
                    break;
            }
        }

        if (adPositionType.equalsIgnoreCase("p")) {
            extentTest.log(LogStatus.INFO, "Ad Position type is p");
            LOG.info("Ad Position type is p");
            adPosition = (adPosition * videoDuration) / 100;
            extentTest.log(LogStatus.INFO, "Ad Postion : " + adPosition);
            LOG.info("Ad Postion : " + adPosition);
            switch (adType) {
                case "preroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for preroll");
                    LOG.info("Checking Ad Position type for preroll");
                    if (adPosition != 0) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        LOG.error("adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for preroll");
                    LOG.info("Checked Ad Position type for preroll");
                    break;
                case "midroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for midroll");
                    LOG.info("Checking Ad Position type for midroll");
                    if (!(adPosition > 0 && adPosition < 100)) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be between 0 and 100 but getting " + adPosition + "%");
                        LOG.error("adPosition must be between 0 and 100 but getting " + adPosition + "%");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for midroll");
                    LOG.info("Checked Ad Position type for midroll");
                    break;
                case "postroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for postroll");
                    LOG.info("Checking Ad Position type for postroll");
                    if (adPosition != videoDuration) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be " + adPosition + " but getting " + videoDuration + " milliseconds");
                        LOG.error("adPosition must be " + adPosition + " but getting " + videoDuration + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for postroll");
                    LOG.info("Checked Ad Position type for postroll");
                    break;

                case "preroll overlay":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for preroll overlay");
                    LOG.info("Checking Ad Position type for preroll overlay");
                    if (adPosition != 0) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        LOG.error("adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for preroll overlay");
                    LOG.info("Checked Ad Position type for preroll overlay");
                    break;

                case "midroll overlay":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for midroll");
                    LOG.info("Checking Ad Position type for midroll");
                    if (!(adPosition > 0 && adPosition < 100)) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be between 0 and 100 but getting " + adPosition + "%");
                        LOG.error("adPosition must be between 0 and 100 but getting " + adPosition + "%");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for midroll");
                    LOG.info("Checked Ad Position type for midroll");
                    break;
            }
        }

        if (!adType.equalsIgnoreCase("postroll")) {
            int videoStartTime = (int) executeJsScript("pp.getPlayheadTime().toFixed()", "int");
            extentTest.log(LogStatus.INFO, "videoStartTime after ad's Playback is :" + videoStartTime);
            LOG.info("videoStartTime after ad's Playback is :" + videoStartTime);

            if (!(videoStartTime <= (adPosition + 3))) {
                extentTest.log(LogStatus.FAIL, "VIDEO is not starting from " + adPosition + " sec instead it starts from " + videoStartTime);
                LOG.error("VIDEO is not starting from " + adPosition + " sec instead it starts from " + videoStartTime);
                return false;
            }
        } else {
            if (adType.equalsIgnoreCase("postroll")) {
                String totalVideoduration = getWebElement("TOTAL_VIDEO_DURATION").getText();
                int videoEndTime = (60 * Integer.parseInt(totalVideoduration.split(":")[0])) + Integer.parseInt(totalVideoduration.split(":")[1]);
                extentTest.log(LogStatus.INFO, "videoEndTime after ad's Playback is :" + videoEndTime);
                LOG.info("videoEndTime after ad's Playback is :" + videoEndTime);
                if (videoEndTime != adPosition) {
                    extentTest.log(LogStatus.FAIL, "VIDEO end Time is not matching Actual Tme : " + videoEndTime + " and expected Time : " + adPosition);
                    LOG.error("VIDEO end Time is not matching Actual Tme : " + videoEndTime + " and expected Time : " + adPosition);
                    return false;
                }
            }
        }

        if (!adType.equalsIgnoreCase("postroll")) {
            driver.executeScript("pp.play()");
        }
        return true;
    }
}


