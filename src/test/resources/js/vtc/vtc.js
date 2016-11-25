function subscribeToEvents() {
    window.pp.mb.subscribe('*', 'test', function(event, params) {
        var playingEventOrder = 1;
        var playedEventOrder = 1;
        var seekingEventOrder = 1;
        var fullscreenChangedEventOrder = 1;
        var reportDiscoveryClickOrder = 1;
        var pausedEventOrder = 1;
        var adPodEndedEventOrder = 1;

        return function(event) {
            if (event.match(/playing/)) {
                OO.$('#ooplayer').append(
                    '<p id=playing_' + playingEventOrder + '>playing '
                    + playingEventOrder + '</p>');
                playingEventOrder++;
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
                fullscreenChangedEventOrder++;
            }

            if (event.match(/reportDiscoveryClick/)) {

                OO.$("#ooplayer").append("<p id=reportDiscoveryClick_"+reportDiscoveryClickOrder+
                    ">reportDiscoveryClick "+reportDiscoveryClickOrder+"</p>");
                reportDiscoveryClickOrder++;
            }

            if (event.match(/adPodEnded/)) {
                OO.$("#ooplayer").append("<p id=adPodEnded_"+adPodEndedEventOrder+">adPodEnded "+adPodEndedEventOrder+"</p>");
                adPodEndedEventOrder++;
            }

            if (event.match(/seeked/)) {
                OO.$('#ooplayer').append(
                    '<p id=seeked_' + seekingEventOrder + '>seeked '
                    + seekingEventOrder + '</p>');
                seekingEventOrder++;
            }
        };
    }());
}