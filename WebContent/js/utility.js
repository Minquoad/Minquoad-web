function toHtmlEquivalent(original) {
	let tmpElement = document.createElement("div");
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

let loadingDiv = null;
function displayLoading(element) {
	if (loadingDiv == null) {
		loadingDiv = $("#loadingDiv");
	}
	element.html(loadingDiv.html().trim());
}

let websockets = {};
let websocketJsonListeners = {};
function addRoledJsonWebsocketListener(role, listener) {

	if (!websockets.hasOwnProperty(role)) {

		let baseUrl = window.location.href.substring(window.location.href.indexOf(window.location.hostname));
		baseUrl = baseUrl.substring(0, baseUrl.indexOf(currentContext) + currentContext.length);

		let websocket = new WebSocket("ws://" + baseUrl + "ImprovedEndpoint");

		websocket.onopen = function(evt) {
			websocket.send(role);
		};

		websocket.onclose = function() {
			if (confirm("Connection with server lost. Refresh page?")) {
				window.location.replace("");
			}
		};

		websocket.onerror = function(e) {
			console.error(e);
			if (confirm("An error occured. Refresh page?")) {
				window.location.replace("");
			}
		};

		websocket.onmessage = function(event) {
			let parsedEvent = JSON.parse(event.data);
			for (let listener of websocketJsonListeners[role]) {
				listener(parsedEvent);
			}
		};

		websockets[role] = websocket;
		websocketJsonListeners[role] = [];
	}

	websocketJsonListeners[role].push(listener);
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

function improveReadability(originalText) {

	if (!readabilityImprovementActivated) {
		return originalText;
	}

	let parenthesisOpeningPosition = originalText.indexOf("(");

	if (parenthesisOpeningPosition != -1) {
		let parenthesisClosingPosition = -1;

		let nesting = 0;
		for (let i = parenthesisOpeningPosition + 1; i < originalText.length && parenthesisClosingPosition == -1; i++) {
			let char = originalText.charAt(i);
			if (char == ")") {
				if (nesting == 0) {
					parenthesisClosingPosition = i;
				} else {
					nesting--;
				}
			}
			if (char == "(") {
				nesting++;
			}
		}

		if (parenthesisClosingPosition != -1) {
			let newText = "";
			newText += improveReadability(originalText.substring(0, parenthesisOpeningPosition));
			newText += '<span class="parenthesis">';
			newText += "(";
			newText += improveReadability(originalText.substring(parenthesisOpeningPosition + 1, parenthesisClosingPosition));
			newText += ")";
			newText += '</span>';
			newText += improveReadability(originalText.substring(parenthesisClosingPosition + 1));
			return newText;

		} else {
			let newText = "";
			newText += improveReadability(originalText.substring(0, parenthesisOpeningPosition));
			newText += "(";
			newText += improveReadability(originalText.substring(parenthesisOpeningPosition + 1));
			return newText;
		}

	} else {
		return improveQuotesReadability(originalText);
	}
}

function improveQuotesReadability(originalText) {

	let firstQuotePosition = originalText.indexOf("\"");
	let secondQuotePosition = -1;
	if (firstQuotePosition != -1 && firstQuotePosition + 1 != originalText.length) {
		let secondQuoteRelativePosition = originalText.substring(firstQuotePosition + 1).indexOf("\"");
		if (secondQuoteRelativePosition != -1) {
			secondQuotePosition = firstQuotePosition + 1 + secondQuoteRelativePosition;
		}
	}

	if (firstQuotePosition != -1 && secondQuotePosition != -1) {
		let newText = "";
		newText += originalText.substring(0, firstQuotePosition);
		newText += "\"";
		newText += '<span class="italic">';
		newText += originalText.substring(firstQuotePosition + 1, secondQuotePosition);
		newText += '</span>';
		newText += "\"";
		newText += improveQuotesReadability(originalText.substring(secondQuotePosition + 1));
		return newText;
	}

	return originalText;
}
