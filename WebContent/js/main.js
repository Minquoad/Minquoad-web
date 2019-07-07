$(document).ready(function() {

	executeMainActions($("body"));

	detectImprovementSuggestionAdditionForm();

});

function executeMainActions(container) {

	container.find(".table").dynatable({
		features : {
			pushState : false
		}
	});

	formatDates(container);
	detectDynamicMenuTriggers();
	borderTiles(container);
	detectForms(container);

}