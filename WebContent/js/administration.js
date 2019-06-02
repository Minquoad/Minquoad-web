function detectNavDivs() {

	let currentSubPage = $("#administration #currentSubPage");
	let navDivs = $("#administration #nav div");

	navDivs.on("click", function(e) {

		let navDiv = $(this);

		navDivs.removeClass("selectedSubPage");
		displayLoading(currentSubPage);
		navDiv.addClass("selectedSubPage");

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

$(document).ready(function() {
	detectNavDivs();
	
	var currentSubPage = new URL(window.location).searchParams.get("currentSubPage");
	
	if (currentSubPage) {
		let navDivs = $("#administration #nav div");
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
