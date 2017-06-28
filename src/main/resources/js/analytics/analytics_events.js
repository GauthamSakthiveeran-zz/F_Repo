
(function() {var oldf = console.log;
    var videoPlaying=1;
    var videoPausedReq=1;
    var videoPlayReq=1;
    var videoPaused=1;
    var videoPlayed=1;
    var videoReplayReq=1;
    var streamTypeUpdated=1;
    var videoSeekReq=1;
    var videoSeekCompleted=1;
    var adBreakStarted=1;
    var adBreakEnded=1;
    var adPodStarted=1;
    var adStarted=1;
    var adEnded=1;
    var fullscreenChanged=1;
    var playbackCompleted=1;
    var volumeChanged=1;
    var bufferingStarted=1;
    var seekCompleted=1;
    var volumeChanged=1;
    var fullscreenChanged=1;
    var intitalPlayback = 1;
    var adSkipped = 1;

    console.log = function() {
        oldf.apply(console, arguments);
        if(arguments[0].includes('Analytics Template: PluginID')){
            var s = arguments[0];
            if(s.includes('video_pause_requested')){
                OO.$("#ooplayer").append("<p id=analytics_video_requested_paused_"+videoPausedReq+">" + arguments[0] + "</p>");
                videoPaused;
            }
            if(s.includes('video_play_requested')){
                OO.$("#ooplayer").append("<p id=analytics_video_requested_playing_"+videoPlayReq+">" + arguments[0] + "</p>");
                videoPlayReq++;
            }
            if(s.includes('video_paused')){
                OO.$("#ooplayer").append("<p id=analytics_video_paused_"+videoPaused+">" + arguments[0] + "</p>");
                videoPaused++;
            }
            if(s.includes('video_playing')){
                OO.$("#ooplayer").append("<p id=analytics_video_playing_"+videoPlaying+">" + arguments[0] + "</p>");
                videoPlaying++;
            }
            if(s.includes('video_replay_requested')){
                OO.$("#ooplayer").append("<p id=analytics_video_requested_replay_"+videoReplayReq+">" + arguments[0] + "</p>");
                videoReplayReq++;
            }
            if(s.includes('stream_type_updated')){
                OO.$("#ooplayer").append("<p id=analytics_stream_type_updated_"+streamTypeUpdated+">" + arguments[0] + "</p>");
                streamTypeUpdated++;
            }
            if(s.includes('video_seek_requested')){
                OO.$("#ooplayer").append("<p id=analytics_video_requested_seeked_"+videoSeekReq+">" + arguments[0] + "</p>");
                videoSeekReq++;
            }
            if(s.includes('video_seek_completed')){
                OO.$("#ooplayer").append("<p id=analytics_video_seeked_"+videoSeekCompleted+">" + arguments[0] + "</p>");
                videoSeekCompleted++;
            }
            if(s.includes('ad_break_started')){
                OO.$("#ooplayer").append("<p id=analytics_ad_break_started_"+adBreakStarted+">" + arguments[0] + "</p>");
                adBreakStarted++;
            }
            if(s.includes('ad_break_ended')){
                OO.$("#ooplayer").append("<p id=analytics_ad_break_ended_"+adBreakEnded+">" + arguments[0] + "</p>");
                adBreakEnded++;
            }
            if(s.includes('ad_pod_started')){
                OO.$("#ooplayer").append("<p id=analytics_ad_pod_started_"+adPodStarted+">" + arguments[0] + "</p>");
                adPodStarted++;
            }
            if(s.includes('ad_started')){
                OO.$("#ooplayer").append("<p id=analytics_ad_started_"+adStarted+">" + arguments[0] + "</p>");
                adStarted++;
            }
            if(s.includes('ad_ended')){
                OO.$("#ooplayer").append("<p id=analytics_ad_ended_"+adEnded+">" + arguments[0] + "</p>");
                adEnded++;
            }
            if(s.includes('fullscreen_changed')){
                OO.$("#ooplayer").append("<p id=analytics_fullscreen_changed_"+fullscreenChanged+">" + arguments[0] + "</p>");
                fullscreenChanged++;
            }
            if(s.includes('playback_completed')){
                OO.$("#ooplayer").append("<p id=analytics_playback_completed_"+playbackCompleted+">" + arguments[0] + "</p>");
                playbackCompleted++;
            }
            if(s.includes('volume_changed')){
                OO.$("#ooplayer").append("<p id=analytics_volume_changed_"+volumeChanged+">" + arguments[0] + "</p>");
                volumeChanged++;
            }
            if(s.includes('video_buffering_started')){
                OO.$("#ooplayer").append("<p id=analytics_video_buffering_started_"+bufferingStarted+">" + arguments[0] + "</p>");
                bufferingStarted++;
            }
            if(s.includes('video_seek_completed')){
                OO.$("#ooplayer").append("<p id=analytics_video_seek_completed_"+seekCompleted+">" + arguments[0] + "</p>");
                seekCompleted++;
            }
            if(s.includes('volume_changed')){
                OO.$("#ooplayer").append("<p id=analytics_volume_changed_"+volumeChanged+">" + arguments[0] + "</p>");
                volumeChanged++;
            }
            if(s.includes('fullscreen_changed')){
                OO.$("#ooplayer").append("<p id=analytics_fullscreen_changed_"+fullscreenChanged+">" + arguments[0] + "</p>");
                fullscreenChanged++;
            }
            if(s.includes('initial_playback_requested')){
                OO.$("#ooplayer").append("<p id=analytics_initial_playback_requested_"+intitalPlayback+">" + arguments[0] + "</p>");
                intitalPlayback++;
            }
            if(s.includes('ad_skipped')){
                 OO.$("#ooplayer").append("<p id=analytics_ad_skipped_"+adSkipped+">" + arguments[0] + "</p>");
                 adSkipped++;
            }
        }
    }
})();