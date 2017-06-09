package com.ooyala.playback.page;

import com.ooyala.playback.utils.JSScriptInjection;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static java.lang.Thread.sleep;

/**
 * Created by jitendra on 08/05/17.
 */
public class AnalyticsValidator extends PlayBackPage implements PlaybackValidator {

    public static Logger logger = Logger.getLogger(AnalyticsValidator.class);

    public AnalyticsValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    public boolean validate(String element, int timeout) throws Exception {
        return true;
    }

    public void getConsoleLogForAnalytics(){
        driver.executeScript("var oldf = console.log;\n" +
                "    var videoPlaying=1;\n" +
                "    var videoPausedReq=1;\n" +
                "    var videoPlayReq=1;\n" +
                "    var videoPaused=1;\n" +
                "    var videoPlayed=1;\n" +
                "    var videoReplayReq=1;\n" +
                "    var streamTypeUpdated=1;\n" +
                "    var videoSeekReq=1;\n" +
                "    var videoSeekCompleted=1;\n" +
                "    var adBreakStarted=1;\n" +
                "    var adBreakEnded=1;\n" +
                "    var adPodStarted=1;\n" +
                "    var adStarted=1;\n" +
                "    var adEnded=1;\n" +
                "    console.log = function() {\n" +
                "        oldf.apply(console, arguments);\n" +
                "        if(arguments[0].includes('Analytics Template: PluginID')){\n" +
                "            var s = arguments[0];\n" +
                "            if(s.includes('video_pause_requested')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_video_pause_requested_\"+videoPausedReq+\">\" + arguments[0] + \"</p>\");\n" +
                "                videoPaused;\n" +
                "            }\n" +
                "            if(s.includes('video_play_requested')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_video_play_requested_\"+videoPlayReq+\">\" + arguments[0] + \"</p>\");\n" +
                "                videoPlayReq++;\n" +
                "            }\n" +
                "            if(s.includes('video_paused')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_video_paused_\"+videoPaused+\">\" + arguments[0] + \"</p>\");\n" +
                "                videoPaused++;\n" +
                "            }\n" +
                "            if(s.includes('video_playing')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_video_playing_\"+videoPlaying+\">\" + arguments[0] + \"</p>\");\n" +
                "                videoPlaying++;\n" +
                "            }\n" +
                "            if(s.includes('video_replay_requested')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_video_replay_requested_\"+videoReplayReq+\">\" + arguments[0] + \"</p>\");\n" +
                "                videoReplayReq++;\n" +
                "            }\n" +
                "            if(s.includes('stream_type_updated')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_stream_type_updated_\"+streamTypeUpdated+\">\" + arguments[0] + \"</p>\");\n" +
                "                streamTypeUpdated++;\n" +
                "            }\n" +
                "            if(s.includes('video_seek_requested')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_video_seek_requested_\"+videoSeekReq+\">\" + arguments[0] + \"</p>\");\n" +
                "                videoSeekReq++;\n" +
                "            }\n" +
                "            if(s.includes('video_seek_completed')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_video_seek_completed_\"+videoSeekCompleted+\">\" + arguments[0] + \"</p>\");\n" +
                "                videoSeekCompleted++;\n" +
                "            }\n" +
                "            if(s.includes('ad_break_started')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_ad_break_started_\"+adBreakStarted+\">\" + arguments[0] + \"</p>\");\n" +
                "                adBreakStarted++;\n" +
                "            }\n" +
                "            if(s.includes('ad_break_ended')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_ad_break_ended_\"+adBreakEnded+\">\" + arguments[0] + \"</p>\");\n" +
                "                adBreakEnded++;\n" +
                "            }\n" +
                "            if(s.includes('ad_pod_started')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_ad_pod_started_\"+adPodStarted+\">\" + arguments[0] + \"</p>\");\n" +
                "                adPodStarted++;\n" +
                "            }\n" +
                "            if(s.includes('ad_started')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_ad_started_\"+adStarted+\">\" + arguments[0] + \"</p>\");\n" +
                "                adStarted++;\n" +
                "            }\n" +
                "            if(s.includes('ad_ended')){\n" +
                "                OO.$(\"#ooplayer\").append(\"<p id=analytics_ad_ended_\"+adEnded+\">\" + arguments[0] + \"</p>\");\n" +
                "                adEnded++;\n" +
                "            }\n" +
                "        }\n" +
                "    }");
    }
}
