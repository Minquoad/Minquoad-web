$(document).ready(function() {

	let subPageMenu = new SubPageMenu("administrationSubPageKey", $(".administration #currentSubPage"));

	$(".administration #nav>div").each(function() {
		let trigger = $(this);
		subPageMenu.createAndAddItem(trigger, null)
	});

});
