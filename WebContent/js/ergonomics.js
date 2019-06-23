function formatDates(container) {

	container.find(".dateToFormat").each(function() {
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

			dateToFormat.text(date.toLocaleDateString(language, options));

		}

		dateToFormat.removeClass("dateToFormat");
	});

}

function improveReadability(originalHtml) {
	let newHtml = improveReadabilityWithParameter(
			originalHtml,
			"\"",
			"\"",
			"italic",
			false
			);

	newHtml = improveReadabilityWithParameter(
			newHtml,
			"(",
			")",
			"parenthesis",
			true
			);

	newHtml = imporveUrlReadability(newHtml);

	return newHtml;
}

function improveReadabilityWithParameter(originalHtml, openingChar, closingChar, spanClass, separatorCharsIncluded) {

	let openingCharPosition = -1;

	let nesting = 0;
	for (let i = 0; i < originalHtml.length && openingCharPosition == -1; i++) {
		let char = originalHtml.charAt(i);
		if (nesting == 0 && char == openingChar) {
			openingCharPosition = i;
		} else {
			if (char == "<") {
				nesting++;
			}
			if (char == ">") {
				nesting--;
			}
		}
	}

	if (openingCharPosition != -1) {

		let closingCharPosition = -1;

		nesting = 0;
		for (let i = openingCharPosition + 1; i < originalHtml.length && closingCharPosition == -1; i++) {
			let char = originalHtml.charAt(i);
			if (nesting == 0 && char == closingChar) {
				closingCharPosition = i;
			} else {
				if (char == "(" || char == "<") {
					nesting++;
				}
				if (char == ")" || char == ">") {
					nesting--;
					if (nesting < 0) {
						
						let newHtml = "";
						newHtml += originalHtml.substring(0, i + 1);
						newHtml += improveReadabilityWithParameter(
								originalHtml.substring(i + 1),
								openingChar,
								closingChar,
								spanClass,
								separatorCharsIncluded
								);
						return newHtml;
					}
				}
				
			}
		}

		if (closingCharPosition != -1) {
			let newHtml = "";
			newHtml += originalHtml.substring(0, openingCharPosition);
			if (!separatorCharsIncluded) {
				newHtml += openingChar;
			}
			newHtml += '<span class="' + spanClass + '">';
			if (separatorCharsIncluded) {
				newHtml += openingChar;
			}
			newHtml += improveReadabilityWithParameter(
					originalHtml.substring(openingCharPosition + 1, closingCharPosition),
					openingChar,
					closingChar,
					spanClass,
					separatorCharsIncluded
					);
			if (separatorCharsIncluded) {
				newHtml += closingChar;
			}
			newHtml += '</span>';
			if (!separatorCharsIncluded) {
				newHtml += closingChar;
			}
			newHtml += improveReadabilityWithParameter(
					originalHtml.substring(closingCharPosition + 1),
					openingChar,
					closingChar,
					spanClass,
					separatorCharsIncluded
					);
			return newHtml;

		} else {
			let newHtml = "";
			newHtml += originalHtml.substring(0, openingCharPosition + 1);
			newHtml += improveReadabilityWithParameter(
					originalHtml.substring(openingCharPosition + 1),
					openingChar,
					closingChar,
					spanClass,
					separatorCharsIncluded
					);
			return newHtml;
		}

		
		
	} else {
		return originalHtml;
	}
}

function imporveUrlReadability(originalHtml) {
	return imporveUrlWithProtocolReadability(
			imporveUrlWithProtocolReadability(
					originalHtml,
					"http"
					),
				"https"
			);
}

function imporveUrlWithProtocolReadability(originalHtml, protocol) {

	let urlStartPosition = originalHtml.indexOf(protocol + "://");

	if (urlStartPosition == -1) {
		return originalHtml;
	}

	let urlEndPosition = urlStartPosition;
	let founded = false;
	while (urlEndPosition < originalHtml.length && founded == false) {
		let char = originalHtml.charAt(urlEndPosition);
		if (char == " " || char == "\n") {
			founded = true;
		} else {
			urlEndPosition++;
		}
	}

	let url = originalHtml.substring(urlStartPosition, urlEndPosition);

	let newHtml = "";
	newHtml += originalHtml.substring(0, urlStartPosition);
	newHtml += '<a href="';
	newHtml += url;
	newHtml += '">';
	newHtml += url;
	newHtml += '</a>';
	newHtml += imporveUrlReadability(originalHtml.substring(urlEndPosition));
	return newHtml;

}
