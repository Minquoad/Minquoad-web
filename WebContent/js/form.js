function submitForm(form, successHandle = null) {

	if (form.attr('enctype') === "multipart/form-data") {

		$.ajax({
			url : form.attr('action'),
			type : 'POST',
			data : new FormData(form.get(0)),
			processData: false,
			contentType: false

		}).done(function(data) {
			if (successHandle !== null) {
				successHandle(data);
			}

		}).fail(function(err) {
			handleAjaxError(err);
			
		});

	} else {
		$.ajax({
			url : form.attr('action'),
			type : 'POST',
			data : form.serialize()

		}).done(function(data) {
			if (successHandle !== null) {
				successHandle(data);
			}

		}).fail(function(err) {
			handleAjaxError(err);
			
		});
	}
}

function detectForms(container) {
	detectFileInputTriggers(container);
	improveTextFields(container);
}

function detectFileInputTriggers(container) {
	if (container.hasClass("improvedTextField")) {
		detectFileInputTrigger(container);
	}

	container.find(".inputFileTrigger").each(function() {
		detectFileInputTrigger($(this));
	});
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

function improveTextFields(container) {
	if (container.hasClass("improvedTextField")) {
		improveTextField(container);
	}

	container.find(".improvedTextField").each(function() {
		improveTextField($(this));
	});
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
