function formatDates() {

	$(".dateToFormat").each(function() {
		let dateToFormat = $(this);

		let text = dateToFormat.text().trim();

		if (text != "") {

			let date = new Date(dateToFormat.text().trim());

			let options = null;

			if (dateToFormat.attr("data-format") == "1") {
				options = {
					hour : '2-digit',
					minute : '2-digit'
				};
			}

			dateToFormat.text(date.toLocaleDateString(navigator.language, options));

		}

		dateToFormat.removeClass("dateToFormat");
	});

}
