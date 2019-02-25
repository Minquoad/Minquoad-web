$(document).ready(function() {

	borderTiles();

	detectDynamicMenuTriggers();

	detectMovableDivs();
	
	$('.table').dynatable({
		features : {
			pushState : false
		}
	});
});
