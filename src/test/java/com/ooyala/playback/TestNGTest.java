package com.ooyala.playback;

import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import org.testng.annotations.*;

/**
 * Created by suraj on 6/12/17.
 */
public class TestNGTest extends PlaybackWebTest {

    public TestNGTest() throws OoyalaException {
    }

    public String getVideoPlugin(Object[] testData){
        urlObject = (UrlObject) testData[1];
        String videoPlugin =urlObject.getVideoPlugins();
        System.out.println(videoPlugin);
        return videoPlugin;
    }
}
