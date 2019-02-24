$(document).ready(function() {

	borderTiles();

	detectDynamicMenuTriggers();

	$('.table').dynatable({
		features : {
			pushState : false
		}
	});
});
