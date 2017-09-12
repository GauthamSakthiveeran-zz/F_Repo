function subscribeToEvents() {

	window.pp.mb.subscribe('*', 'test', function(event,params) {

        var moveToDirection = 1;
        var moveToDirectionEventOrder = 1;
        var direction_changed = 1;
        var direction_changedEventOrder = 1;
        var someKeyEvent = 1;
        var someKeyEventEventOrder = 1;

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

            OO.log(logger);

		};
	}());
}