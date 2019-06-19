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
				successHandle();
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
				successHandle();
			}

		}).fail(function(err) {
			handleAjaxError(err);
			
		});
	}
}

function detectInputFileButtonTrigger(container) {

	let triggers = container.find('.inputFileTrigger>*:not([type="file"])');

	triggers.on("click", function(e) {
		let input = $(this).closest(".inputFileTrigger").find("[type='file']");
		input.trigger('click');
	});

	let inputs = triggers.closest(".inputFileTrigger").find("[type='file']");
	inputs.on("change", function(e) {
		let input = $(this);
		let triggerContainer = $(this).closest(".inputFileTrigger");

		if (input.val()) {
			triggerContainer.find(".visibleWhenFull").show();
			triggerContainer.find(".visibleWhenEmpty").hide();
		} else {
			triggerContainer.find(".visibleWhenFull").hide();
			triggerContainer.find(".visibleWhenEmpty").show();
		}
	});

	inputs.trigger("change");
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

				let quoteCount = 0;
				let originalText = textField.val();
				for (let i = 0; i < originalText.length; i++) {
					if (originalText.charAt(i) == "\"") {
						quoteCount++;
					}
				}
				if (quoteCount % 2 == 0) {
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
