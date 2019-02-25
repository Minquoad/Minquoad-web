$(document).ready(function() {

	borderTiles();

	detectDynamicMenuTriggers();

	detectConversationTriggers();

	detectConversationTriggers();

	detectMovableDivs();

	$('.table').dynatable({
		features : {
			pushState : false
		}
	});
});
