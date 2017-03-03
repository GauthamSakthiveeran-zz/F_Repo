package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PoddedAdValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by snehal on 03/03/17.
 */
public class PlaybackSetEmbedCode extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackSetEmbedCode.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private SeekValidator seek;
    private PoddedAdValidator poddedAdValidator;
    private PlayAction playAction;

    public PlaybackSetEmbedCode() throws OoyalaException {
        super();
    }

    @Test(groups = {"Playback"}, dataProvider = "testUrls")
    public void verifySetEmebedCodeFunctionality(String testName, String url) throws OoyalaException {

        String[] parts= testName.split(":");
        String tcName = parts[1].trim();
        String adType = parts[2].trim();
        String embedCode = parts[3].trim();

        boolean result = true;

        try {
            driver.get(url);

            result = result && play.waitForPage();

            injectScript();

            result = result && playAction.startAction();

            if(!tcName.contains("Preroll")){
                result = result && seek.validate("seeked_1",20000);
            }

            result = result && eventValidator.validate("adsPlayed_1", 60000);

            if(tcName.contains("Podded")){
                result = result && poddedAdValidator.setPosition(adType).validate("countPoddedAds_1", 60000);
            }

            ((JavascriptExecutor) driver).executeScript("pp.setEmbedCode('"+embedCode+"')");

            result = result && eventValidator.validate("setEmbedCode_1", 60000);

            result = result && eventValidator.validate("videoElementDisposed_1", 60000);

            if(result){
                String newAssetEmbedCode = ((JavascriptExecutor) driver)
                        .executeScript("return pp.getEmbedCode()").toString();

                if(embedCode.equals(newAssetEmbedCode)){
                    logger.info("New aseet is loadded properly");
                }
                else {
                    result = false;
                    logger.error("New aseet is not loadded properly");
                    extentTest.log(LogStatus.FAIL, "New asset is not loaded");
                }
            }

        }catch (Exception e) {
            logger.error(e);
            result = false;
            extentTest.log(LogStatus.FAIL, "Set Embed Code Test failed", e);
        }
        Assert.assertTrue(result, "Set Embed Code Test failed");
    }
}
