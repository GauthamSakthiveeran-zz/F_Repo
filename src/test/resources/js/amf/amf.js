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
		
		return function(event) {
			
			if (event.match(/showNonlinearAd/)) {
				OO.$('#ooplayer').append('<p id=showNonlinearAd_'+willShowNonlinearAdEventOrder+'> showNonlinearAd '+willShowNonlinearAdEventOrder+'</p>'); 
				willShowNonlinearAdEventOrder++;
			}
				
			if (event.match(/videoPlaying/) && arguments[1] == 'main') {
				OO.$('#ooplayer').append('<p id=videoPlaying_'+videoPlayingEventOrder+'>videoPlaying '+videoPlayingEventOrder+'</p>'); 
				videoPlayingEventOrder++;
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
					willPlaySingleAdsEventOrder++;
				}
				if(videoPlayingEventOrder > 1 && videoPlayedEventOrder == 1){
					OO.$('#ooplayer').append('<p id=MidRoll_willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Midroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>'); 
					OO.$('#ooplayer').append('<p id=willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Midroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>'); 
					willPlaySingleAdsEventOrder++;
				}
				if(videoPlayedEventOrder == 2){
					OO.$('#ooplayer').append('<p id=willPlaySingleAd_'+willPlaySingleAdsEventOrder+'>Postroll_willPlaySingleAd '+willPlaySingleAdsEventOrder+'</p>');
					willPlaySingleAdsEventOrder++;
				}
				OO.$('#ooplayer').append('<p id=willPlayPrerollAd'+'>willPlayPrerollAd</p>');
				OO.$('#ooplayer').append('<p id=adIsPlaying'+'>adIsPlaying</p>');
			}
			if (event.match(/singleAdPlayed/)) {
				OO.$('#ooplayer').append('<p id=singleAdPlayed_'+singleadsPlayedEventOrder+'>singleAdPlayed '+singleadsPlayedEventOrder+' </p>'); 
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
			if (event.match(/replay/)) {
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
				OO.$('#ooplayer').append('<p id=adsPlayed_'+adsPlayedEventOrder+'>adsPlayed '+adsPlayedEventOrder+' </p>'); 
				OO.$('#ooplayer').append('<p id=countPoddedAds_'+adsPlayedEventOrder+'>'+(willPlaySingleAdsEventOrder-1)+'</p>');
				adsPlayedEventOrder++;
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
			
			if (event.match(/videoPlayed/)&& arguments[1] == 'main') {
				OO.$('#ooplayer').append('<p id=videoPlayed_'+videoPlayedEventOrder+'>videoPlayed '+videoPlayedEventOrder+'</p>'); 
                videoPlayedEventOrder++;
			}  
		};
	}());
}