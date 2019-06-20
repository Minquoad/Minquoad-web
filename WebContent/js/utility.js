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

function displayLoading(element) {
	let html = "";
	html += '<div class="centererContainer fullSize loadingDiv">';
	html += '<div class="totallyCenteredContainer">';
	html += '<img height="64" width="64" src="' + loadingImgUrl + '" />';
	html += '</div>';
	html += '</div>';
	element.html(html);
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
		newText += improveQuotesReadability(originalText.substring(0, firstQuotePosition));
		newText += "\"";
		newText += '<span class="italic">';
		newText += improveQuotesReadability(originalText.substring(firstQuotePosition + 1, secondQuotePosition));
		newText += '</span>';
		newText += "\"";
		newText += improveQuotesReadability(originalText.substring(secondQuotePosition + 1));
		return newText;
	}

	return imporveUrlReadability(originalText);
}

function imporveUrlReadability(originalText) {

	let urlStartPosition = originalText.indexOf("http://");
	
	let urlEndPosition = -1;
	if (urlStartPosition != -1 && urlStartPosition != originalText.length) {
		let urlEndPosition = originalText.substring(urlStartPosition).indexOf(' ');
		if (urlEndPosition != -1) {
			urlEndPosition = urlStartPosition + urlEndPosition;
		}
	}
	if (urlStartPosition != -1 && urlEndPosition != -1) {
		
		let url = originalText.substring(urlStartPosition, urlEndPosition);

		let newText = "";
		newText += imporveUrlReadability(originalText.substring(0, urlStartPosition));
		newText += '<a href="';
		newText += url;
		newText += '">';
		newText += url;
		newText += '</a>';
		newText += imporveUrlReadability(originalText.substring(urlEndPosition));
		return newText;
	}
	
	return imporveSecureUrlReadability(originalText);
}

function imporveSecureUrlReadability(originalText) {

	let urlStartPosition = originalText.indexOf("https://");
	
	let urlEndPosition = -1;
	if (urlStartPosition != -1 && urlStartPosition != originalText.length) {
		let urlEndPosition = originalText.substring(urlStartPosition).indexOf(" ");
		if (urlEndPosition != -1) {
			urlEndPosition = urlStartPosition + urlEndPosition;
		}
	}
	if (urlStartPosition != -1 && urlEndPosition != -1) {
		
		let url = originalText.substring(urlStartPosition, urlEndPosition);

		let newText = "";
		newText += originalText.substring(0, urlStartPosition);
		newText += '<a href="';
		newText += url;
		newText += '">';
		newText += url;
		newText += '</a>';
		newText += imporveUrlReadability(originalText.substring(urlEndPosition));
		return newText;
	}
	
	return originalText;
}

function getCurrentUrlParameter(name) {
	let url = new URL(window.location);
	return url.searchParams.get(name);
}

function removeAllCurrentUrlParameters() {
	let location = window.location.toString();
	
	let argumentsStartKeyCharPos = location.indexOf("?");

	if (argumentsStartKeyCharPos != -1) {
		window.history.replaceState(null, null, location.substring(0, argumentsStartKeyCharPos));
	}
}

function setParamToCurrentUrl(name, value) {
	let url = new URL(window.location);
	url.searchParams.set(name, value);
	window.history.replaceState(null, null, url.toString());
}





