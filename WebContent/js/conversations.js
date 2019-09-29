$(document).ready(function() {

	let subPageMenu = new SubPageMenu("conversationSubPageKey", $(".conversations #currentContainer"));

	$(".conversations #list .borderedTile").each(function() {
		let trigger = $(this);
		subPageMenu.createAndAddItem(trigger, function(data, textStatus, xhr) {
			if (trigger.attr("data-subPageKey") == "createConversation") {
				detectConversationCreation(data, textStatus, xhr);
			} else {
				detectCurrentConversation();
			}
		})
	});

	addRoledJsonWebsocketListener("conversationAddition", function(conversationResume) {

		let list = $(".conversations #list");

		let tileHtml = "";
		tileHtml += '<div class="borderedTile"';
		tileHtml += ' data-subPageUrl="';
		tileHtml += currentConversationUrl + "?conversationId=" + conversationResume.conversation.id;
		tileHtml += '" data-subPageKey="';
		tileHtml += conversationResume.conversation.id;
		tileHtml += '">';

		tileHtml += '<div class="fullSize resume">';

		tileHtml += '<div class="conversationTitle scrollableContainer">';
		tileHtml += toHtmlEquivalent(conversationResume.conversation.title);
		tileHtml += '</div>';

		tileHtml += '<div class="participants">';
		tileHtml += '';// TODO
		tileHtml += '</div>';

		tileHtml += '</div>';

		tileHtml += '</div>';

		list.prepend(tileHtml);

		let tile = list.find(".borderedTile").first();

		executeMainActions(tile);

		subPageMenu.createAndAddItem(tile, function(data, textStatus, xhr) {
			detectCurrentConversation();
		})

		tile.trigger('click');
	});
});

function detectConversationCreation(data, textStatus, xhr) {
	if (xhr.status == 201) {
		displayLoading($(".conversations #currentContainer"));
	}
}
