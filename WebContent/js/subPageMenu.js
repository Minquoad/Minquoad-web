function createSubPageMenu(
		argumentName,// the name of the argument to add to the url
		triggers,// a set of element having a data-argumentName attribute
		container,// the sub page container
		autoload = true,// if true and nothing in the url the load the first
		listener = null// will be called after every container change
	) {

	triggers.on("click", function(e) {

		let trigger = $(this);

		triggers.removeClass("selectedSubPage");
		displayLoading(container);
		trigger.addClass("selectedSubPage");

		let subPageKey = trigger.attr("data-subPageKey");
		setParamToCurrentUrl(argumentName, subPageKey);

		$.ajax({
			type : "GET",
			url : trigger.attr("data-subPageUrl"),
			dataType : "html",
			success : function(data) {
				container.empty();
				container.append(data);

				executeMainActions(container);
				if (listener != null) {
					listener();
				}
			},
			error : function(err) {
				handleAjaxError(err);
			}
		});
		
	});


	let currentSubPageKey = getCurrentUrlParameter(argumentName);

	if (currentSubPageKey) {
		
		triggers.each(function() {
			let trigger = $(this);
			if (trigger.attr("data-subPageKey") == currentSubPageKey) {
				trigger.trigger("click");
			}
		});

	} else {
		if (autoload) {
			triggers.first().trigger("click");
		}
	}
}
