function toHtmlEquivalent(original) {
	let tmpElement = document.createElement("div");
	tmpElement.append(document.createTextNode(original));
	return tmpElement.innerHTML;
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
