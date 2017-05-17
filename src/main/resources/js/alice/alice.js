

function subscribeToEvents() {
    window.pp.mb.subscribe("*", "test", function(event, params) {
        var adsPlayedEventOrder = 1;
        var closedCaptionOrder = 1;
        var controlsShownEventOrder = 1;
        var fullscreenChangedEventOrder = 1;
        var pausedEventOrder = 1;
        var playbackReadyEventOrder = 1;
        var playedEventOrder = 1;
        var reportDiscoveryClickOrder = 1;
        var reportDiscoveryImpressionOrder = 1;
        var seekingEventOrder = 1;
        var setEmbedCodeOrder = 1;
        var videoPlayedEventOrder = 1;
        var videoPlayingEventOrder = 1;
        var videoPreloadEventOrder = 1;
        var willPauseAdsEventOrder = 1;
        var willPlayAdsEventOrder = 1;
        var closedCaptionLang = 1;
        var reportDiscoveryClickOrder = 1;
        var assetDimensionOrder = 1;
        var controlsShownEventOrder = 1;
        var replayEventOrder = 1;
        var videoPlayingEventOrder = 1;
        var willPlayAdsEventOrder = 1;
        var playingEventOrder = 1;
        var bitrateChangedEventOrder = 1;
        var destroyEventOrder = 1;
        var adPodEndedEventOrder = 1;
        var skipAdEventOrder = 1;
        var bufferingEventCount=1;
        var savePlayerSettingsFalseEventCount = 1;
        var savePlayerSettingsTrueEventCount = 1;


        return function(event) {

            if (event.match(/playing/)) {
                OO.$("#ooplayer").append("<p id=playing_" + playingEventOrder + ">playing "+
                playingEventOrder + "</p>");
                playingEventOrder++;
            }

            if (event.match(/savePlayerSettings/) && arguments[1].closedCaptionOptions.enabled == false && savePlayerSettingsFalseEventCount<2) {
                OO.$("#ooplayer").append("<p id=savePlayerSettings_off_"+savePlayerSettingsFalseEventCount+">"+savePlayerSettingsFalseEventCount+"</p>");
                savePlayerSettingsFalseEventCount++;
            }

            if (event.match(/savePlayerSettings/) && arguments[1].closedCaptionOptions.enabled == true && savePlayerSettingsTrueEventCount<2) {
                OO.$("#ooplayer").append("<p id=savePlayerSettings_on_"+savePlayerSettingsTrueEventCount+">"+savePlayerSettingsTrueEventCount+"</p>");
                savePlayerSettingsTrueEventCount++;
            }

            if (event.match(/played/)) {
                OO.$("#ooplayer").append("<p id=played_" + playedEventOrder + ">played "+
                playedEventOrder + "</p>");
                playedEventOrder++;
            }
            if (event.match(/fullscreenChanged/)) {
                OO.$("#ooplayer")
                    .append("<p id=fullscreenChanged_" + arguments[1]+
                    ">fullscreenChanged " + arguments[1]+
                    "</p>");
                fullscreenChangedEventOrder++;
            }

            if (event.match(/seeked/)) {
                OO.$("#ooplayer").append("<p id=seeked_" + seekingEventOrder + ">seeked " +
                seekingEventOrder + "</p>");
                seekingEventOrder++;
            }

            if (event.match(/skipAd/)) {
                OO.$("#ooplayer").append("<p id=skipAd_" + skipAdEventOrder + ">skipAd " +
                    skipAdEventOrder + "</p>");
                skipAdEventOrder++;
            }

            if (event.match(/videoPlaying/) && arguments[1] == "main") {

                OO.$("#ooplayer").append("<p id=videoPlaying_"
                +videoPlayingEventOrder+">playing "+videoPlayingEventOrder+"</p>");
                videoPlayingEventOrder++;

            }
            if(event.match(/playbackReady/)) {
                OO.$("#ooplayer").append("<p id=playbackReady_"+
                playbackReadyEventOrder+">playbackReady "+playbackReadyEventOrder+"</p>");
                playbackReadyEventOrder++;
            }

            if (event.match(/played/)) {
  				OO.$("#ooplayer").append("<p id=played_"+playedEventOrder+">played"+playedEventOrder+"</p>");
                playedEventOrder++;
            }

            if (event.match(/videoPlayed/)) {
                OO.$("#ooplayer").append("<p id=videoPlayed_"+videoPlayedEventOrder+">videoPlayed"+videoPlayedEventOrder+"</p>");
                videoPlayedEventOrder++;
            }
            if (event.match(/paused/)) {
                OO.$("#ooplayer").append("<p id=paused_"+pausedEventOrder+">paused "+pausedEventOrder+"</p>");
                pausedEventOrder++;
            }

            if (event.match(/willPlaySingleAd/)) {
                OO.$("#ooplayer").append("<p id=willPlaySingleAd_"
                +willPlayAdsEventOrder+">Preroll willPlaySingleAd "+willPlayAdsEventOrder+"</p>");
                willPlayAdsEventOrder++;
			}

            if (event.match(/controlsShown/)) {

                OO.$("#ooplayer").append("<p id=controlsShown_"+controlsShownEventOrder+">controlsShown "+controlsShownEventOrder+"</p>");
                controlsShownEventOrder++;
            }

            if (event.match(/singleAdPlayed/)) {

                OO.$("#ooplayer").append("<p id=singleAdPlayed_"+adsPlayedEventOrder+">singleAdPlayed "+adsPlayedEventOrder+" </p>");
                adsPlayedEventOrder++;
            }

            if (event.match(/closedCaptionCueChanged/)) {
                OO.$("#ooplayer").append("<p id=ccshowing_"+closedCaptionOrder+">ccshown "+closedCaptionOrder+"</p>");
                closedCaptionOrder++;
            }

            if (event.match(/setClosedCaptionsLanguage/)) {
                OO.$("#ooplayer").append("<p id=cclanguage_"+ arguments[1]+">cclanguage_"+ arguments[1]+"</p>");
				OO.$("#ooplayer").append("<p id=ccmode_"+ arguments[2].mode+">ccmode_"+ arguments[2].mode+"</p>");
			}

            if (event.match(/reportDiscoveryImpression/)) {

                OO.$("#ooplayer").append("<p id=reportDiscoveryImpression_"+reportDiscoveryImpressionOrder+
                ">reportDiscoveryImpression "+reportDiscoveryImpressionOrder+"</p>");
                reportDiscoveryImpressionOrder++;
            }

            if (event.match(/reportDiscoveryClick/)) {

                OO.$("#ooplayer").append("<p id=reportDiscoveryClick_"+reportDiscoveryClickOrder+
                ">reportDiscoveryClick "+reportDiscoveryClickOrder+"</p>");
                reportDiscoveryClickOrder++;
            }

            if(event.match(/setEmbedCode/)) {
                OO.$("#ooplayer").append("<p id=setEmbedCode_"+setEmbedCodeOrder+">setEmbedCode "+setEmbedCodeOrder+"</p>");
                setEmbedCodeOrder++;
            }


            if(event.match(/videoPreload/)) {
                OO.$("#ooplayer").append("<p id=videoPreload_"+videoPreloadEventOrder+">videoPreload "+videoPreloadEventOrder+"</p>");
                videoPreloadEventOrder++;
            }

            if (event.match(/replay/)) {
                OO.$("#ooplayer").append("<p id=replay_"+replayEventOrder+">replay "+replayEventOrder+" </p>");
                replayEventOrder++;
			}

            if (event.match(/willPlayAds/)) {
                OO.$("#ooplayer").append("<p id=willPlayAds_"+willPlayAdsEventOrder+">willPlayAds "+willPlayAdsEventOrder+"</p>");
                willPlayAdsEventOrder++;
            }


            if (event.match(/sendClickEvent/)) {
                OO.$("#ooplayer").append("<p id=sendClickEvent_"+sendClickEventOrder+">sendClickEvent "+sendClickEventOrder+"</p>");
                sendClickEventOrder++;
			}

            if (event.match(/videoWillPlay/) && arguments[1] == "ads") {
                OO.$("#ooplayer").append("<p id=adplayingurl_"+videowillplayEventOrder+">Ad URL "+arguments[2]+"</p>");
                videowillplayEventOrder++;
            }

            if (event.match(/adsPlayed/)) {
                OO.$("#ooplayer").append("<p id=adsPlayed_"+adsPlayedEventOrder+">adsPlayed "+adsPlayedEventOrder+" </p>");
                adsPlayedEventOrder++;
            }

            if (event.match(/assetDimension/)) {
                OO.$("#ooplayer").append("<p id=assetDimension_"+assetDimensionOrder+
                "  width="+arguments[1].width +
                " height="+arguments[1].height+">assetDimension</p>");
                assetDimensionOrder++;
            }
            if (event.match(/bitrateChanged/)) {
                OO.$("#ooplayer").append("<p id=bitrateChanged_"+arguments[1]['bitrate']+">bitrateChanged "+bitrateChangedEventOrder+"</p>");
                bitrateChangedEventOrder++;
            }
			
			if (event.match(/destroy/)) {
				OO.$('#ooplayer').append('<p id=destroy_'+destroyEventOrder+'>destroy '+destroyEventOrder+'</p>'); 
				destroyEventOrder++;
			}
            if (event.match(/adPodEnded/)) {
                OO.$("#ooplayer").append("<p id=adPodEnded_" + adPodEndedEventOrder + ">adPodEnded " + adPodEndedEventOrder + "</p>");
                adPodEndedEventOrder++;
            }
            
            if(event.match(/videoControllerCreateVideoElement/)){
            	OO.$('#ooplayer').append('<p id = drm_tag >' + JSON.stringify(arguments[2]) + '</p>');
            }

            if (event.match(/videoWillPlay/) && arguments[1] == "main") {
                OO.$("#ooplayer").append("<p id=videoPlayingurl"+">"+arguments[2]+"</p>");
            }

            if (event.match(/buffering/)) {
                OO.$('#ooplayer').append('<p id=buffering_' + bufferingEventCount + '>buffering_'+ bufferingEventCount + '</p>');
                      bufferingEventCount++;
            }
        };
    }());
}