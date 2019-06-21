$(document).ready(function() {

	executeMainActions($("body"));

	detectImprovementSuggestionAdditionForm();

});

function executeMainActions(container) {

	formatDates(container);
	detectDynamicMenuTriggers();
	borderTiles(container);
	detectInputFileButtonTrigger(container);

	container.find(".table").dynatable({
		features : {
			pushState : false
		}
	});

}