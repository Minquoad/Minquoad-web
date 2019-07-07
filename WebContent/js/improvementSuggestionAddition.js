function detectImprovementSuggestionAdditionForm() {

	let form = $("#footer #improvementSuggestionAdditionForm");
	let textarea = $('#footer #improvementSuggestionAdditionForm textarea');

	textarea.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13' && !event.shiftKey) {
			event.preventDefault();

			if (textarea.val() !== "") {
				if (confirm("Are you shure you want to sent the message \"" + textarea.val() + "\" to the website designer?")) {

					submitForm(form, function(data) {
						alert("Thank you :)");
					});

					textarea.val("");
				}
			}
		}
	});

}