let websocket = null;
let groupedByRoleWsListeners = {};

function addRoledJsonWebsocketListener(role, listener) {

	if (websocket == null) {

		let baseUrl = window.location.href.substring(window.location.href.indexOf(window.location.hostname));
		baseUrl = baseUrl.substring(0, baseUrl.indexOf(currentContext) + currentContext.length);

		websocket = new WebSocket("ws://" + baseUrl + "ImprovedEndpoint");

		websocket.onopen = onopen;
		websocket.onerror = onerror;
		websocket.onclose = onclose;
		websocket.onmessage = onmessage;
	}

	if (!groupedByRoleWsListeners.hasOwnProperty(role)) {
		groupedByRoleWsListeners[role] = [];
		if (websocket.readyState) {
			websocket.send(role);
		}
	}

	groupedByRoleWsListeners[role].push(listener);
}

function onopen(evt) {
	for (let role in groupedByRoleWsListeners) {
		websocket.send(role);
	}
}

function onerror(e) {
	console.error(e);
	if (confirm("An error occured.\nRefresh page?")) {
		window.location.replace("");
	}
}

function onclose(event) {
	if (confirm("Connection with server lost.\nRefresh page?")) {
		window.location.replace("");
	}
}

function onmessage(event) {
	let parsedEvent = JSON.parse(event.data);
	for (let listener of groupedByRoleWsListeners[parsedEvent.role]) {
		listener(parsedEvent.data);
	}
}
