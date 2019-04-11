function detectImprovementSuggestionAdditionForm() {

	let form = $("#footer #improvementSuggestionAdditionForm");
	let textarea = $('#footer #improvementSuggestionAdditionForm textarea');

	textarea.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13' && !event.shiftKey) {
			event.preventDefault();

			if (textarea.val() !== "") {
				if (confirm("Are you shure you want to sent the message \"" 
						+ textarea.val()
						+ "\" to the website designer?")) {
					$.ajax({
						url : form.attr('action'),
						type : "POST",
						data : form.serialize(),
						success : function() {
							alert("Thank you :)");
							textarea.val("");
						},
						error : function(jqXHR) {
							alert("ERROR " + jqXHR.status);
							window.location.replace("");
						}
					});
				}
			}
		}
	});

}