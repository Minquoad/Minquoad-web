let minMoveBeforTranslation = 2;

let movableDivs;

function correctPositionIfNeeded(mousedownedMovableDiv) {

	let overflowing;

	overflowing = parseInt(mousedownedMovableDiv.css("left"));
	if (overflowing < 0) {
		mousedownedMovableDiv.css("left", '-=' + overflowing);
	}

	overflowing = parseInt(mousedownedMovableDiv.css("top"));
	if (overflowing < 0) {
		mousedownedMovableDiv.css("top", '-=' + overflowing);
	}

	overflowing = parseInt(mousedownedMovableDiv.css("right"));
	if (overflowing < 0) {
		mousedownedMovableDiv.css("left", '+=' + overflowing);
	}

	overflowing = parseInt(mousedownedMovableDiv.css("bottom"));
	if (overflowing < 0) {
		mousedownedMovableDiv.css("top", '+=' + overflowing);
	}

}

function correctAllPositionIfNeeded() {
	if (movableDivs) {
		movableDivs.each(function() {
			correctPositionIfNeeded($(this));
		});
	}
}

function detectMovableDivs() {
	movableDivs = $(".movableDiv");
	correctAllPositionIfNeeded();
}

function deselctEverything() {
	let sel = window.getSelection ? window.getSelection() : document.selection;
	if (sel) {
		if (sel.removeAllRanges) {
			sel.removeAllRanges();
		} else if (sel.empty) {
			sel.empty();
		}
	}
}

$(document).mousedown(
		function(e) {

			let mousedownedMovableDivIfExists = $(event.target).closest(
					".movableDiv");

			if (mousedownedMovableDivIfExists.length) {

				let oldPos = {
					x : e.pageX,
					y : e.pageY
				};

				$(window).on("mousemove", function(e) {

					deselctEverything();

					let newPos = {
						x : e.pageX,
						y : e.pageY
					};

					let xVector = newPos.x - oldPos.x;
					let yVector = newPos.y - oldPos.y;

					if (Math.abs(xVector) >= minMoveBeforTranslation
							|| Math.abs(yVector) >= minMoveBeforTranslation) {

						mousedownedMovableDivIfExists.css("left", '+='
								+ (newPos.x - oldPos.x));
						mousedownedMovableDivIfExists.css("top", '+='
								+ (newPos.y - oldPos.y));

						correctPositionIfNeeded(mousedownedMovableDivIfExists);

						oldPos = newPos;
					}
				});

				$(window).mouseup(function() {
					$(window).off("mousemove");
				});

			}

		});

$(window).resize(function() {
	correctAllPositionIfNeeded();
});
