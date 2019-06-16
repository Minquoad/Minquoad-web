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
