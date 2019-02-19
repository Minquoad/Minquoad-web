$(document).ready(function() {

	borderTiles();

	$('.table').dynatable({
		features : {
			pushState : false
		}
	});
});
