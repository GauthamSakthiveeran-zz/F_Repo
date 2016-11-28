package com.ooyala.playback.VTC;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EncodingValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.qe.common.exception.OoyalaException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.net.URLDecoder.decode;

/**
 * Created by jitendra on 28/11/16.
 */
public class PlaybackOverrideEncodingPriorityTests extends PlaybackWebTest {

    private PlayValidator play;
    private PlayAction playAction;
    private SeekValidator seek;
    private EventValidator event;
    private EncodingValidator encode;

    public PlaybackOverrideEncodingPriorityTests() throws OoyalaException{
        super();
    }

    @Test(groups = "Playback", dataProvider = "testUrls")
    public void testOverrideEncodingPriorities(String testName , String url){

        logger.info("Test url for "+testName+" is : \n"+url);

        boolean result = false;

        try {

            driver.get(url);

            play.waitForPage();

            Thread.sleep(10000);

            injectScript();

            encode.setTestUrl(url);

            encode.validate("validate_default_encoding",50);

            playAction.startAction();

            loadingSpinner();


            event.validate("singleAdPlayed_1", 160);

            boolean isAdPlaying = false;

            isAdPlaying = (Boolean) ((JavascriptExecutor) driver).executeScript("return pp.isAdPlaying();");

            if (isAdPlaying) {
                ((JavascriptExecutor) driver).executeScript("return pp.skipAd();");
                isAdPlaying = false;
            }

            loadingSpinner();

            seek.validate("seeked_1",20);

            event.validate("videoPlayed_1",20);

            String param = "{\"freewheel-ads-manager\":{\"fw_video_asset_id\":\"Q5MXg2bzq0UAXXMjLIFWio_6U0Jcfk6v\",\"html5_ad_server\":\"http://g1.v.fwmrm.net\",\"html5_player_profile\":\"90750:ooyala_html5\",\"fw_mrm_network_id\":\"380912\",\"showInAdControlBar\":true},\"initialTime\":0,\"autoplay\":false,\"encodingPriority\":[\"hls\",\"webm\",\"mp4\",\"dash\"]}";

            encode.setTestUrl(encode.getNewUrl(param,browser));

            Thread.sleep(10000);

            injectScript();

            encode.validate("Override",60);

            playAction.startAction();

            loadingSpinner();

            isAdPlaying = (Boolean) ((JavascriptExecutor) driver).executeScript("return pp.isAdPlaying();");

            if (isAdPlaying){
                ((JavascriptExecutor) driver).executeScript("return pp.skipAd();");
            }

            loadingSpinner();

            seek.validate("seeked_1",40);

            event.validate("videoPlayed_1",40);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
