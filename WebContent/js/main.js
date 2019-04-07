$(document).ready(function() {

	borderTiles();

	detectDynamicMenuTriggers();

	// detectConversationTriggers();

	detectMovableDivs();

	$('.table').dynatable({
		features : {
			pushState : false
		}
	});
	
	detectImprovementSuggestionAdditionForm();
	
});
