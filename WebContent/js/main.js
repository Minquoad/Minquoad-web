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

	detectInputFileButtonTrigger($("body"));

	detectMovableDivs();

	detectImprovementSuggestionAdditionForm();

});
