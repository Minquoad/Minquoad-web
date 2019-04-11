$(document).ready(function() {

	$('.table').dynatable({
		features : {
			pushState : false
		}
	});

	borderTiles();

	detectDynamicMenuTriggers();

	// detectConversationTriggers();

	detectMovableDivs();

	detectImprovementSuggestionAdditionForm();
	
});
