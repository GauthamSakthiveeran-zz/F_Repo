
function subscribeToEvents() {
    window.pp1.mb.subscribe('*', 'test', function (event, params) {
        var playingEventOrder = 1;
        var willPlayAdsEventOrder = 1;
        var adsPlayedEventOrder = 1;
        var playedEventOrder = 1;
        var seekingEventOrder = 1;
        var fullscreenChangedEventOrder = 1;
        var pauseEventOrder = 1;
        var videoPlayedEventOrder = 1;
        var videoPlayingEventOrder = 1;

        return function (event) {
            if (event.match(/willPlaySingleAd/) && videoPlayingEventOrder == 1) {
                OO.$('#player1').append('<p id=player1_willPlaySingleAd_' + willPlayAdsEventOrder
                    + '>Preroll willPlaySingleAd ' + willPlayAdsEventOrder + '</p>');
                willPlayAdsEventOrder++;
            }
            if (event.match(/playing/)) {
                OO.$('#player1').append('<p style="color: #000088" id=player1_playing_' + playingEventOrder
                    + '>playing ' + playingEventOrder + '</p>');
                playingEventOrder++;
            }
            if (event.match(/videoPlaying/) && arguments[1] == 'main') {
                OO.$('#player1').append('<p id=player1_videoPlaying_' + videoPlayingEventOrder
                    + '>videoPlaying ' + videoPlayingEventOrder + '</p>');
                videoPlayingEventOrder++;
            }
            if (event.match(/singleAdPlayed/)) {
                OO.$('#player1').append('<p id=player1_singleAdPlayed_' + adsPlayedEventOrder
                    + '>singleAdPlayed ' + adsPlayedEventOrder + ' </p>');
                adsPlayedEventOrder++;
            }
            if (event.match(/pause/)) {
                OO.$('#player1').append('<p id=player1_pause_' + pauseEventOrder
                    + '>pause ' + pauseEventOrder + '</p>');
                pauseEventOrder++;
            }
            if (event.match(/seeked/)) {
                OO.$('#player1').append('<p id=player1_seeked_' + seekingEventOrder
                    + '>seeked ' + seekingEventOrder + '</p>');
                seekingEventOrder++;
            }
            if (event.match(/fullscreenChanged/)) {
                OO.$('#player1').append('<p id=player1_fullscreenChanged_' + arguments[1]
                    + '>fullscreenChanged ' + arguments[1] + '</p>');
                fullscreenChangedEventOrder++;
            }
            if (event.match(/played/)) {
                OO.$('#player1').append('<p id=player1_played_' + playedEventOrder
                    + '>played ' + playedEventOrder + '</p>');
                playedEventOrder++;
            }
            if (event.match(/videoPlayed/)) {
                OO.$('#player1').append('<p id=player1_videoPlayed_' + videoPlayedEventOrder
                    + '>videoPlayed' + videoPlayedEventOrder + '</p>');
                videoPlayedEventOrder++;
            }
        }
    }());

    window.pp2.mb.subscribe('*', 'test', function (event, params) {
        var playingEventOrder1 = 1;
        var willPlayAdsEventOrder1 = 1;
        var adsPlayedEventOrder1 = 1;
        var playedEventOrder1 = 1;
        var seekingEventOrder1 = 1;
        var fullscreenChangedEventOrder1 = 1;
        var pauseEventOrder1 = 1;
        var videoPlayedEventOrder1 = 1;
        var videoPlayingEventOrder1 = 1;
        return function (event) {
            if (event.match(/willPlaySingleAd/) && videoPlayingEventOrder1 == 1) {
                OO.$('#player2').append('<p id=player2_willPlaySingleAd_' + willPlayAdsEventOrder1
                    + '>Preroll willPlaySingleAd ' + willPlayAdsEventOrder1 + '</p>');
                willPlayAdsEventOrder1++;
            }
            if (event.match(/playing/)) {
                OO.$('#player2').append('<p style="color: #000088" id=player2_playing_' + playingEventOrder1
                    + '>playing ' + playingEventOrder1 + '</p>');
                playingEventOrder1++;
            }
            if (event.match(/videoPlaying/) && arguments[1] == 'main') {
                OO.$('#player2').append('<p id=player2_videoPlaying_' + videoPlayingEventOrder1
                    + '>videoPlaying ' + videoPlayingEventOrder1 + '</p>');
                videoPlayingEventOrder1++;
            }
            if (event.match(/singleAdPlayed/)) {
                OO.$('#player2').append('<p id=player2_singleAdPlayed_' + adsPlayedEventOrder1
                    + '>singleAdPlayed ' + adsPlayedEventOrder1 + ' </p>');
                adsPlayedEventOrder1++;
            }
            if (event.match(/pause/)) {
                OO.$('#player2').append('<p id=player2_pause_' + pauseEventOrder1
                    + '>pause ' + pauseEventOrder1 + '</p>');
                pauseEventOrder1++;
            }
            if (event.match(/seeked/)) {
                OO.$('#player2').append('<p id=player2_seeked_' + seekingEventOrder1
                    + '>seeked ' + seekingEventOrder1 + '</p>');
                seekingEventOrder1++;
            }
            if (event.match(/fullscreenChanged/)) {
                OO.$('#player2').append('<p id=player2_fullscreenChanged_' + arguments[1]
                    + '>fullscreenChanged ' + arguments[1] + '</p>');
                fullscreenChangedEventOrder1++;
            }
            if (event.match(/played/)) {
                OO.$('#player2').append('<p id=player2_played_' + playedEventOrder1
                    + '>played ' + playedEventOrder1 + '</p>');
                playedEventOrder1++;
            }
            if (event.match(/videoPlayed/)) {
                OO.$('#player2').append('<p id=player2_videoPlayed_' + videoPlayedEventOrder1
                    + '>videoPlayed' + videoPlayedEventOrder1 + '</p>');
                videoPlayedEventOrder1++;
            }
        }
    }());
}
