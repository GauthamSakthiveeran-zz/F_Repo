function subscribeToEvents() {
    window.pp.mb.subscribe('*', 'test', function(event,params) {
        var playingEventOrder = 1;
        var willPlayAdsEventOrder = 1;
        var adsPlayedEventOrder = 1;
        var showAdSkipButtonEventOrder = 1
        var videowillplayEventOrder = 1;
        var replayEventOrder = 1;
        var playing = ['FirstTime','OnReplay'];
        var playerCreatedOrder = 1;
        var willShowNonlinearAdEventOrder = 1;
        var adManagerControllerAllAdsDone =1;
        var pauseEventOrder = 1;
        var destroyEventOrder = 1;
        var count = 0;
        var willPlaySingleAdsEventOrder = 1;
        var singleadsPlayedEventOrder = 1;
        var adReplayEventOrder = 1;
        var videoPlayingEventOrder = 1;
        var videoPlayedEventOrder = 1;
        var willPauseAdsEventOrder = 1;
        var videoPausedAdsEventOrder = 1;
        var willPlayNonlinearAdEventOrder = 1;
        var videoCreatedEventOrder=1;
        var downloadingEventOrder = 1;
        var playedEventOrder = 1;
        var pausedEventOrder = 1;
        var reportDiscoveryClickEventOrder = 1;
        var reportDiscoveryImpressionEventOrder = 1;
        var focusVideoEventOrder = 1;
        var createVideoEventOrder = 1;
        var videoCreatedEventOrder = 1;
        var videoControllerSetVideoStreamsEventOrder = 1;
        var disposeVideoEventOrder = 1;
        var videoPauseEventOrder = 1;
        var videoPausedEventOrder = 1;
        var videoSeekEventOrder = 1;
        var videoSetInitialTimeEventOrder = 1;
        var videoElementDisposedEventOrder = 1;
        var adPodEndedEventOrder = 1;
        var seekingEventOrder = 1;
        var setEmbedCodeEventOrder = 1;
        var playbackReadyEventOrder = 1;
        var videoPreloadEventOrder = 1;
        var videoPlayEventOrder = 1;
        var videoWillPlayOrder = 1;
        var videoCreatedForAdsEventOrder = 1;
        var adPodStartedEventOrder = 1;
        var videoPlayingAdEventOrder = 1;
        var willPlayAdOnReplayEventOrder = 1;
        var videoInFocusOrder = 1;
        var videoLostFocusEventOrder = 1;

        return function(event) {

            if (event.match(/showNonlinearAd/)) {
                OO.$('#ooplayer').append('<p id=showNonlinearAd_'+willShowNonlinearAdEventOrder+'> showNonlinearAd '+willShowNonlinearAdEventOrder+'</p>');
                willShowNonlinearAdEventOrder++;
            }

            if (event.match(/willPlayNonlinearAd/)) {
                OO.$('#ooplayer').append('<p id=willPlayNonlinearAd_'+willPlayNonlinearAdEventOrder+'> willPlayNonlinearAd '+willPlayNonlinearAdEventOrder+'</p>');
                willPlayNonlinearAdEventOrder++;
            }

            if (event.match(/willPauseAds/)) {
                OO.$('#ooplayer').append('<p id=willPauseAds_'+willPauseAdsEventOrder+'> willPauseAds '+willPauseAdsEventOrder+'</p>');
                willPauseAdsEventOrder++;
            }

            if (event.match(/videoPaused/) && arguments[1] == 'ads') {
                OO.$('#ooplayer').append('<p id=videoPausedAds_'+videoPausedAdsEventOrder+'>videoPausedAds '+videoPausedAdsEventOrder+'</p>');
                videoPausedAdsEventOrder++;
            }

            if (event.match(/videoPlaying/) && arguments[1] == 'main') {
                OO.$('#ooplayer').append('<p id=videoPlaying_'+videoPlayingEventOrder+'>videoPlaying '+videoPlayingEventOrder+'</p>');
                videoPlayingEventOrder++;
            }

            if (event.match(/videoPlaying/) && arguments[1] == 'ads') {
                OO.$('#ooplayer').append('<p id=videoPlayingAd_'+videoPlayingAdEventOrder+'>videoPlayingAd '+videoPlayingAdEventOrder+'</p>');
                videoPlayingAdEventOrder++;
            }

            if (event.match(/playerCreated/)) {
                OO.$('#ooplayer').append('<p id=playerCreated_'+playerCreatedOrder+'>playerCreated '+arguments[1]+'</p>');
                playerCreatedOrder++;
            }

            if (event.match(/playing/)) {
                if(replayEventOrder == 2){
                    OO.$('#ooplayer').append('<p id=playing_'+playing[1]+'>playing '+playing[1]+'</p>');c
                }else{
                    OO.$('#ooplayer').append('<p id=playing_'+playingEventOrder+'>playing '+playingEventOrder+'</p>');
                    playingEventOrder++;
                    OO.$('#ooplayer').append('<p id=playing_'+playing[0]+'>playing '+playing[0]+'</p>');
                    count++;
                }

            }


            if (event.match(/willPlaySingleAd/)) {
                if(videoPlayingEventOrder == 1){
                    OO.$('#ooplayer').append('<p id= willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Preroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>');
                    OO.$('#ooplayer').append('<p id=PreRoll_willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Preroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>');
                }
                if(videoPlayingEventOrder > 1 && videoPlayedEventOrder == 1){
                    OO.$('#ooplayer').append('<p id=MidRoll_willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Midroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>');
                    OO.$('#ooplayer').append('<p id=willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Midroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>');
                }
                if(videoPlayedEventOrder == 2){
                    OO.$('#ooplayer').append('<p id=willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Postroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>');
                    OO.$('#ooplayer').append('<p id=PostRoll_willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Postroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>');
                }
                willPlaySingleAdsEventOrder++;
                OO.$('#ooplayer').append('<p id=willPlayPrerollAd'+'>willPlayPrerollAd</p>');
                OO.$('#ooplayer').append('<p id=adIsPlaying'+'>adIsPlaying</p>');
            }


            if (event.match(/willPlaySingleAd/) && replayEventOrder == 2) {
                OO.$('#ooplayer').append('<p id=willPlayAdOnReplay_'+willPlayAdOnReplayEventOrder+'>willPlayAdOnReplayEventOrder</p>');
                willPlayAdOnReplayEventOrder++;
            }


            if (event.match(/singleAdPlayed/)) {
                OO.$('#ooplayer').append('<p id=singleAdPlayed_'+singleadsPlayedEventOrder+'>singleAdPlayed '+singleadsPlayedEventOrder+'</p>');
                singleadsPlayedEventOrder++;
                showAdSkipButtonEventOrder = 1;
            }


            if (event.match(/videoWillPlay/) && arguments[1] == 'ads') {
                var bumper = '/Bumper';

                if(arguments[2].indexOf(bumper) > -1) {
                    OO.$('#ooplayer').append('<p id=BumperAd'+'>BumperAd '+arguments[2]+'</p>');
                    if(replayEventOrder == 2) {
                        OO.$('#ooplayer').append('<p id=BumperAdOnReplay'+'>BumperAdOnReplay '+arguments[2]+'</p>');
                    }
                } else{
                    OO.$('#ooplayer').append('<p id=adplayingurl_'+videowillplayEventOrder+'>Ad URL '+arguments[2]+'</p>');
                    videowillplayEventOrder++;
                }

            }

            if (event.match(/videoWillPlay/)) {
                OO.$('#ooplayer').append('<p id=videoWillPlay_'+videoWillPlayOrder
                    +'>videoWillPlay '+videoWillPlayOrder+'</p>');
                videoWillPlayOrder++;
            }

            if (event.match(/replay/)) {
                OO.$("#ooplayer").append("<p id=replay_"+replayEventOrder+">replay "+replayEventOrder+"</p>");
                replayEventOrder++;
                adReplayEventOrder++;
            }


            if (event.match(/adsClicked/)) {
                OO.$('#ooplayer').append('<p id=adsClicked_'+arguments[1]['source']+'>adsClicked'+arguments[1]['source']+'</p>');
            }
            if (event.match(/adsClickthroughOpened/)) {
                OO.$('#ooplayer').append('<p id=adsClickThroughOpened>adsClickThroughOpened</p>');
            }

            if (event.match(/showAdSkipButton/) && showAdSkipButtonEventOrder < 2 ) {
                OO.$('#ooplayer').append('<p id=showAdSkipButton_'+showAdSkipButtonEventOrder+'>showAdSkipButton '+showAdSkipButtonEventOrder+'</p>');
                showAdSkipButtonEventOrder++;
            }



            if (event.match(/adsPlayed/)) {
                OO.$('#ooplayer').append('<p id=adsPlayed_'+adsPlayedEventOrder+'>adsPlayed '+adsPlayedEventOrder+'</p>');
                OO.$('#ooplayer').append('<p id=countPoddedAds_'+adsPlayedEventOrder+'>'+(willPlaySingleAdsEventOrder-1)+'</p>');
                adsPlayedEventOrder++;
            }


            if (event.match(/adManagerControllerAllAdsDone/)) {
                OO.$('#ooplayer').append('<p id=countPoddedAds>'+(willPlaySingleAdsEventOrder-1) +'</p>');
                adManagerControllerAllAdsDone++;
            }

            if(event.match(/pause/)) {
                OO.$('#ooplayer').append('<p id=player1_pause_'+pauseEventOrder+'>pause '+pauseEventOrder+'</p>');
                OO.$('#ooplayer').append('<p id=paused'+'>Video Paused</p>');
                pauseEventOrder++;
            }

            if (event.match(/destroy/)) {
                OO.$('#ooplayer').append('<p id=destroy_'+destroyEventOrder+'>destroy '+destroyEventOrder+'</p>');
                destroyEventOrder++;
            }


            if (event.match(/willPlayAds/)) {

                if(videoPlayingEventOrder == 1 ){
                    OO.$('#ooplayer').append('<p id=PreRoll_willPlayAds'+'>PreRoll_willPlayAds '+willPlayAdsEventOrder+'</p>');
                    willPlayAdsEventOrder++;
                }

                if(videoPlayingEventOrder > 1 && videoPlayedEventOrder == 1){
                    OO.$('#ooplayer').append('<p id=MidRoll_willPlayAds_'+willPlayAdsEventOrder+'>MidRoll_willPlayAds '+willPlayAdsEventOrder+'</p>');
                    OO.$('#ooplayer').append('<p id=MidRoll_willPlayAds>MidRoll_willPlayAds '+willPlayAdsEventOrder+'</p>');
                    willPlayAdsEventOrder++;
                }
                if(videoPlayedEventOrder == 2){
                    OO.$('#ooplayer').append('<p id=PostRoll_willPlayAds'+'>PostRoll_willPlayAds '+willPlayAdsEventOrder+'</p>');
                    willPlayAdsEventOrder++;
                }

                if(adReplayEventOrder == 2){
                    OO.$('#ooplayer').append('<p id=PreRoll_willPlayAds_OnReplay'+'>PreRoll_willPlayAds_OnReplay '+willPlaySingleAdsEventOrder+'</p>');
                    adReplayEventOrder++;
                }

                else if(adReplayEventOrder == 3){
                    OO.$('#ooplayer').append('<p id=MidRoll_willPlayAds_OnReplay'+'>MidRoll_willPlayAds_OnReplay</p>');
                    adReplayEventOrder++;
                }

                else if(adReplayEventOrder == 4){
                    OO.$('#ooplayer').append('<p id=PostRoll_willPlayAds_OnReplay'+'>PostRoll_willPlayAds_OnReplay</p>');
                    adReplayEventOrder++;
                }

            }

            if (event.match(/videoPlayed/)&& arguments[1] == 'main') {
                OO.$('#ooplayer').append('<p id=videoPlayed_'+videoPlayedEventOrder+'>videoPlayed '+videoPlayedEventOrder+'</p>');
                videoPlayedEventOrder++;
            }

            if (event.match(/videoControllerVideoElementCreated/) && arguments[1].videoId == 'main') {
                OO.$('#ooplayer').append('<p id=videoCreatedForMain'+'>videoCreated for main '+'</p>');
                videoCreatedEventOrder++;
            }

            if (event.match(/downloading/) && arguments[5] == 'main'){
                var initialTime = arguments[1].toFixed();

                if ((initialTime > 0&&initialTime < 10)) {
                    OO.$('#ooplayer').append('<p id=initialTime10_false' +'>initialTime_false ' + '' +
                        '</p>');
                }
                if (!(initialTime > 0&&initialTime < 10)) {
                    if ((initialTime == 10) && !(initialTime < 10)) {
                        console.log('initialTime10_' + downloadingEventOrder);
                        OO.$('#ooplayer').append('<p id=initialTime_' + initialTime + '>initialTime ' + '' +
                            '</p>');
                    }
                }
            }

            if (event.match(/downloading/) && arguments[5] == 'main'){
                var initialTime = arguments[1].toFixed();
                if ((initialTime > 0&&initialTime < 20)) {
                    OO.$('#ooplayer').append('<p id=initialTime20_false' +'>initialTime_false ' + '' +
                        '</p>');
                }
                if (!(initialTime > 0&&initialTime < 20)) {
                    if ((initialTime == 20) && !(initialTime < 20)) {
                        console.log('initialTime10_' + downloadingEventOrder);
                        OO.$('#ooplayer').append('<p id=initialTime_' + initialTime + '>initialTime ' + '' +
                            '</p>');
                    }
                }
            }

            if (event.match(/played/)) {
                OO.$('#ooplayer').append(
                    '<p id=played_' + playedEventOrder + '>played '
                    + playedEventOrder + '</p>');
                playedEventOrder++;
            }

            if (event.match(/paused/)) {
                OO.$('#ooplayer').append(
                    '<p id=paused_' + pausedEventOrder + '>paused '
                    + pausedEventOrder + '</p>');
                pausedEventOrder++;
            }

            if (event.match(/fullscreenChanged/)) {
                OO.$('#ooplayer')
                    .append(
                        '<p id=fullscreenChanged_' + arguments[1]
                        + '>fullscreenChanged ' + arguments[1]
                        + '</p>');
            }

            if (event.match(/setClosedCaptionsLanguage/)) {
                OO.$('#ooplayer').append('<p id=cclanguage_'+ arguments[1]+'>cclanguage_'+ arguments[1]+'</p>');
                OO.$('#ooplayer').append('<p id=ccmode_'+ arguments[2].mode+'>ccmode_'+ arguments[2].mode+'</p>');
            }

            if (event.match(/reportDiscoveryClick/)) {
                OO.$("#ooplayer").append(
                    "<p id=reportDiscoveryClick_"+reportDiscoveryClickEventOrder+
                    ">reportDiscoveryClick "+reportDiscoveryClickEventOrder+"</p>");
                reportDiscoveryClickEventOrder++;
            }

            if (event.match(/videoControllerVideoElementCreated/) && arguments[1].videoId == 'ads') {
                OO.$('#ooplayer').append('<p id=videoCreatedForAds_'+videoCreatedForAdsEventOrder+'>videoCreated for ads '+'</p>');
                videoCreatedForAdsEventOrder++;
            }

            if (event.match(/reportDiscoveryImpression/)) {

                OO.$("#ooplayer").append(
                    "<p id=reportDiscoveryImpression_"+reportDiscoveryImpressionEventOrder+
                    ">reportDiscoveryClick "+reportDiscoveryImpressionEventOrder+"</p>");
                reportDiscoveryImpressionEventOrder++;
            }

            if(event.match(/videoControllerFocusVideoElement/)) {
                OO.$('#ooplayer').append(
                    '<p id=focusVideo_'+focusVideoEventOrder
                    +'>focusVideo '+focusVideoEventOrder+'</p>');
                focusVideoEventOrder++;
            }

            if (event.match(/videoControllerCreateVideoElement/)) {
                OO.$('#ooplayer').append('<p id=CreateVideo_'+createVideoEventOrder
                    +'>CreateVideo '+createVideoEventOrder+'</p>');
                createVideoEventOrder++;
            }

            if (event.match(/videoControllerVideoElementCreated/)) {
                OO.$('#ooplayer').append('<p id=videoCreated_'+videoCreatedEventOrder
                    +'>videoCreated '+videoCreatedEventOrder+'</p>');
                videoCreatedEventOrder++;
            }

            if (event.match(/videoControllerVideoElementInFocus/)) {
                OO.$('#ooplayer').append('<p id=videoInFocus_'+videoInFocusOrder
                    +'>videoInFocus '+videoInFocusOrder+'</p>');
                videoInFocusOrder++;
            }

            if (event.match(/videoControllerVideoElementLostFocus/)) {
                OO.$('#ooplayer').append('<p id=videoLostFocus_'+videoLostFocusEventOrder
                    +'>videoLostFocus '+videoLostFocusEventOrder+'</p>');
                videoLostFocusEventOrder++;
            }

            if (event.match(/videoControllerSetVideoStreams/)) {
                OO.$('#ooplayer').append('<p id=setVideoStream_'+videoControllerSetVideoStreamsEventOrder
                    +'>setVideoStream  '+videoControllerSetVideoStreamsEventOrder+'</p>');
                videoControllerSetVideoStreamsEventOrder++;
            }

            if (event.match(/videoControllerDisposeVideoElement/)) {
                OO.$('#ooplayer').append('<p id=disposeVideo_'+disposeVideoEventOrder
                    +'>disposeVideo '+disposeVideoEventOrder+'</p>');
                disposeVideoEventOrder++;
            }

            if (event.match(/videoPause/)) {
                OO.$('#ooplayer').append('<p id=videoPause_'+videoPauseEventOrder
                    +'>videoPause '+videoPauseEventOrder+'</p>');
                videoPauseEventOrder++;
            }

            if (event.match(/videoPaused/)) {
                OO.$('#ooplayer').append('<p id=videoPaused_'+videoPausedEventOrder
                    +'>videoPaused '+videoPausedEventOrder+'</p>');
                videoPausedEventOrder++;
            }

            if (event.match(/videoSeek/)) {
                OO.$('#ooplayer').append('<p id=seeked_'+videoSeekEventOrder
                    +'>videoSeek '+videoSeekEventOrder+'</p>');
                videoSeekEventOrder++;
            }

            if(event.match(/videoSetInitialTime/)) {
                OO.$('#ooplayer').append('<p id=videoSetInitialTime_'+videoSetInitialTimeEventOrder
                    +'>videoSetInitialTime '+videoSetInitialTimeEventOrder+'</p>');
                videoSetInitialTimeEventOrder++;
            }

            if (event.match(/videoControllerVideoElementDisposed/)) {
                OO.$('#ooplayer').append('<p id=videoElementDisposed_'+videoElementDisposedEventOrder
                    +'>videoElementDisposed '+videoElementDisposedEventOrder+'</p>');
                videoElementDisposedEventOrder++;
            }

            if (event.match(/adPodEnded/)) {
                OO.$("#ooplayer").append("<p id=adPodEnded_"+adPodEndedEventOrder
                    +">adPodEnded "+adPodEndedEventOrder+"</p>");
                adPodEndedEventOrder++;
            }

            if (event.match(/seeked/)) {
                OO.$('#ooplayer').append(
                    '<p id=seeked_' + seekingEventOrder + '>seeked '
                    + seekingEventOrder + '</p>');
                seekingEventOrder++;
            }

            if (event.match(/setEmbedCode/)) {
                OO.$('#ooplayer').append(
                    '<p id=setEmbedCode_' + setEmbedCodeEventOrder + '>setEmbedCode '+ setEmbedCodeEventOrder + '</p>');
                setEmbedCodeEventOrder++;
            }

            if (event.match(/playbackReady/)) {
                OO.$("#ooplayer").append("<p id=playbackReady_"+playbackReadyEventOrder+">playbackReady "+playbackReadyEventOrder+"</p>");
                playbackReadyEventOrder++;
            }

            if (event.match(/videoPreload/)) {
                OO.$("#ooplayer").append("<p id=videoPreload_"+videoPreloadEventOrder+">videoPreload "+videoPreloadEventOrder+"</p>");
                videoPreloadEventOrder++;
            }

            if (event.match(/videoPlay/)) {
                OO.$('#ooplayer').append('<p id=videoPlay_'+videoPlayEventOrder
                    +'>videoPlay '+videoPlayEventOrder+'</p>');
                videoPlayEventOrder++;
            }

            if (event.match(/adPodStarted/)) {
                var adCount = arguments[1];
                console.log("number of ads : "+adCount);
                OO.$("#ooplayer").append("<p id=adPodStarted_"+adPodStartedEventOrder+">"+arguments[1]+"</p>");
                adPodStartedEventOrder++;
            }
        };
    }());
}