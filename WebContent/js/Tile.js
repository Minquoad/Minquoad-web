$(document).ready(function() {

	function borderTiles() {

		$('.bottomTileBorderAdder>.borderedTile').unwrap();
		$('.rightTileBorderAdder>.borderedTile').unwrap();
		$('.topTileBorderAdder>.borderedTile').unwrap();
		$('.leftTileBorderAdder>.borderedTile').unwrap();

		$(".borderedTile").wrap("<div class='leftTileBorderAdder'></div>");
		$(".borderedTile").wrap("<div class='topTileBorderAdder'></div>");
		$(".borderedTile").wrap("<div class='rightTileBorderAdder'></div>");
		$(".borderedTile").wrap("<div class='bottomTileBorderAdder'></div>");
	}

	borderTiles();

});
