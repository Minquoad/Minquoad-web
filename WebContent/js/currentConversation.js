$(document).ready(function() {

	addRoledJsonWebsocketListener("messageAddition", function(message) {

		let current = $("#current");

		if (!!current && (current.attr("data-conversationId") == message.conversation)) {

			let messageTile = generateMessageTile(message);
			
			let messages = current.find("#messages");
			messages.append(messageTile);
			let lastMessage = messages.children().last();
			executeMainActions(lastMessage);

			messagesDiv = document.getElementById("messages");
			messagesDiv.scrollTop = messagesDiv.scrollHeight;

			detectMessageEditionButtons(lastMessage);
		}

	});

	addRoledJsonWebsocketListener("messageEdition", function(message) {

		let current = $("#current");

		if (!!current && (current.attr("data-conversationId") == message.conversation)) {

			let tile = current.find("[data-messageId=" + message.id + "]");

			tile.attr("title", originalMessageLabel + toHtmlEquivalent(message.text));
			tile.find(".messageText").html("<span class='keepLineBreak readabilityToImprove'>" + toHtmlEquivalent(message.editedText) + "</span>");

			executeMainActions(tile);

			let name = tile.find(".name");
			if (name.text().indexOf("🖉") == -1) {
				name.append("🖉");
			}
		}
	});
});

function detectCurrentConversation(data, textStatus, xhr) {

	if (xhr.status == 204) {
		let list = $(".conversations #list");
		list.find(".selectedSubPage").remove();
		list.find(".borderedTile").first().trigger("click");

	} else {

		let current = $(".conversations #current");
		if (current.length) {

			detectMessageEditionButtons(current);

			let messagesDiv = document.getElementById("messages");
			messagesDiv.scrollTop = messagesDiv.scrollHeight;

			let form = current.find('#messageEditorForm');
			let textarea = form.find('textarea');
			let fileField = form.find('input[type="file"]');
			let emojis = form.find('#sepcialChars .dynamicMenuItem span');
			let button = form.find('[type="button"]');

			let postMessage = function() {
				if (textarea.val().trim() != "" || fileField.val()) {
					submitForm(form);
					textarea.val("");
					fileField.val("");
					fileField.trigger("change");
				}
			};

			button.on('click', function(e) {
				e.preventDefault();
				postMessage();
			});

			emojis.on('click', function(e) {
				textarea.val(textarea.val() + $(this).text());
				textarea.focus();
			});

			textarea.keypress(function(event) {
				var keycode = (event.keyCode ? event.keyCode : event.which);
				if (!event.shiftKey) {

					if (keycode == '13') {
						event.preventDefault();
						postMessage();
					}
				}
			});
		}
	}
}

function detectMessageEditionButtons(container) {
	container.find(".messageEditionButton").each(function() {
		let messageEditionButton = $(this);

		messageEditionButton.on('click', function(e) {
			let tile = messageEditionButton.closest(".borderedTile");
			let messageTextDiv = tile.find(".messageText");

			if (messageTextDiv.find("form").length == 0) {

				let messageEditionFromHtml = generateMessageEditionFromHtml(tile.attr("data-messageId"), messageTextDiv.children("span").text());

				let oldHtml = messageTextDiv.html();
				messageTextDiv.empty();
				messageTextDiv.append(messageEditionFromHtml);
				executeMainActions(messageTextDiv);

				let messageEditionForm = messageTextDiv.find('.messageEditionForm');
				let textarea = messageEditionForm.find('textarea');
				let sendButton = messageEditionForm.find('[type="button"][value="⭆"]');
				let cancelButton = messageEditionForm.find('[type="button"][value="✖"]');

				let postMessage = function() {
					submitForm(messageEditionForm);
					displayLoading(messageTextDiv);
				};

				cancelButton.on('click', function(e) {
					e.preventDefault();
					messageTextDiv.empty();
					messageTextDiv.append(oldHtml);
				});

				sendButton.on('click', function(e) {
					e.preventDefault();
					postMessage();
				});

				textarea.keypress(function(event) {
					var keycode = (event.keyCode ? event.keyCode : event.which);
					if (!event.shiftKey) {

						if (keycode == '13') {
							event.preventDefault();
							postMessage();
						}
					}
				});
			}
		});
	});
}

// HTML generators

function generateMessageTile(message) {

	let messageDiv = "";
	messageDiv += '<div class="borderedTile fullWidth" data-messageId="';
	messageDiv += message.id;
	messageDiv += '" data-messageUserId="';
	messageDiv += message.user.id;
	messageDiv += '">';
	messageDiv += '<div class="messageMetaData">';
	messageDiv += '<div class="name">';
	messageDiv += '<a href="';
	messageDiv += profileUrl + "?targetUserId=" + message.user.id;
	messageDiv += '">';
	messageDiv += '<span style="color: ';
	messageDiv += message.user.defaultColor;
	messageDiv += '"> ';
	messageDiv += toHtmlEquivalent(message.user.nickname);
	messageDiv += '</span> : ';
	messageDiv += '</a>';
	messageDiv += '</div>';
	messageDiv += '<div>';

	if (userId == message.user.id) {
		messageDiv += '<span class="messageEditionButton">🖉</span>';
	}

	messageDiv += ' <span class="dateToFormat" data-format="1">';
	messageDiv += message.instant;
	messageDiv += '</span> ';
	messageDiv += '</div>';
	messageDiv += '</div>';
	messageDiv += '<div class="messageText"><span class="keepLineBreak readabilityToImprove">';
	messageDiv += toHtmlEquivalent(message.text);
	messageDiv += '</span></div>';

	let messageFile = message.messageFile;
	if (messageFile != null) {
		if (messageFile.image) {
			messageDiv += '<div class="messageImageContainer">';
			messageDiv += '<img src="';
			messageDiv += imageDownloadUrl + "?id=" + messageFile.id;
			messageDiv += '" class="messageImage" />';
			messageDiv += '<div>';
		} else {
			messageDiv += '<a class="messageFileLink" href="';
			messageDiv += fileDownloadUrl + "?id=" + messageFile.id;
			messageDiv += '">';
			messageDiv += "📁";
			messageDiv += toHtmlEquivalent(messageFile.originalName);
			messageDiv += '</a>';
		}
	}

	messageDiv += '</div>';

	return messageDiv;
}

function generateMessageEditionFromHtml(messageId, textareaText) {

	let editionDiv = "";
	editionDiv += '<form class="fullSize messageEditionForm" method="post" action="';
	editionDiv += messageEditionUrl;
	editionDiv += '" accept-charset="UTF-8">';

	editionDiv += '<div>';
	editionDiv += '<input type="button" value="⭆" title="';
	editionDiv += updateButtonTitle;
	editionDiv += '" />';
	editionDiv += '<input type="button" value="✖" title="';
	editionDiv += cancelButtonTitle;
	editionDiv += '" />';
	editionDiv += '</div>';

	editionDiv += '<input type="hidden" name="messageId" value="';
	editionDiv += messageId;
	editionDiv += '"/>';
	editionDiv += '<textarea name="newText" class="improvedTextField">';
	editionDiv += textareaText;
	editionDiv += '</textarea>';

	editionDiv += '</form>';

	return editionDiv;
}
