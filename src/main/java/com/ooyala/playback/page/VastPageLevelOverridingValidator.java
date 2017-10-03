package com.ooyala.playback.page;

import com.ooyala.facile.page.WebPage;
import com.ooyala.playback.factory.PlayBackFactory;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

public class VastPageLevelOverridingValidator extends PlayBackPage implements PlaybackValidator {

    private final static Logger logger = Logger.getLogger(VastPageLevelOverridingValidator.class);

    public VastPageLevelOverridingValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("time");
        addElementToPageElements("pause");
    }

    private String adType;

    public VastPageLevelOverridingValidator setAdType(String adType) {
        this.adType = adType;
        return this;
    }

    private boolean isMoreAds = false;

    public VastPageLevelOverridingValidator isMoreThanOneAd(boolean isMoreAds) {
        this.isMoreAds = isMoreAds;
        return this;
    }

    private long videoDuration;

    public void getTotalDuration() {
        videoDuration = new PlayBackFactory(driver, extentTest).getPlayerAPIAction().getDurationFixed();
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
        	new PlayBackFactory(driver, extentTest).getPlayerAPIAction().pause();
        }
        extentTest.log(LogStatus.INFO, "Video duration is :" + videoDuration);
        logger.info("Video duration is :" + videoDuration);

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
            logger.info("Ad Position type is t");
            switch (adType) {
                case "preroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for preroll");
                    logger.info("Checking Ad Position type for preroll");
                    if (adPosition != 0) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        logger.error("adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for preroll");
                    logger.info("Checked Ad Position type for preroll");
                    break;
                case "midroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for midroll");
                    logger.info("Checking Ad Position type for midroll");
                    if (!(adPosition > 0 && adPosition < videoDuration)) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        logger.error("adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for midroll");
                    logger.info("Checked Ad Position type for midroll");
                    break;
                case "postroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for postroll");
                    logger.info("Checking Ad Position type for postroll");
                    String totalVideoTimeInString = getWebElement("PLAYHEAD_TIME").getText();
                    int totalVideoTime = (60 * Integer.parseInt(totalVideoTimeInString.split(":")[0])) + Integer.parseInt(totalVideoTimeInString.split(":")[1]);
                    if (adPosition != totalVideoTime) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be " + adPosition + " but getting " + videoDuration + " milliseconds");
                        logger.error("adPosition must be " + adPosition + " but getting " + videoDuration + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for postroll");
                    logger.info("Checked Ad Position type for postroll");
                    break;

                case "preroll overlay":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for preroll overlay");
                    logger.info("Checking Ad Position type for preroll overlay");
                    if (adPosition != 0) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        logger.error("adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for preroll overlay");
                    logger.info("Checked Ad Position type for preroll overlay");
                    break;

                case "midroll overlay":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for midroll overlay");
                    logger.info("Checking Ad Position type for midroll overlay");
                    if (!(adPosition > 0 && adPosition < videoDuration)) {
                        WebPage.logger.info("adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        extentTest.log(LogStatus.FAIL, "adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        logger.error("adPosition must be between 0ms and total duration of video but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for midroll overlay");
                    logger.info("Checked Ad Position type for midroll overlay");
                    break;
            }
        }

        if (adPositionType.equalsIgnoreCase("p")) {
            extentTest.log(LogStatus.INFO, "Ad Position type is p");
            logger.info("Ad Position type is p");
            adPosition = (adPosition * videoDuration) / 100;
            extentTest.log(LogStatus.INFO, "Ad Postion : " + adPosition);
            logger.info("Ad Postion : " + adPosition);
            switch (adType) {
                case "preroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for preroll");
                    logger.info("Checking Ad Position type for preroll");
                    if (adPosition != 0) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        logger.error("adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for preroll");
                    logger.info("Checked Ad Position type for preroll");
                    break;
                case "midroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for midroll");
                    logger.info("Checking Ad Position type for midroll");
                    if (!(adPosition > 0 && adPosition < 100)) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be between 0 and 100 but getting " + adPosition + "%");
                        logger.error("adPosition must be between 0 and 100 but getting " + adPosition + "%");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for midroll");
                    logger.info("Checked Ad Position type for midroll");
                    break;
                case "postroll":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for postroll");
                    logger.info("Checking Ad Position type for postroll");
                    if (adPosition != videoDuration) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be " + adPosition + " but getting " + videoDuration + " milliseconds");
                        logger.error("adPosition must be " + adPosition + " but getting " + videoDuration + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for postroll");
                    logger.info("Checked Ad Position type for postroll");
                    break;

                case "preroll overlay":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for preroll overlay");
                    logger.info("Checking Ad Position type for preroll overlay");
                    if (adPosition != 0) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        logger.error("adPosition must be 0ms but getting " + adPosition + " milliseconds");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for preroll overlay");
                    logger.info("Checked Ad Position type for preroll overlay");
                    break;

                case "midroll overlay":
                    extentTest.log(LogStatus.INFO, "Checking Ad Position type for midroll");
                    logger.info("Checking Ad Position type for midroll");
                    if (!(adPosition > 0 && adPosition < 100)) {
                        extentTest.log(LogStatus.FAIL, "adPosition must be between 0 and 100 but getting " + adPosition + "%");
                        logger.error("adPosition must be between 0 and 100 but getting " + adPosition + "%");
                        return false;
                    }
                    extentTest.log(LogStatus.INFO, "Checked Ad Position type for midroll");
                    logger.info("Checked Ad Position type for midroll");
                    break;
            }
        }

        if (!adType.equalsIgnoreCase("postroll")) {
            int videoStartTime = (int) executeJsScript("pp.getPlayheadTime().toFixed()", "int");
            extentTest.log(LogStatus.INFO, "videoStartTime after ad's Playback is :" + videoStartTime);
            logger.info("videoStartTime after ad's Playback is :" + videoStartTime);

            if (!(videoStartTime <= (adPosition + 3))) {
                extentTest.log(LogStatus.FAIL, "VIDEO is not starting from " + adPosition + " sec instead it starts from " + videoStartTime);
                logger.error("VIDEO is not starting from " + adPosition + " sec instead it starts from " + videoStartTime);
                return false;
            }
        } else {
            if (adType.equalsIgnoreCase("postroll")) {
                String totalVideoduration = getWebElement("TOTAL_VIDEO_DURATION").getText();
                int videoEndTime = (60 * Integer.parseInt(totalVideoduration.split(":")[0])) + Integer.parseInt(totalVideoduration.split(":")[1]);
                extentTest.log(LogStatus.INFO, "videoEndTime after ad's Playback is :" + videoEndTime);
                logger.info("videoEndTime after ad's Playback is :" + videoEndTime);
                if (videoEndTime != adPosition) {
                    extentTest.log(LogStatus.FAIL, "VIDEO end Time is not matching Actual Tme : " + videoEndTime + " and expected Time : " + adPosition);
                    logger.error("VIDEO end Time is not matching Actual Tme : " + videoEndTime + " and expected Time : " + adPosition);
                    return false;
                }
            }
        }

        if (!adType.equalsIgnoreCase("postroll")) {
        	new PlayBackFactory(driver, extentTest).getPlayerAPIAction().play();
        }
        return true;
    }
}


