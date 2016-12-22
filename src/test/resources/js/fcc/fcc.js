function subscribeToEvents() {
    window.pp.mb.subscribe('*', 'test', function(event, params) {
        var playingEventOrder = 1;
        var playedEventOrder = 1;
        var pausedEventOrder = 1;
        var videoSeekOrder=1;

        return function(event) {
            if (event.match(/playing/)) {
                OO.$('#ooplayer').append(
                    '<p id=playing_' + playingEventOrder + '>playing '
                    + playingEventOrder + '</p>');
                playingEventOrder++;
            }
           if (event.match(/paused/)) {
                OO.$('#ooplayer').append(
                    '<p id=paused_' + pausedEventOrder + '>paused '
                       + pausedEventOrder + '</p>');
                    pausedEventOrder++;
                     }
            if (event.match(/played/)) {
                            OO.$('#ooplayer').append(
                                '<p id=played_' + playedEventOrder + '>played '
                                + playedEventOrder + '</p>');
                            playedEventOrder++;
            }
            if (event.match(/videoSeek/)) {
                            OO.$('#ooplayer').append('<p id=seeked_'+videoSeekOrder
                                +'>videoSeek '+videoSeekOrder+'</p>');
                            videoSeekOrder++;
            }
        };
    }());
}