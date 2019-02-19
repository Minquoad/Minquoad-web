$(document).ready(function() {

	let dynamicMenuTriggers = $(".dynamicMenuTrigger");

	$(document).click(function(event) {

		let clickedMenuTriggerIfExists = $(event.target).closest(".dynamicMenuTrigger");

		dynamicMenuTriggers.each(function() {
			if ($(this).is(clickedMenuTriggerIfExists)) {

				if ($(this).find(".dynamicMenu").css("display") == "none") {
					$(this).find(".dynamicMenu").css("display", "block");
					$(this).css("background", "var(--MENU_BACKGROUND_COLOR)");
				} else {
					$(this).find(".dynamicMenu").css("display", "none");
					$(this).css("background", "inherit");
				}
				
			} else {
				$(this).find(".dynamicMenu").css("display", "none");
				$(this).css("background", "inherit");
			}
		});
		
	});

});
