var oldf = console.log;
console.log = function() {
	oldf.apply(console, arguments);
	if(arguments[0].includes('Bitmovin player is using technology'))
		OO.$("#ooplayer").append("<p id=bitmovin_technology>" + arguments[0] + "</p>");
}
