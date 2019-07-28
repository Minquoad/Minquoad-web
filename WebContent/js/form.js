function submitForm(form, successHandle = null) {

	if (form.attr('enctype') === "multipart/form-data") {

		$.ajax({
			url : form.attr('action'),
			type : 'POST',
			data : new FormData(form.get(0)),
			processData: false,
			contentType: false

		}).done(function(data, textStatus, xhr) {
			if (successHandle !== null) {
				successHandle(data, textStatus, xhr);
			}

		}).fail(function(jqXHR, textStatus, error) {
			handleAjaxError(jqXHR, textStatus, error);
		});

	} else {
		$.ajax({
			url : form.attr('action'),
			type : 'POST',
			data : form.serialize()

		}).done(function(data, textStatus, xhr) {
			console.log(textStatus);
			if (successHandle !== null) {
				successHandle(data, textStatus, xhr);
			}

		}).fail(function(jqXHR, textStatus, error) {
			handleAjaxError(jqXHR, textStatus, error);
		});
	}
}

function detectForms(container) {
	container.find(".inputFileTrigger").each(function() {
		detectFileInputTrigger($(this));
	});

	container.find(".improvedTextField").each(function() {
		improveTextField($(this));
	});

	container.find("select.select2").each(function() {
		updateSelect2($(this));
	});

	container.find("input[name='timezoneOffset']").val(new Date().getTimezoneOffset());
}

function detectFileInputTrigger(container) {

	let triggers = container.find(".visibleWhenFull, .visibleWhenEmpty");

	let input = container.find("[type='file']");

	triggers.on("click", function(e) {
		input.trigger('click');
	});

	input.on("change", function(e) {
		if (input.val()) {
			container.find(".visibleWhenFull").show();
			container.find(".visibleWhenEmpty").hide();
		} else {
			container.find(".visibleWhenFull").hide();
			container.find(".visibleWhenEmpty").show();
		}
	});

	input.trigger("change");
}

function improveTextField(textField) {

	if (!typingAssistanceActivated) {
		return;
	}

	textField.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (!event.shiftKey && textField.prop('selectionStart') == textField.prop('selectionEnd')) {

			if (keycode == '40') {
				event.preventDefault();

				let position = textField.prop('selectionStart');
				textField.val(textField.val().substring(0, position) + '()' + textField.val().substring(position));
				textField.prop('selectionStart', position + 1);
				textField.prop('selectionEnd', position + 1);

			} else if (keycode == '41') {
				let position = textField.prop('selectionStart');
				if (position != textField.val().length && textField.val().substring(position, position + 1) == ")") {
					event.preventDefault();
					textField.prop('selectionStart', position + 1);
					textField.prop('selectionEnd', position + 1);
				}

			} else if (keycode == '34') {

				let quoteNumber = 0;
				let originalText = textField.val();
				for (let i = 0; i < originalText.length; i++) {
					if (originalText.charAt(i) == "\"") {
						quoteNumber++;
					}
				}
				if (quoteNumber % 2 == 0) {
					event.preventDefault();

					let position = textField.prop('selectionStart');
					if (position == textField.val().length || textField.val().substring(position, position + 1) != "\"") {
						textField.val(textField.val().substring(0, position) + "\"\"" + textField.val().substring(position));
					}
					textField.prop('selectionStart', position + 1);
					textField.prop('selectionEnd', position + 1);
				}
			}
		}
	});
}

function updateSelect2(select2Element) {
	select2Element.select2({
		matcher: function(params, data) {
			data.parentText = data.parentText || "";

			// Always return the object if there is nothing to compare
			if ($.trim(params.term) === '') {
				return data;
			}

			// Do a recursive check for options with children
			if (data.children && data.children.length > 0) {
				// Clone the data object if there are children
				// This is required as we modify the object to remove any
				// non-matches
				var match = $.extend(true, {}, data);

				// Check each child of the option
				for (var c = data.children.length - 1; c >= 0; c--) {
					var child = data.children[c];
					child.parentText += data.parentText + " " + data.text;

					var matches = modelMatcher(params, child);

					// If there wasn't a match, remove the object in the array
					if (matches == null) {
						match.children.splice(c, 1);
					}
				}

				// If any children matched, return the new object
				if (match.children.length > 0) {
					return match;
				}

				// If there were no matching children, check just the plain
				// object
				return modelMatcher(params, match);
			}

			// If the typed-in term matches the text of this term, or the text
			// from any parent term, then it's a match.
			var original = (data.parentText + ' ' + data.text).toUpperCase();
			var term = params.term.toUpperCase();

			// Check if the text contains the term
			if (original.indexOf(term) > -1) {
				return data;
			}

			// If it doesn't contain the term, don't return anything
			return null;
		}
	});
}
