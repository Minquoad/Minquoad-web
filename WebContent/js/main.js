$(document).ready(function() {

	executeMainActions($("body"));

	detectImprovementSuggestionAdditionForm();

});

function executeMainActions(container) {

	formatDates(container);

	container.find(".table").dynatable({
		features : {
			pushState : false
		}
	});

	detectReadabilityToImprove(container);
	detectDynamicMenuTriggers();
	borderTiles(container);
	detectForms(container);
}
