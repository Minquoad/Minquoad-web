let dynamicMenuTriggers;

function detectDynamicMenuTriggers() {
	dynamicMenuTriggers = $(".dynamicMenuTrigger");
}

$(document).click(function(event) {

	let clickedMenuTriggerIfExists = $(event.target).closest(".dynamicMenuTrigger");

	dynamicMenuTriggers.each(function() {

		let dynamicMenuTrigger = $(this);
		let dynamicMenu = dynamicMenuTrigger.find(".dynamicMenu");

		if (dynamicMenuTrigger.is(clickedMenuTriggerIfExists) && dynamicMenu.css("display") == "none") {

			dynamicMenu.css("display", "block");
			dynamicMenuTrigger.css("background-color", "var(--MENU_BACKGROUND_COLOR)");

		} else {

			let clickedMenuIfExists = $(event.target).closest(".dynamicMenu");

			if (!dynamicMenu.is(clickedMenuIfExists)) {
				dynamicMenu.css("display", "none");
				dynamicMenuTrigger.css("background-color", "inherit");
			}
		}
	});

});
