function subscribeToCommonEvents() {
	window.pp.mb.subscribe('*', 'test', function(event, params) {
		var playedEventOrder = 1;
		var fullscreenChangedEventOrder = 1;
		var seekingEventOrder = 1;
        
		var playbackReadyEventOrder = 1;
		var adsclickedEventOrder = 1;
		var closedCaptionOrder = 1;
		var closedCaptionLang = 1;
		var reportDiscoveryImpressionOrder = 1;
		var reportDiscoveryClickOrder = 1;
		var setEmbedCodeOrder =1;
		var videoPreloadEventOrder =1;
		var replayEventOrder = 1;
		var sendClickEventOrder = 1;
		var skipAdEventOrder = 1;
		var nonLinearAdPlayedEventOrder = 1;
		var adPodEndedEventOrder = 1;
		var pausedEventOrder = 1;
		var videoElementDisposedEventOrder = 1;
		var savePlayerSettingsFalseEventCount = 1;
        var savePlayerSettingsTrueEventCount = 1;
        var videoElementLostFocusEventOrder = 1;
        var ooyalaAdsEventOrder = 1;
        var volumeChanged=1;
        var changeVolume=1;
        
        var logger = '';
        
		return function(event) {
			
			logger = 'QE Preformance log: event: ' + event;
			
			if (event.match(/played/)) {
				OO.$('#ooplayer').append('<p id=played_'+playedEventOrder+'>played '+playedEventOrder+'</p>'); 
				playedEventOrder++;
			}
			
			if(event.match(/paused/)) {
				OO.$('#ooplayer').append('<p id=paused_'+pausedEventOrder+'>paused '+pausedEventOrder+'</p>'); 
				pausedEventOrder++;
			}
			
			if (event.match(/fullscreenChanged/)) {
				OO.$('#ooplayer').append('<p id=fullscreenChanged_'+arguments[1]+'>fullscreenChanged '+arguments[1]+'</p>'); 
				fullscreenChangedEventOrder++;
			}
			
			if (event.match(/seeked/)) {
				OO.$('#ooplayer').append('<p id=seeked_' + seekingEventOrder + '>seeked ' +
								 seekingEventOrder + '</p>');
				seekingEventOrder++;
			}
			
			if(event.match(/playbackReady/)) {
				OO.$('#ooplayer').append('<p id=playbackReady_'+playbackReadyEventOrder+'>playbackReady '+playbackReadyEventOrder+'</p>'); 
				playbackReadyEventOrder++;
			}
			if (event.match(/adsClicked/)) {
				OO.$('#ooplayer').append('<p id=adsClicked_'+adsclickedEventOrder+'>adsClicked'+adsclickedEventOrder+'</p>'); 
				adsclickedEventOrder++;
			}
			
			if (event.match(/closedCaptionCueChanged/)) {
				OO.$('#ooplayer').append('<p id=ccshowing_'+closedCaptionOrder+'>ccshown '+closedCaptionOrder+'</p>'); 
				closedCaptionOrder++;
			}
			
			if (event.match(/setClosedCaptionsLanguage/)) {
				OO.$('#ooplayer').append('<p id=cclanguage_'+ arguments[1]+'>cclanguage_'+ arguments[1]+'</p>');
				OO.$('#ooplayer').append('<p id=ccmode_'+ arguments[2].mode+'>ccmode_'+ arguments[2].mode+'</p>');
				if(playingEventOrder==2) {
					var time = pp.getPlayheadTime();
					OO.$('#ooplayer').append('<p id=playTime>'+time+'</p>');
				}
				
				if(playingEventOrder==3) {
					var time = pp.getPlayheadTime();
					OO.$('#ooplayer').append('<p id=midroll_playTime>'+time+'</p>');
				}
			}
			if (event.match(/reportDiscoveryImpression/)) {
				OO.$('#ooplayer').append('<p id=reportDiscoveryImpression_'+reportDiscoveryImpressionOrder+'>reportDiscoveryImpression '+reportDiscoveryImpressionOrder+'</p>'); 
				reportDiscoveryImpressionOrder++;
			}
			if (event.match(/reportDiscoveryClick/)) {
				OO.$('#ooplayer').append('<p id=reportDiscoveryClick_'+reportDiscoveryClickOrder+'>reportDiscoveryClick '+reportDiscoveryClickOrder+'</p>'); 
				reportDiscoveryClickOrder++;
			}
			
			if(event.match(/setEmbedCode/)) {
				OO.$('#ooplayer').append('<p id=setEmbedCode_'+setEmbedCodeOrder+'>setEmbedCode '+setEmbedCodeOrder+'</p>'); 
				setEmbedCodeOrder++;
			}
			
			if(event.match(/videoPreload/)) {
				OO.$('#ooplayer').append('<p id=videoPreload_'+videoPreloadEventOrder+'>videoPreload '+videoPreloadEventOrder+'</p>'); 
				videoPreloadEventOrder++;
			}
			

			if (event.match(/replay/)) { 
				OO.$('#ooplayer').append('<p id=replay_'+replayEventOrder+'>replay '+replayEventOrder+' </p>'); 
				replayEventOrder++;
			}
			
			if (event.match(/sendClickEvent/)) {
				OO.$('#ooplayer').append('<p id=sendClickEvent_'+sendClickEventOrder+'>sendClickEvent '+sendClickEventOrder+'</p>'); 
				sendClickEventOrder++;
			}
			
			if (event.match(/videoSetInitialTime/)) {
				OO.$('#ooplayer').append('<p id=InitialTime_'+arguments[2]+'>InitialTime '+ arguments[2] +'</p>');
				OO.$('#ooplayer').append('<p id=VideoInitialTime>'+ arguments[2] +'</p>');
			}
			
			if (event.match(/skipAd/)) {
				OO.$('#ooplayer').append('<p id=skipAd_'+skipAdEventOrder+'>skipAd '+skipAdEventOrder+'</p>'); 
				skipAdEventOrder++;
			}
			
			
			if (event.match(/nonlinearAdPlayed/)) {
				OO.$('#ooplayer').append('<p id=nonlinearAdPlayed_'+nonLinearAdPlayedEventOrder+'>nonlinearAdPlayed '+nonLinearAdPlayedEventOrder+' </p>'); 
				nonLinearAdPlayedEventOrder++;
			}
			
			if (event.match(/adPodEnded/)) { 
				OO.$('#ooplayer').append('<p id=adPodEnded_'+adPodEndedEventOrder+'>adPodEnded '+adPodEndedEventOrder+'</p>'); 
				OO.$('#ooplayer').append('<p id=adPodEnd_'+arguments[1]+'_'+adPodEndedEventOrder+'>adPodPlayed '+arguments[1]+' '+adPodEndedEventOrder+'  </p>');
				OO.$('#ooplayer').append('<p id=admanager_details_'+adPodEndedEventOrder+'>'+arguments[1]+'</p>');
				adPodEndedEventOrder++;
				if (arguments[1]=='ooyala-ads-manager_0') {
					OO.$('#ooplayer').append('<p id=ooyalaAds_'+ooyalaAdsEventOrder+'>ooyalaAds '+ooyalaAdsEventOrder+'</p>');
					ooyalaAdsEventOrder++;
				}
				if (arguments[1]=='ooyala-ads-manager_1') {
                 	OO.$('#ooplayer').append('<p id=ooyalaAds_'+ooyalaAdsEventOrder+'>ooyalaAds '+ooyalaAdsEventOrder+'</p>');
                 	ooyalaAdsEventOrder++;
                }
                if (arguments[1]=='ooyala-ads-manager_2') {
                    OO.$('#ooplayer').append('<p id=ooyalaAds_'+ooyalaAdsEventOrder+'>ooyalaAds '+ooyalaAdsEventOrder+'</p>');
                    ooyalaAdsEventOrder++;
                }
			}

			if (event.match(/videoControllerVideoElementDisposed/)) {
                OO.$('#ooplayer').append('<p id=videoElementDisposed_'+videoElementDisposedEventOrder
                +'>videoElementDisposed '+videoElementDisposedEventOrder+'</p>');
                videoElementDisposedEventOrder++;
            }

            if (event.match(/videoControllerVideoElementLostFocus/)) {
                            OO.$('#ooplayer').append('<p id=videoElementLostFocus_'+videoElementLostFocusEventOrder
                            +'>videoElementDisposed '+videoElementLostFocusEventOrder+'</p>');
                            videoElementLostFocusEventOrder++;
                        }
			
			if (event.match(/videoWillPlay/) && arguments[1] == "main") {
                OO.$("#ooplayer").append("<p id=videoPlayingurl"+">Video URL "+arguments[2]+"</p>");
            }
			
			if (event.match(/savePlayerSettings/) && arguments[1].closedCaptionOptions.enabled == false && savePlayerSettingsFalseEventCount<2) {
                OO.$("#ooplayer").append("<p id=savePlayerSettings_off_"+savePlayerSettingsFalseEventCount+">"+savePlayerSettingsFalseEventCount+"</p>");
                savePlayerSettingsFalseEventCount++;
            }

            if (event.match(/savePlayerSettings/) && arguments[1].closedCaptionOptions.enabled == true && savePlayerSettingsTrueEventCount<2) {
                OO.$("#ooplayer").append("<p id=savePlayerSettings_on_"+savePlayerSettingsTrueEventCount+">"+savePlayerSettingsTrueEventCount+"</p>");
                savePlayerSettingsTrueEventCount++;
            }

            if (event.match(/volumeChanged/)) {
                 OO.$('#ooplayer').append('<p id=volumeChanged_' + volumeChanged + '>volumeChanged_'+ volumeChanged + '</p>');
                 volumeChanged++;
            }

            if (event.match(/changeVolume/)) {
                  OO.$('#ooplayer').append('<p id=changeVolume_' + changeVolume + '>changeVolume_'+ changeVolume + '</p>');
                  changeVolume++;
            }
            
            OO.log(logger);
			
		};
	}());
}