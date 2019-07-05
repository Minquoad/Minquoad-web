function borderTiles(container) {

	if (container.hasClass("borderedTile")) {
		borderTile(container);
	}

	container.find(".borderedTile").each(function() {
		borderTile($(this));
	});
}

function borderTile(borderedTile) {
	if (!borderedTile.children().first().hasClass("topTileBorderAdder")) {
		borderedTile.wrapInner("<div class='borderedTilebackgroundCorrector'></div>");
		borderedTile.wrapInner("<div class='leftTileBorderAdder'></div>");
		borderedTile.wrapInner("<div class='bottomTileBorderAdder'></div>");
		borderedTile.wrapInner("<div class='rightTileBorderAdder'></div>");
		borderedTile.wrapInner("<div class='topTileBorderAdder'></div>");
	}
}
