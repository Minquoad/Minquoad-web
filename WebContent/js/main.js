$(document).ready(function() {

	executeMainActions($("body"));

	detectImprovementSuggestionAdditionForm();

	addRoledJsonWebsocketListener("refreshment", function(refreshment) {
		
		let maximumWait = Math.floor(Math.random() * refreshment.maximumWait); 

		setTimeout(() => {
			document.location.reload(refreshment.hard);
		}, maximumWait);
		
	});

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
