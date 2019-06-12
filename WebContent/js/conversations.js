function detectConversationResumes() {

	let currentContainer = $("#conversations #currentContainer");
	let conversationResumeTiles = $("#conversations #list .borderedTile");

	conversationResumeTiles.on("click", function(e) {

		let conversationResumeTile = $(this);

		conversationResumeTiles.find(".resume").removeClass("selectedConversation");
		displayLoading(currentContainer);
		conversationResumeTile.find(".resume").addClass("selectedConversation");

		$.ajax({
			type : "GET",
			url : conversationResumeTile.attr("data-currentConversationUrl"),
			dataType : "html",
			success : function(data) {
				currentContainer.empty();
				currentContainer.append(data);
				detectCurrentConversation();
			},
			error : function(err) {
				handleAjaxError(err);
			}
		});
	});
}

function detectCurrentConversation() {
	formatDates();
	borderTiles();
	detectDynamicMenuTriggers();

	let current = $("#conversations #current");

	detectMessageEditionButtons(current);

	current.find(".messageText").each(function() {
		let messageText = $(this);
		messageText.html(improveReadability(messageText.html().trim()));
	});

	let messagesDiv = document.getElementById("messages");
	messagesDiv.scrollTop = messagesDiv.scrollHeight;

	let form = current.find('#messageEditorForm');
	let textarea = form.find('textarea');
	let emojis = form.find('#sepcialChars .dynamicMenuItem span');
	let button = form.find('[type="button"]');

	let postMessage = function() {
		if (textarea.val().trim() != "") {

			let formData = form.serialize();
			textarea.val("");
			
			$.ajax({
				url : form.attr('action'),
				type : "POST",
				data : formData,
				success : function() {
				},
				error : function(err) {
					handleAjaxError(err);
				}
			});
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

	improveTextField(textarea);

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

function detectMessageEditionButtons(container) {
	container.find(".messageEditionButton").each(function() {
		let messageEditionButton = $(this);

		messageEditionButton.on('click', function(e) {
			let tile = messageEditionButton.closest(".borderedTile");
			let messageTextDiv = tile.find(".messageText");

			if (messageTextDiv.find("form").length == 0) {

				let editionDiv = "";
				editionDiv += '<form class="fullSize" method="post" action="';
				editionDiv += messageEditionUrl;
				editionDiv += '">';

				editionDiv += '<div>';
				editionDiv += '<input type="button" value="⭆" title="';
				editionDiv += updateButtonTitle;
				editionDiv += '" />';
				editionDiv += '<input type="button" value="✖" title="';
				editionDiv += cancelButtonTitle;
				editionDiv += '" />';
				editionDiv += '<input type="button" value="🗑" title="';
				editionDiv += deleteButtonTitle;
				editionDiv += '" />';
				editionDiv += '</div>';

				editionDiv += '<input type="hidden" name="messageId" value="';
				editionDiv += tile.attr("data-messageId");
				editionDiv += '"/>';
				editionDiv += '<textarea name="newText">';
				editionDiv += messageTextDiv.text();
				editionDiv += '</textarea>';

				editionDiv += '</form>';

				let oldHtml = messageTextDiv.html();
				messageTextDiv.empty();
				messageTextDiv.append(editionDiv);

				let form = messageTextDiv.find('form');
				let textarea = form.find('textarea');
				let sendButton = form.find('[type="button"][value="⭆"]');
				let cancelButton = form.find('[type="button"][value="✖"]');
				let deleteButton = form.find('[type="button"][value="🗑"]');

				improveTextField(textarea);

				let postMessage = function() {
					if (textarea.val().trim() != "") {

						let data = form.serialize();
						displayLoading(messageTextDiv);

						$.ajax({
							url : form.attr('action'),
							type : "POST",
							data : data,
							success : function() {
							},
							error : function(err) {
								handleAjaxError(err);
							}
						});
					}
				};

				cancelButton.on('click', function(e) {
					e.preventDefault();
					messageTextDiv.empty();
					messageTextDiv.append(oldHtml);
				});

				deleteButton.on('click', function(e) {
					e.preventDefault();
					alert("TODO");
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

$(document).ready(function() {

	detectConversationResumes();
	$("#conversations #list .borderedTile .selectedConversation").trigger("click");

	getWebsocketWithRole("conversationUpdating").onmessage = function(e) {
		let event = JSON.parse(e.data);

		if (event.enventKey == "MessageAddition") {
			let message = event.data;
			let current = $("#current");

			if (current.attr("data-conversationid") == message.conversation) {

				let messageDiv = "";
				messageDiv += '<div class="borderedTile fullWidth" data-messageId="';
				messageDiv += message.id;
				messageDiv += '" data-messageUserId="';
				messageDiv += message.user.id;
				messageDiv += '">';
				messageDiv += '<div class="messageMetaData">';
				messageDiv += '<div class="name">';
				messageDiv += '<span style="color: ';
				messageDiv += message.user.defaultColor;
				messageDiv += '">';
				messageDiv += toHtmlEquivalent(message.user.nickname);
				messageDiv += '</span> :';
				messageDiv += '</div>';
				messageDiv += '<div>';

				if (userId == message.user.id) {
					messageDiv += '<span class="messageEditionButton">🖉</span>';
				}

				messageDiv += ' ';
				messageDiv += '<span class="dateToFormat" data-format="1">';
				messageDiv += message.instant;
				messageDiv += '</span></div>';
				messageDiv += '</div>';
				messageDiv += '<div class="messageText">';
				messageDiv += improveReadability(toHtmlEquivalent(message.text));
				messageDiv += '</div>';
				messageDiv += '</div>';

				let messages = current.find("#messages");
				messages.append(messageDiv);

				formatDates();
				borderTiles();
				messagesDiv = document.getElementById("messages");
				messagesDiv.scrollTop = messagesDiv.scrollHeight;

				detectMessageEditionButtons(messages.children().last());
			}
		}

		if (event.enventKey == "MessageEdition") {
			let message = event.data;
			let current = $("#current");

			if (current.attr("data-conversationid") == message.conversation) {
				let tile = current.find("[data-messageId=" + message.id + "]");

				tile.attr("title", originalMessageLabel + toHtmlEquivalent(message.text));
				tile.find(".messageText").html(improveReadability(toHtmlEquivalent(message.editedText)));

				let name = tile.find(".name");
				if (name.text().indexOf("🖉") == -1) {
					name.append("🖉");
				}
			}
		}
	};

});
