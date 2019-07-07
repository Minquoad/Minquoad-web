function createSubPageMenu(
		argumentName,// the name of the argument to add to the url
		triggers,// a set of element having a data-argumentName attribute
		container,// the sub page container
		autoload = true,// if true and nothing in the url the load the first
		listener = null// will be called after every container change
	) {

	triggers.on("click", function(e) {

		let trigger = $(this);

		let selectedSubPageClass = "selectedSubPage";

		if (!trigger.hasClass(selectedSubPageClass)) {
			triggers.removeClass(selectedSubPageClass);
			displayLoading(container);
			trigger.addClass(selectedSubPageClass);
			
			let subPageKey = trigger.attr("data-subPageKey");
			setParamToCurrentUrl(argumentName, subPageKey);
	
			$.ajax({
				type : "GET",
				url : trigger.attr("data-subPageUrl"),
				dataType : "html",
				success : function(data) {
					fillSubPage(container, data, listener);
				},
				error : function(err) {
					handleAjaxError(err);
				}
			});
		}
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

function fillSubPage(container, content, listener) {
	container.empty();
	container.append(content);

	executeMainActions(container);

	container.find('form').each(function() {
		let form = $(this);
		let submitInput = form.find("input[type='submit']");
		submitInput.on("click", function(e) {
			e.preventDefault();
			submitForm(form, function(data) {
				fillSubPage(container, data, listener);
			});
		});
	});

	if (listener != null) {
		listener();
	}
}
