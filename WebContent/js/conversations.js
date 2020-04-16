$(document).ready(function() {

	let subPageMenu = new SubPageMenu("conversationSubPageKey", $(".conversations #currentContainer"));

	$(".conversations #list .borderedTile").each(function() {
		let trigger = $(this);
		subPageMenu.createAndAddItem(trigger, function(data, textStatus, xhr) {
			if (trigger.attr("data-subPageKey") == "createConversation") {
				detectConversationCreation(data, textStatus, xhr);
			} else {
				detectCurrentConversation(data, textStatus, xhr);
			}
		})
	});

	addRoledJsonWebsocketListener("conversationAddition", function(conversationResume) {

		let list = $(".conversations #list");

		list.prepend(generateConversationResumeHtml(conversationResume));

		let tile = list.find(".borderedTile").first();

		executeMainActions(tile);

		subPageMenu.createAndAddItem(tile, null);

	});
});

function detectConversationCreation(data, textStatus, xhr) {
	if (xhr.status == 201) {
		displayLoading($(".conversations #currentContainer"));
		waitConversationAppearance(data.id);
	}
}

function waitConversationAppearance(conversationId) {

	let tile = $(".conversations #list .borderedTile[data-subpagekey=" + conversationId + "]");

	if (tile.length != 0) {
		tile.trigger('click');

	} else {
		setTimeout(waitConversationAppearance, 200, conversationId);
	}
}

// HTML generators

function generateConversationResumeHtml(conversationResume) {

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
	tileHtml += '';console.log(conversationResume);// TODO
	tileHtml += '</div>';

	tileHtml += '</div>';

	tileHtml += '</div>';

	return tileHtml;
}
