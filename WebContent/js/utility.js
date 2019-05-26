function toHtmlEquivalent(original) {
	var tmpElement = document.createElement("div");
	tmpElement.append(document.createTextNode(original));
	return tmpElement.innerHTML;
}

function handleAjaxError(error) {
	let message = "";
	if (error.status == 401) {
		message = "Unauthorized";
	} else if (error.status == 403) {
		message = "Forbidden";
	} else if (error.status / 100 == 5) {
		message = "Internal Server Error";
	}
	if (confirm(message + ". Refresh page?")) {
		window.location.replace("");
	}
}

function displayLoading(element) {
	element.html($("#loadingDiv").html());
}

function creteWebsocketWithRole(role) {

	let currentContext = $("#variousData").attr("data-currentContext");
	let baseUrl = window.location.href.substring(window.location.href.indexOf(window.location.hostname));
	baseUrl = baseUrl.substring(0, baseUrl.indexOf(currentContext) + currentContext.length);

	let websocket = new WebSocket("ws://" + baseUrl + "ImprovedEndpoint");

	websocket.onopen = function(evt) {
		websocket.send(role);
	};

	websocket.onerror = function(e) {
		console.error(e);
		if (confirm("An error occured. Refresh page?")) {
			window.location.replace("");
		}
	};

	return websocket;
}

function improveTextField(textField) {

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

function improveReadability(originalText) {

	let newText = "";

	let parenthesisOpeningPosition = originalText.indexOf("(");
	let parenthesisClosingPosition = originalText.indexOf(")");
	let firstQuotePosition = originalText.indexOf("\"");
	let secondQuotePosition = -1;
	if (firstQuotePosition != -1) {
		secondQuotePosition = firstQuotePosition + 1 + originalText.substring(firstQuotePosition + 1).indexOf("\"");
	}
	let parenthesisPossible = parenthesisOpeningPosition != -1 && parenthesisClosingPosition != -1 && parenthesisOpeningPosition < parenthesisClosingPosition;
	let quotePossible = firstQuotePosition != -1 && secondQuotePosition != -1;

	if (parenthesisPossible || quotePossible) {

		let parenthesisFirst = null;

		if (parenthesisPossible && quotePossible) {
			parenthesisFirst = parenthesisOpeningPosition < firstQuotePosition;
		} else if (parenthesisPossible) {
			parenthesisFirst = true;
		} else {
			parenthesisFirst = false;
		}

		if (parenthesisFirst) {
			newText += originalText.substring(0, parenthesisOpeningPosition);
			newText += '<span class="parenthesis">';
			newText += "(";
			newText += improveReadability(originalText.substring(parenthesisOpeningPosition + 1, parenthesisClosingPosition));
			newText += ")";
			newText += '</span>';
			newText += improveReadability(originalText.substring(parenthesisClosingPosition + 1));

		} else {
			newText += originalText.substring(0, firstQuotePosition);
			newText += "\"";
			newText += '<span class="italic">';
			newText += improveReadability(originalText.substring(firstQuotePosition + 1, secondQuotePosition));
			newText += '</span>';
			newText += "\"";
			newText += improveReadability(originalText.substring(secondQuotePosition + 1));

		}

	} else {
		newText += originalText;
	}

	return newText.replace("\n", "<br/>");
}
