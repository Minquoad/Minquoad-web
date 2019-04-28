function handleAjaxError(error) {
	let message = "";
	if (error.status == 401) {
		message = "Unauthorized";
	} else if (error.status == 403) {
		message = "Forbidden";
	} else if (error.status/100 == 5) {
		message = "Internal Server Error";
	}
	alert(message);
	window.location.replace("");
}

function displayLoading(element) {
	element.html($("#loadingDiv").html());
}
