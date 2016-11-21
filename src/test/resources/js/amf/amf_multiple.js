function subscribeToEvents() {
	window.pp[1].mb.subscribe('*', 'test', function(event,params) {
		var playingEventOrder1 = 1;
		var willPlayAdsEventOrder1 = 1;
        var adsPlayedEventOrder1 = 1;
        var playedEventOrder1 = 1;
        var seekingEventOrder1 = 1;
        var fullscreenChangedEventOrder1 = 1;
        var pauseEventOrder1 = 1;
        var videoPlayedEventOrder1 = 1;
        var videoPlayingEventOrder1 = 1;
        
		return function(event, p1, p2, p3) {
			if (event.match(/willPlaySingleAd/) && videoPlayingEventOrder1 == 1 ) {
				OO.$('#ooplayer2').append('<p id=player2_willPlaySingleAd_'+willPlayAdsEventOrder1+'>Preroll willPlaySingleAd '+willPlayAdsEventOrder1+'</p>'); 
				willPlayAdsEventOrder1++;
            }
            if (event.match(/playing/)) {
				OO.$('#ooplayer2').append('<p id=player2_playing_'+playingEventOrder1+'>playing '+playingEventOrder1+'</p>'); 
				playingEventOrder1++;
            }
            if (event.match(/videoPlaying/) && arguments[1] == 'main') {
                OO.$('#ooplayer2').append('<p id=player2_videoPlaying_'+videoPlayingEventOrder1+'>videoPlaying '+videoPlayingEventOrder1+'</p>'); 
				videoPlayingEventOrder1++;
            }
            if (event.match(/singleAdPlayed/)) {
                OO.$('#ooplayer2').append('<p id=player2_singleAdPlayed_'+adsPlayedEventOrder1+'>singleAdPlayed '+adsPlayedEventOrder1+' </p>'); 
				adsPlayedEventOrder1++;
            }
			if(event.match(/pause/)) { 
				OO.$('#ooplayer2').append('<p id=player2_pause_'+pauseEventOrder1+'>pause '+pauseEventOrder1+'</p>'); 
				pauseEventOrder1++;
            }
            if(event.match(/seeked/)) { 
				OO.$('#ooplayer2').append('<p id=player2_seeked_'+seekingEventOrder1+'>seeked '+seekingEventOrder1+'</p>'); 
				seekingEventOrder1++;
            }
            if (event.match(/fullscreenChanged/)) {
				OO.$('#ooplayer2').append('<p id=player2_fullscreenChanged_'+arguments[1]+'>fullscreenChanged '+arguments[1]+'</p>'); 
				fullscreenChangedEventOrder1++;
            }
            if (event.match(/played/)) {
                OO.$('#ooplayer2').append('<p id=player2_played_'+playedEventOrder1+'>played '+playedEventOrder1+'</p>'); 
				playedEventOrder1++;
            }
            if (event.match(/videoPlayed/)) {
				OO.$('#ooplayer2').append('<p id=player2_videoPlayed_'+videoPlayedEventOrder1+'>videoPlayed'+videoPlayedEventOrder1+'</p>'); 
				videoPlayedEventOrder1++;
            }
			
		};
	}());
}