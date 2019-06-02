$(document).ready(function() {

	$('.table').dynatable({
		features : {
			pushState : false
		}
	});

	formatDates();

	borderTiles();

	detectDynamicMenuTriggers();

	// detectConversationTriggers();

	detectMovableDivs();

	detectImprovementSuggestionAdditionForm();

});
