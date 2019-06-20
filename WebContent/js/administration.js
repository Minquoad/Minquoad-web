$(document).ready(function() {
	detectNavDivs();

	let currentSubPage = getCurrentUrlParameter("currentSubPage");

	if (currentSubPage) {
		$("#administration #nav div").each(function() {
			let navDiv = $(this);
			if (navDiv.attr("data-administrationSubPageName") == currentSubPage) {
				navDiv.trigger("click");
			}
		});
	} else {
		$("#administration #nav div:first-child").trigger("click");
	}
	
});

function detectNavDivs() {

	let currentSubPage = $("#administration #currentSubPage");
	let navDivs = $("#administration #nav div");

	navDivs.on("click", function(e) {

		let navDiv = $(this);

		navDivs.removeClass("selectedSubPage");
		displayLoading(currentSubPage);
		navDiv.addClass("selectedSubPage");

		removeAllCurrentUrlParameters();
		setParamToCurrentUrl("currentSubPage", navDiv.attr("data-administrationSubPageName"));

		$.ajax({
			type : "GET",
			url : navDiv.attr("data-administrationSubPageUrl"),
			dataType : "html",
			success : function(data) {
				currentSubPage.empty();
				currentSubPage.append(data);

				formatDates();
				borderTiles();
				detectDynamicMenuTriggers();
			},
			error : function(err) {
				handleAjaxError(err);
			}
		});
	});
}
