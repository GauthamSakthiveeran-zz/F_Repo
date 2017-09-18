function subscribeToEvents() {

	window.pp.mb.subscribe('*', 'test', function(event,params) {

        var moveToDirection = 1;
        var moveToDirectionEventOrder = 1;
        var direction_changed = 1;
        var direction_changedEventOrder = 1;
        var play = 1;
        var playEventOrder = 1;
              var ddd = 1;
        var dddEventOrder = 1;
               var yyy = 1;
        var yyyEventOrder = 1;


        var logger = '';

		return function(event) {

			logger = 'QE Preformance log: event: ' + event;

			if (event.match(/moveToDirection/) && arguments[2] == true) {
					OO.$('#ooplayer').append('<p id=moveToDirection_'+moveToDirectionEventOrder+'>moveToDirection '+moveToDirectionEventOrder+'</p>');
					moveToDirectionEventOrder++;
			}
			if (event.match(/direction_changed/)) {
					OO.$('#ooplayer').append('<p id=direction_changed_'+direction_changedEventOrder+'>direction_changed '+direction_changedEventOrder+'</p>');
					direction_changedEventOrder++;
			}
				if (event.match(/play/) && arguments[0] == "play") {
					OO.$('#ooplayer').append('<p id=play_'+playEventOrder+'>play_'+playEventOrder+'</p>');
					playEventOrder++;
			}

					if (event.match(/play/)) {
					OO.$('#isMobile').append('<p id=yyy_'+yyyEventOrder+'>yyy_'+yyyEventOrder+'</p>');
					yyyEventOrder++;
			}

				if (event.match(/isMobile/) ) {
					OO.$('#ooplayer').append('<p id=dd_'+dddEventOrder+'>dd_'+dddEventOrder+'</p>');
					dddEventOrder++;
			}

            OO.log(logger);

		};
	}());
}