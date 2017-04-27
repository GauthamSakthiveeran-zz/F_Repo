package com.ooyala.playback.page;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 29/12/16.
 */
public class OoyalaAPIValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(OoyalaAPIValidator.class);

    public OoyalaAPIValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
        addElementToPageElements("play");
        addElementToPageElements("pause");
        addElementToPageElements("adclicks");
    }

    @SuppressWarnings("unchecked")
	@Override
    public boolean validate(String element, int timeout) throws Exception {

        Boolean autoplay = (Boolean) (((JavascriptExecutor) driver).executeScript("return pp.parameters.autoplay"));

        if (!autoplay){
            logger.error("Not able to Autoplay");
            return false;
        }

        if(!waitOnElement("AD_PANEL",10000)){
            logger.error("ad is not playing");
            return false;
        }

        boolean isAdplaying = (Boolean) (((JavascriptExecutor) driver)
                .executeScript("return pp.isAdPlaying()"));

        logger.info("isAdplaying :"+isAdplaying);

        if(!isAdplaying){
            logger.error("Ad not playing");
            return false;
        }

        int ShowSkipAdbtnTime = parseInt((((JavascriptExecutor) driver).executeScript("return pp.parameters.linearAdSkipButtonStartTime")).toString());

        while (true) {
            double adPlayheadTime = parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime()").toString());
            if (adPlayheadTime > (double) ShowSkipAdbtnTime) {
                ((JavascriptExecutor) driver).executeScript("pp.skipAd();");
                break;
            }

        }

        if(!waitOnElement(By.id("skipAd_1"), 5000)){
            logger.error("Not able to skip the ad");
            return false;
        }

        loadingSpinner();

        if (!waitOnElement(By.id("videoPlaying_1"),10000)){
            logger.error("Video is not playing");
            return false;
        }

        double initialtime = parseDouble(((JavascriptExecutor) driver).executeScript("return pp.parameters.initialTime").toString());

        double playaheadTime = parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime()").toString());

        if(!(playaheadTime>initialtime)){
            logger.error("Video playback not started from initial time");
            return false;
        }

        double totaltime = parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getDuration()").toString());

        if(!(totaltime > 0)){
            logger.error("Total time must be greater thatn 0 but we are getting it as :"+totaltime);
            return false;
        }

        ((JavascriptExecutor) driver).executeScript("pp.seek(pp.getDuration()-10);");

        if (!waitOnElement(By.id("seeked_1"),10000)){
            logger.error("Not able to seek the video...");
            return false;
        }

        String title = ((JavascriptExecutor) driver).executeScript("return pp.getTitle()").toString();

        if (title==null){
            logger.error("Not able to get title of the video");
            return false;
        }

        String getvolume = (((JavascriptExecutor) driver).executeScript("return pp.getVolume()").toString());

        logger.info("Volume is : "+getvolume);

        if (getvolume==null){
            logger.error("Not able to get volume of the video");
            return false;
        }

        String embedCode = ((JavascriptExecutor) driver).executeScript("return pp.getEmbedCode()").toString();

        if (embedCode==null){
            logger.error("Not able to get Embed code");
            return false;
        }


        ArrayList<String> langlist = ((ArrayList<String>) ((JavascriptExecutor) driver)
                .executeScript("return pp.getCurrentItemClosedCaptionsLanguages().languages;"));


        for (int i = 0; i < langlist.size(); i++) {
            ((JavascriptExecutor) driver).executeScript("pp.setClosedCaptionsLanguage(\"" + langlist.get(i) + "\")");
            if (!waitOnElement(By.id("cclanguage_"+langlist.get(i)),10000)){
                logger.error("Not able to get "+langlist.get(i));
                return false;
            }
        }

        return true;
    }
    
    public boolean validateInitailTime(){
    	double initialtime = parseDouble(((JavascriptExecutor) driver).executeScript("return pp.parameters.initialTime").toString());

        double playaheadTime = parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime()").toString());

        if(!(playaheadTime>initialtime)){
            extentTest.log(LogStatus.FAIL,"Video playback not started from initial time");
            return false;
        }
        return true;
    }

}
