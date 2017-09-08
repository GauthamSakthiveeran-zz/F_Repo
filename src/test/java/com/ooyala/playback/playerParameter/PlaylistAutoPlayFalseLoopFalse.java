package com.ooyala.playback.playerParameter;


import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.StreamValidator;
import com.ooyala.playback.page.VolumeValidator;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlaylistValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;


public class PlaylistAutoPlayFalseLoopFalse extends PlaybackWebTest {

    private PlaylistValidator playlist;
    private PlayValidator play;
    private StreamValidator streamTypeValidator;
    private EventValidator eventValidator;
    private VolumeValidator volumeValidator;
    private static final Logger logger = Logger.getLogger(PlaylistAutoPlayFalseLoopFalse.class);
    public PlaylistAutoPlayFalseLoopFalse() throws OoyalaException {
        super();
    }

    @Test(groups = "autoplay", dataProvider = "testUrls")
    public void testPlaylistTests(String description, UrlObject url) throws OoyalaException {
        //seperating the tab name from the test description
        String[] parts = description.split(":")[1].trim().split(",");
        String[] tcName = null;
        String[] autoplayValidator = null;
        String[] loopValidator = null;
        for(int i=0;i<parts.length-1;i++) {
    	      tcName=parts[i].split("-");
    	      if(tcName[i].contains("Auto") || tcName[i].contains("auto")) {
    	    	    autoplayValidator=tcName;
    	      } else {
    	      	loopValidator=tcName;
    	      }
       }

       String videoPluginName = parts[parts.length - 1].trim();
       
       driver.get(url.getUrl());
       boolean result = true;
        try {
        	driver.get(url.getUrl());
            if (!(description.contains("Autoplay-true"))) {
                result = result && play.waitForPage();
                injectScript();
            } else {
                result = result && playlist.isPageLoaded();
                injectScript();
            }
           result = result && playlist.playlistValidator(autoplayValidator[0], autoplayValidator[1], videoPluginName);
           result = result && play.validate("playing_1", 20000);
           result = result && volumeValidator.validateInitialVolume(0.5);
           result = result && playlist.playlistValidator(loopValidator[0], loopValidator[1], videoPluginName);

       

        } catch (Exception e) {
            extentTest.log(LogStatus.FAIL, e.getMessage());
            logger.error(e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Autoplay Playlist tests failed" + description);
    }
}
