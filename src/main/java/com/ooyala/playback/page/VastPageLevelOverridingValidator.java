package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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
	public VastPageLevelOverridingValidator setAdType(String adType){
		this.adType = adType;
		return this;
	}

	boolean isMoreAds=false;
    public VastPageLevelOverridingValidator isMoreThanOneAd(boolean isMoreAds){
	    this.isMoreAds = isMoreAds;
	    return this;
    }

    /**
     *  Validating adPosition type and ad position and based on it checking if that particular ad starts from given ad position....
     *  ad postion type would be either "t" or "p" where t=time which varies from 0 to 100 and
     * @param element
     * @param timeout
     * @return true if pass and false if fails
     * @throws Exception
     */
	public boolean validate(String element, int timeout) throws Exception {
		String adPositionType="";
		long adPosition=0L;
		adType = adType.toLowerCase();
		long videoDuration = (long)executeJsScript("pp.getDuration().toFixed()","long");
		extentTest.log(LogStatus.INFO,"Video duration is :"+videoDuration);

		if (isMoreAds){
            if (adType.equalsIgnoreCase("preroll")){
                adPositionType = (String) executeJsScript("pp.parameters.vast.all_ads[2].position_type", "string");
                adPosition = (Long) executeJsScript("pp.parameters.vast.all_ads[2].position", "long");
            }
            if (adType.equalsIgnoreCase("midroll")){
                adPositionType = (String) executeJsScript("pp.parameters.vast.all_ads[1].position_type", "string");
                adPosition = (Long) executeJsScript("pp.parameters.vast.all_ads[1].position", "long");
            }
            if (adType.equalsIgnoreCase("postroll")){
                adPositionType = (String) executeJsScript("pp.parameters.vast.all_ads[0].position_type", "string");
                adPosition = (Long) executeJsScript("pp.parameters.vast.all_ads[0].position", "long");
            }
        }else {
            adPositionType = (String) executeJsScript("pp.parameters.vast.all_ads[0].position_type", "string");
            adPosition = (Long) executeJsScript("pp.parameters.vast.all_ads[0].position", "long");
        }

		if (adType.equalsIgnoreCase("Preroll") ||
				adType.equalsIgnoreCase("Midroll") ||
				adType.equalsIgnoreCase("Postroll")) {

			if (adPositionType.equalsIgnoreCase("t")) {
                adPosition = TimeUnit.MILLISECONDS.toSeconds(adPosition);
			    extentTest.log(LogStatus.INFO,"Ad Position type is t");
                switch (adType) {
                    case "preroll":
                        extentTest.log(LogStatus.INFO,"Checking Ad Position type for preroll");
                        if (adPosition != 0) {
                            extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                            return false;
                        }
                        extentTest.log(LogStatus.INFO,"Checked Ad Position type for preroll");
                        break;
                    case "midroll":
                        extentTest.log(LogStatus.INFO,"Checking Ad Position type for midroll");
                        if (!(adPosition > 0 && adPosition < videoDuration)) {
                            extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                            return false;
                        }
                        extentTest.log(LogStatus.INFO,"Checked Ad Position type for midroll");
                        break;
                    case "postroll":
                        extentTest.log(LogStatus.INFO,"Checking Ad Position type for postroll");
                        String totalVideoTimeInString = driver.findElement(By.xpath(".//span[@class='oo-total-time']")).getText();
                        int totalVideoTime = (60 * Integer.parseInt(totalVideoTimeInString.split(":")[0])) + Integer.parseInt(totalVideoTimeInString.split(":")[1]);
                        if (adPosition != totalVideoTime) {
                            extentTest.log(LogStatus.FAIL, "adPosition must be "+adPosition+" but getting " + videoDuration + " milliseconds");
                            return false;
                        }
                        extentTest.log(LogStatus.INFO,"Checked Ad Position type for postroll");
                        break;
                }
            }

            if (adPositionType.equalsIgnoreCase("p")){
                extentTest.log(LogStatus.INFO,"Ad Position type is p");
                String playheadTime="";
                int totalVideoTime = 0;
                try {
                    for (int i=0 ; i<=5 ; i++){
                        moveElement(getWebElement("HIDDEN_CONTROL_BAR"));
                        playheadTime = getWebElement("PLAYHEAD_TIME").getText();
                        if (!playheadTime.equalsIgnoreCase("")){
                            break;
                        }
                        Thread.sleep(1000);
                    }
                    totalVideoTime = (60 * Integer.parseInt(playheadTime.split(":")[0])) + Integer.parseInt(playheadTime.split(":")[1]);
                    extentTest.log(LogStatus.INFO,"Total Duration : "+totalVideoTime);
                } catch (Exception nulp){
                    nulp.getStackTrace();
                    extentTest.log(LogStatus.FAIL,"Playhead time is not showing on control bar ...\n"+nulp.getMessage());
                    return false;
                }
                videoDuration = (long)executeJsScript("pp.getDuration().toFixed()","long");
                adPosition = (adPosition*videoDuration)/100;
                extentTest.log(LogStatus.INFO,"Ad Postion : "+adPosition);
                switch (adType) {
                    case "preroll":
                        extentTest.log(LogStatus.INFO,"Checking Ad Position type for preroll");
                        if (adPosition != 0) {
                            extentTest.log(LogStatus.FAIL, "adPosition must be 0ms but getting " + adPosition + " milliseconds");
                            return false;
                        }
                        extentTest.log(LogStatus.INFO,"Checked Ad Position type for preroll");
                        break;
                    case "midroll":
                        extentTest.log(LogStatus.INFO,"Checking Ad Position type for midroll");
                        if (!(adPosition > 0 && adPosition < 100)) {
                            extentTest.log(LogStatus.FAIL, "adPosition must be between 0 and 100 but getting " + adPosition + "%");
                            return false;
                        }
                        extentTest.log(LogStatus.INFO,"Checked Ad Position type for midroll");
                        break;
                    case "postroll":
                        extentTest.log(LogStatus.INFO,"Checking Ad Position type for postroll");
                        if (adPosition != totalVideoTime) {
                            extentTest.log(LogStatus.FAIL, "adPosition must be "+adPosition+" but getting " + videoDuration + " milliseconds");
                            return false;
                        }
                        extentTest.log(LogStatus.INFO,"Checked Ad Position type for postroll");
                        break;
                }
            }

            if (!adType.equalsIgnoreCase("postroll")) {
                while (!(boolean) executeJsScript("pp.isPlaying()", "boolean")) {
                    Thread.sleep(2000);
                    if (!loadingSpinner()) {
                        extentTest.log(LogStatus.ERROR, "loading spinner appears for long time after ad gets played");
                        return false;
                    }
                }

                int videoStartTime = (int) executeJsScript("pp.getPlayheadTime().toFixed()","int");
                extentTest.log(LogStatus.INFO,"videoStartTime after ad's Playback is :"+videoStartTime);

                if(!(videoStartTime <= (adPosition+3))){
                    extentTest.log(LogStatus.FAIL,"VIDEO is not starting from "+adPosition+" sec instead it starts from "+videoStartTime);
                    return false;
                }
            }else {
                if (adType.equalsIgnoreCase("postroll")){
                    String totalVideoduration = getWebElement("TOTAL_VIDEO_DURATION").getText();
                    int videoEndTime = (60 * Integer.parseInt(totalVideoduration.split(":")[0])) + Integer.parseInt(totalVideoduration.split(":")[1]);
                    extentTest.log(LogStatus.INFO,"videoEndTime after ad's Playback is :"+videoEndTime);
                    if (videoEndTime != adPosition){
                        extentTest.log(LogStatus.FAIL,"VIDEO end Time is not matching Actual Tme : "+videoEndTime+" and expected Time : "+adPosition);
                        return false;
                    }
                }
            }
		}
		return true;
	}
}


