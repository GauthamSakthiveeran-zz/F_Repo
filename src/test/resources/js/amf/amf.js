function subscribeToEvents() {
	
	window.pp[0].mb.subscribe('*', 'test', function(event,params) {
		var playingEventOrder = 1;
		var playedEventOrder = 1;
		var seekingEventOrder = 1;
		var willPlayAdsEventOrder = 1;
		var adsPlayedEventOrder = 1;
		var showAdSkipButtonEventOrder = 1
		var videoPlayingEventOrder = 1;
		var fullscreenChangedEventOrder = 1;
		var videowillplayEventOrder = 1;
		var replayEventOrder = 1;
		var playing = ['FirstTime','OnReplay'];
		var closedCaptionOrder = 1;
		var closedCaptionLang = 1;
		var adsclickedEventOrder = 1;
		var skipAdEventOrder = 1;
		var reportDiscoveryClickOrder = 1;
		var setEmbedCodeOrder = 1;
		var reportDiscoveryImpressionOrder = 1;
		var sendClickEventOrder = 1;
		var playbackReadyEventOrder = 1;
		var videoPreloadEventOrder = 1;
		var playerCreatedOrder = 1;
		var adPodplayedEventOrder = 1;
		var willShowNonlinearAdEventOrder = 1;
		var nonLinearAdPlayedEventOrder = 1;
		var adManagerControllerAllAdsDone =1;
		var pauseEventOrder = 1;
		var destroyEventOrder = 1;
		var count = 0;
		var reportDiscoveryImpressionOrder = 1;
		var willPlaySingleAdsEventOrder = 1;
		var singleadsPlayedEventOrder = 1;
		var adReplayEventOrder = 1;
		
		return function(event) {
			if (event.match(/playerCreated/)) {
				OO.$('#ooplayer').append('<p id=playerCreated_'+playerCreatedOrder+'>playerCreated '+arguments[1]+'</p>'); 
				playerCreatedOrder++;
			}
				
			if (event.match(/playing/)) {
				if(replayEventOrder == 2){
					OO.$('#ooplayer').append('<p id=playing_'+playing[1]+'>playing '+playing[1]+'</p>');
				}else{
					OO.$('#ooplayer').append('<p id=playing_'+playingEventOrder+'>playing '+playingEventOrder+'</p>'); 
					playingEventOrder++;
					OO.$('#ooplayer').append('<p id=playing_'+playing[0]+'>playing '+playing[0]+'</p>');
					count++;
				}
				
			} 
			if (event.match(/played/)) {
				OO.$('#ooplayer').append('<p id=played_'+playedEventOrder+'>played '+playedEventOrder+'</p>'); 
				playedEventOrder++;
			}
			if(event.match(/seeked/)) {
				OO.$('#ooplayer').append('<p id=seeked_'+seekingEventOrder+'>seeked '+seekingEventOrder+'</p>'); 
				seekingEventOrder++;
			}
			if (event.match(/willPlaySingleAd/)) {
				if(videoPlayingEventOrder == 1){
					OO.$('#ooplayer').append('<p id= willPlaySingleAd_'+willPlayAdsEventOrder+'>Preroll_willPlaySingleAd '+willPlayAdsEventOrder+'</p>'); 
					willPlayAdsEventOrder++;
				}
				if(videoPlayingEventOrder > 1 && videoPlayedEventOrder == 1){
					OO.$('#ooplayer').append('<p id=MidRoll_willPlaySingleAd_'+willPlayAdsEventOrder+'>Midroll_willPlaySingleAd '+willPlayAdsEventOrder+'</p>'); 
					willPlayAdsEventOrder++;
				}
				if(videoPlayedEventOrder == 2){
					OO.$('#ooplayer').append('<p id=willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Postroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>');
					willPlaySingleAdsEventOrder++;
				}
				OO.$('#ooplayer').append('<p id=willPlayPrerollAd'+'>willPlayPrerollAd</p>');
				OO.$('#ooplayer').append('<p id=adIsPlaying'+'>adIsPlaying</p>');
			}
			if (event.match(/singleAdPlayed/)) {
				OO.$('#ooplayer').append('<p id=singleAdPlayed_'+adsPlayedEventOrder+'>singleAdPlayed '+adsPlayedEventOrder+' </p>'); 
				adsPlayedEventOrder++;
				showAdSkipButtonEventOrder = 1;
			}
			if (event.match(/videoPlaying/) && arguments[1] == 'main') {
				OO.$('#ooplayer').append('<p id=videoPlaying_'+videoPlayingEventOrder+'>videoPlaying '+videoPlayingEventOrder+'</p>'); 
				videoPlayingEventOrder++;
			}
			if (event.match(/fullscreenChanged/)) {
				OO.$('#ooplayer').append('<p id=fullscreenChanged_'+arguments[1]+'>fullscreenChanged '+arguments[1]+'</p>'); 
				fullscreenChangedEventOrder++;
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
			if (event.match(/replay/)) {
				OO.$('#ooplayer').append('<p id=replay_1'+'>replaying Video</p>');
				replayEventOrder++;
				adReplayEventOrder++;
			}
			if (event.match(/closedCaptionCueChanged/)) {
				OO.$('#ooplayer').append('<p id=ccshowing_'+closedCaptionOrder+'>ccshown '+closedCaptionOrder+'</p>'); 
				closedCaptionOrder++;
			}
			
			if (event.match(/setClosedCaptionsLanguage/)) {
				OO.$('#ooplayer').append('<p id=cclanguage_'+ arguments[1]+'>cclanguage_'+ arguments[1]+'</p>');
				OO.$('#ooplayer').append('<p id=ccmode_'+ arguments[2].mode+'>ccmode_'+ arguments[2].mode+'</p>');
			}
			
			if (event.match(/adsClicked/)) {
				OO.$('#ooplayer').append('<p id=adsClicked_'+adsclickedEventOrder+'>adsClicked'+adsclickedEventOrder+'</p>'); 
				adsclickedEventOrder++;
				OO.$('#ooplayer').append('<p id=adsClicked_'+arguments[1]['source']+'>adsClicked'+arguments[1]['source']+'</p>');
			}
			if (event.match(/adsClickthroughOpened/)) {
				OO.$('#ooplayer').append('<p id=adsClickThroughOpened>adsClickThroughOpened</p>');
			}
			
			if (event.match(/showAdSkipButton/) && showAdSkipButtonEventOrder < 2 ) {
				OO.$('#ooplayer').append('<p id=showAdSkipButton_'+showAdSkipButtonEventOrder+'>showAdSkipButton '+showAdSkipButtonEventOrder+'</p>'); 
				showAdSkipButtonEventOrder++;
			}
			
			if (event.match(/skipAd/)) {
				OO.$('#ooplayer').append('<p id=skipAd_'+skipAdEventOrder+'>skipAd '+skipAdEventOrder+'</p>'); skipAdEventOrder++;
			}
			if (event.match(/reportDiscoveryImpression/)) {
				OO.$('#ooplayer').append('<p id=reportDiscoveryImpression_'+reportDiscoveryImpressionOrder+'>reportDiscoveryImpression '+reportDiscoveryImpressionOrder+'</p>'); 
				reportDiscoveryImpressionOrder++;
			}
			if (event.match(/sendClickEvent/)) {
				OO.$('#ooplayer').append('<p id=sendClickEvent_'+sendClickEventOrder+'>sendClickEvent '+sendClickEventOrder+'</p>'); 
				sendClickEventOrder++;
			}
			if (event.match(/reportDiscoveryClick/)) {
				OO.$('#ooplayer').append('<p id=reportDiscoveryClick_'+reportDiscoveryClickOrder+'>reportDiscoveryClick '+reportDiscoveryClickOrder+'</p>'); 
				reportDiscoveryClickOrder++;
			}
			if(event.match(/setEmbedCode/)) {
				OO.$('#ooplayer').append('<p id=setEmbedCode_'+setEmbedCodeOrder+'>setEmbedCode '+setEmbedCodeOrder+'</p>'); 
				setEmbedCodeOrder++;
			}
			if(event.match(/playbackReady/)) {
				OO.$('#ooplayer').append('<p id=playbackReady_'+playbackReadyEventOrder+'>playbackReady '+playbackReadyEventOrder+'</p>'); 
				playbackReadyEventOrder++;
			}
			if(event.match(/videoPreload/)) {
				OO.$('#ooplayer').append('<p id=videoPreload_'+videoPreloadEventOrder+'>videoPreload '+videoPreloadEventOrder+'</p>'); 
				videoPreloadEventOrder++;
			}
			if (event.match(/adsPlayed/)) {
				OO.$('#ooplayer').append('<p id=adsPlayed_'+adsPlayedEventOrder+'>adsPlayed '+adsPlayedEventOrder+' </p>'); 
				OO.$('#ooplayer').append('<p id=countPoddedAds_'+adsPlayedEventOrder+'>'+(willPlaySingleAdsEventOrder-1)+'</p>');
				adsPlayedEventOrder++;
			}
			if (event.match(/adPodEnded/)) {
				OO.$('#ooplayer').append('<p id=adPodEnd_'+arguments[1]+'_'+adPodplayedEventOrder+'>adPodPlayed '+arguments[1]+' '+adPodplayedEventOrder+'  </p>'); 
				adPodplayedEventOrder++;
			}
			
			if (event.match(/showNonlinearAd/)) {
				OO.$('#ooplayer').append('<p id=showNonlinearAd_'+willShowNonlinearAdEventOrder+'> showNonlinearAd '+willShowNonlinearAdEventOrder+'</p>'); 
				willShowNonlinearAdEventOrder++;
			}
			if (event.match(/nonlinearAdPlayed/)) {
				OO.$('#ooplayer').append('<p id=nonlinearAdPlayed_'+nonLinearAdPlayedEventOrder+'>nonlinearAdPlayed '+nonLinearAdPlayedEventOrder+' </p>'); 
				nonLinearAdPlayedEventOrder++;
			}
			
			if (event.match(/adManagerControllerAllAdsDone/)) {
				OO.$('#ooplayer').append('<p id=countPoddedAds>'+(willPlayAdsEventOrder-1) +'</p>');
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
			if (event.match(/videoSetInitialTime/)) {
				OO.$('#ooplayer').append('<p id=InitialTime_'+arguments[2]+'>InitialTime '+ arguments[2] +'</p>');
				OO.$('#ooplayer').append('<p id=VideoInitialTime>'+ arguments[2] +'</p>');
			}
			
			if (event.match(/reportDiscoveryImpression/)) {
				OO.$('#ooplayer').append('<p id=reportDiscoveryImpression_'+reportDiscoveryImpressionOrder+'>reportDiscoveryImpression '+reportDiscoveryImpressionOrder+'</p>'); 
				reportDiscoveryImpressionOrder++;
			}
			if (event.match(/willPlayAds/)) {
				if(videoPlayingEventOrder > 1 && videoPlayedEventOrder == 1){
					OO.$('#ooplayer').append('<p id=MidRoll_willPlayAds'+'>MidRoll_willPlayAds '+willPlayAdsEventOrder+'</p>'); 
					willPlayAdsEventOrder++;
				}
				if(videoPlayedEventOrder == 2){
					OO.$('#ooplayer').append('<p id=PostRoll_willPlayAds'+'>PostRoll_willPlayAds '+willPlayAdsEventOrder+'</p>'); 
					adReplayEventOrder++;
					willPlayAdsEventOrder++;
				}
				
				if(adReplayEventOrder == 3 && willPlaySingleAdsEventOrder == 5){
					OO.$('#ooplayer').append('<p id=MidRoll_willPlayAds_OnReplay'+'>MidRoll_willPlayAds_OnReplay</p>');
					adReplayEventOrder++;
				}
				if(adReplayEventOrder == 4 && willPlaySingleAdsEventOrder == 6){
					OO.$('#ooplayer').append('<p id=PostRoll_willPlayAds_OnReplay'+'>PostRoll_willPlayAds_OnReplay</p>');
					adReplayEventOrder++;
				}
			}
		};
	}());
}