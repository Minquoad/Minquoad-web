function toHtmlEquivalent(original) {
	var tmpElement = document.createElement("div");
	tmpElement.append(document.createTextNode(original));
	return tmpElement.innerHTML.replace("\n", "<br/>");
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
	alert(message);
	window.location.replace("");
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
		if (confirm("An error occured. Do you want to refresh the page?")) {
			window.location.replace("");
		}
	};

	return websocket;
}
