let websocket = null;
let groupedByRoleWsListeners = {};

function addRoledJsonWebsocketListener(role, listener) {

	if (websocket == null) {

		let baseUrl = window.location.href.substring(window.location.href.indexOf(window.location.hostname));
		baseUrl = baseUrl.substring(0, baseUrl.indexOf(currentContext) + currentContext.length);

		websocket = new WebSocket("ws://" + baseUrl + "ImprovedEndpoint");

		websocket.onopen = function(evt) {
			for (let loopRole in groupedByRoleWsListeners) {
				websocket.send(loopRole);
			}
		};

		websocket.onerror = function(e) {
			console.error(e);
			if (confirm("An error occured.\nRefresh page?")) {
				window.location.replace("");
			}
		};

		websocket.onclose = function() {
			if (confirm("Connection with server lost.\nRefresh page?")) {
				window.location.replace("");
			}
		};

		websocket.onmessage = function(event) {
			let parsedEvent = JSON.parse(event.data);
			for (let listener of groupedByRoleWsListeners[parsedEvent.role]) {
				listener(parsedEvent.data);
			}
		};

	}

	if (!groupedByRoleWsListeners.hasOwnProperty(role)) {
		groupedByRoleWsListeners[role] = [];
		if (websocket.readyState) {
			websocket.send(role);
		}
	}

	groupedByRoleWsListeners[role].push(listener);
	
}
