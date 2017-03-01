function subscribeToEvents() {
    window.pp.mb.subscribe('*', 'test', function(event, params) {
        var playingEventOrder = 1;
        var playedEventOrder = 1;
        var seekingEventOrder = 1;
        var fullscreenChangedEventOrder = 1;
        var reportDiscoveryClickOrder = 1;
        var pausedEventOrder = 1;
        var adPodEndedEventOrder = 1;
        var focusVideoOrder = 1;
        var createVideoOrder = 1;
        var videoCreatedOrder = 1;
        var videoInFocusOrder = 1;
        var videoLostFocusOrder = 1;
        var disposeVideoOrder = 1;
        var videoElementDisposedOrder = 1;
        var videoSetInitialTimeOrder = 1;
        var videoPlayOrder = 1;
        var videoWillPlayOrder = 1;
        var videoPlayingOrder = 1;
        var videoPauseOrder = 1;
        var videoPlayedOrder = 1;
        var videoPausedOrder = 1;
        var videoSeekOrder = 1;
        var adPlayedOrder = 1;
        var willPlaySingleAdOrder = 1;
        var setEmbedCodeEventOrder = 1;
        var replayEventOrder = 1;
        var reportDiscoveryImpressionOrder = 1;
        var playbackReadyEventOrder = 1;
        var videoPreloadEventOrder = 1;
        var downloadingEventOrder = 1;

        return function(event) {
            if (event.match(/playing/)) {
                OO.$('#ooplayer').append(
                    '<p id=playing_' + playingEventOrder + '>playing '
                    + playingEventOrder + '</p>');
                playingEventOrder++;
            }

            if (event.match(/willPlaySingleAd/)) {
                OO.$('#ooplayer').append(
                    '<p id=willPlaySingleAd_' + willPlaySingleAdOrder + '>willAdPlay '
                    + willPlaySingleAdOrder + '</p>');
                willPlaySingleAdOrder++;
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

            if (event.match(/singleAdPlayed/)) {
                OO.$('#ooplayer').append(
                    '<p id=adsPlayed_' + adPlayedOrder + '>adPlayed '
                    + adPlayedOrder + '</p>');
                adPlayedOrder++;
            }

            if (event.match(/fullscreenChanged/)) {
                OO.$('#ooplayer')
                    .append(
                        '<p id=fullscreenChanged_' + arguments[1]
                        + '>fullscreenChanged ' + arguments[1]
                        + '</p>');
                fullscreenChangedEventOrder++;
            }

            if (event.match(/setClosedCaptionsLanguage/)) {
                OO.$('#ooplayer').append('<p id=cclanguage_'+ arguments[1]+'>cclanguage_'+ arguments[1]+'</p>');
                OO.$('#ooplayer').append('<p id=ccmode_'+ arguments[2].mode+'>ccmode_'+ arguments[2].mode+'</p>');
            }


            if (event.match(/reportDiscoveryClick/)) {

                OO.$("#ooplayer").append(
                    "<p id=reportDiscoveryClick_"+reportDiscoveryClickOrder+
                    ">reportDiscoveryClick "+reportDiscoveryClickOrder+"</p>");
                reportDiscoveryClickOrder++;
            }

            if (event.match(/reportDiscoveryImpression/)) {

                OO.$("#ooplayer").append(
                    "<p id=reportDiscoveryImpression_"+reportDiscoveryImpressionOrder+
                    ">reportDiscoveryClick "+reportDiscoveryImpressionOrder+"</p>");
                reportDiscoveryImpressionOrder++;
            }

            if(event.match(/videoControllerFocusVideoElement/)) {
                OO.$('#ooplayer').append(
                    '<p id=focusVideo_'+focusVideoOrder
                    +'>focusVideo '+focusVideoOrder+'</p>');
                focusVideoOrder++;
            }

            if (event.match(/videoControllerCreateVideoElement/)) {
                OO.$('#ooplayer').append('<p id=CreateVideo_'+createVideoOrder
                    +'>CreateVideo '+createVideoOrder+'</p>');
                createVideoOrder++;
            }

            if (event.match(/videoControllerVideoElementCreated/)) {
                OO.$('#ooplayer').append('<p id=videoCreated_'+videoCreatedOrder
                    +'>videoCreated '+videoCreatedOrder+'</p>');
                videoCreatedOrder++;
            }

            if (event.match(/videoControllerVideoElementInFocus/)) {
                OO.$('#ooplayer').append('<p id=videoInFocus_'+videoInFocusOrder
                    +'>videoInFocus '+videoInFocusOrder+'</p>');
                videoInFocusOrder++;
            }

            if (event.match(/videoControllerVideoElementLostFocus/)) {
                OO.$('#ooplayer').append('<p id=videoLostFocus_'+videoLostFocusOrder
                    +'>videoLostFocus '+videoLostFocusOrder+'</p>');
                videoLostFocusOrder++;
            }

            if (event.match(/videoControllerDisposeVideoElement/)) {
                OO.$('#ooplayer').append('<p id=disposeVideo_'+disposeVideoOrder
                    +'>disposeVideo '+disposeVideoOrder+'</p>');
                disposeVideoOrder++;
            }

            if (event.match(/videoPlay/)) {
                OO.$('#ooplayer').append('<p id=videoPlay_'+videoPlayOrder
                    +'>videoPlay '+videoPlayOrder+'</p>');
                videoPlayOrder++;
            }

            if (event.match(/videoWillPlay/)) {
                OO.$('#ooplayer').append('<p id=videoWillPlay_'+videoWillPlayOrder
                    +'>videoWillPlay '+videoWillPlayOrder+'</p>');
                videoWillPlayOrder++;
            }

            if (event.match(/videoPlaying/)) {
                OO.$('#ooplayer').append('<p id=videoPlaying_'+videoPlayingOrder
                    +'>videoPlaying '+videoPlayingOrder+'</p>');
                videoPlayingOrder++;
            }

            if (event.match(/videoPlayed/)) {
                OO.$('#ooplayer').append('<p id=videoPlayed_'+videoPlayedOrder
                    +'>videoPlayed '+videoPlayedOrder+'</p>');
                videoPlayedOrder++;
            }

            if (event.match(/videoPause/)) {
                OO.$('#ooplayer').append('<p id=videoPause_'+videoPauseOrder
                    +'>videoPause '+videoPauseOrder+'</p>');
                videoPauseOrder++;
            }

            if (event.match(/videoPaused/)) {
                OO.$('#ooplayer').append('<p id=videoPaused_'+videoPausedOrder
                    +'>videoPaused '+videoPausedOrder+'</p>');
                videoPausedOrder++;
            }

            if (event.match(/videoSeek/)) {
                OO.$('#ooplayer').append('<p id=seeked_'+videoSeekOrder
                    +'>videoSeek '+videoSeekOrder+'</p>');
                videoSeekOrder++;
            }

            if(event.match(/videoSetInitialTime/)) {
                OO.$('#ooplayer').append('<p id=videoSetInitialTime_'+videoSetInitialTimeOrder
                    +'>videoSetInitialTime '+videoSetInitialTimeOrder+'</p>');
                videoSetInitialTimeOrder++;
            }

            if (event.match(/videoControllerVideoElementDisposed/)) {
                OO.$('#ooplayer').append('<p id=videoElementDisposed_'+videoElementDisposedOrder
                    +'>videoElementDisposed '+videoElementDisposedOrder+'</p>');
                videoElementDisposedOrder++;
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

            if (event.match(/replay/)) {
                 OO.$("#ooplayer").append("<p id=replay_"+replayEventOrder+">replay "+replayEventOrder+"</p>");
                 replayEventOrder++;
            }

            if (event.match(/playbackReady/)) {
                 OO.$("#ooplayer").append("<p id=playbackReady_"+playbackReadyEventOrder+">playbackReady "+playbackReadyEventOrder+"</p>");
                 playbackReadyEventOrder++;
            }

             if (event.match(/videoPreload/)) {
                 OO.$("#ooplayer").append("<p id=videoPreload_"+videoPreloadEventOrder+">videoPreload "+videoPreloadEventOrder+"</p>");
                 videoPreloadEventOrder++;
             }
        };
    }());
}