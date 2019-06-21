function borderTiles(container) {

	container.find(".borderedTile").each(function() {
		let borderedTile = $(this);

		if (!borderedTile.children().first().hasClass("topTileBorderAdder")) {
			borderedTile.wrapInner("<div class='borderedTilebackgroundCorrector'></div>");
			borderedTile.wrapInner("<div class='leftTileBorderAdder'></div>");
			borderedTile.wrapInner("<div class='bottomTileBorderAdder'></div>");
			borderedTile.wrapInner("<div class='rightTileBorderAdder'></div>");
			borderedTile.wrapInner("<div class='topTileBorderAdder'></div>");
		}
	});

}
