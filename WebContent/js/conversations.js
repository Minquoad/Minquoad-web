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
	borderTiles();
	detectDynamicMenuTriggers();
	
	$("#conversations .messageText").each(function() {
		let messageText = $(this);
		messageText.html(improveReadability(messageText.html().trim()));
	});

	let messagesDiv = document.getElementById("messages");
	messagesDiv.scrollTop = messagesDiv.scrollHeight;

	let form = $('#messageEditorForm');
	let textarea = $('#conversations #current #messageEditor textarea');
	let emojis = $('#conversations #current #messageEditor #sepcialChars span');
	let button = $('#conversations #current #messageEditor [type="button"]');

	let postMessage = function() {
		if (textarea.val().trim() != "") {
			$.ajax({
				url : form.attr('action'),
				type : "POST",
				data : form.serialize(),
				success : function() {
					textarea.val("");
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

$(document).ready(
		function() {

			detectConversationResumes();
			$("#conversations #list .borderedTile .selectedConversation").trigger("click");

			creteWebsocketWithRole("conversationUpdating").onmessage = function(e) {
				let message = JSON.parse(e.data);

				let current = $("#current");

				if (current.attr("data-conversationid") == message.conversation) {

					let messageDiv = "";
					messageDiv += '<div class="borderedTile fullWidth" data-messageId="';
					messageDiv += message.id;
					messageDiv += '">';
					messageDiv += '<div class="messageMetaData">';
					messageDiv += '<div>';
					messageDiv += '<span style="color: ';
					messageDiv += message.user.defaultColor;
					messageDiv += '">';
					messageDiv += toHtmlEquivalent(message.user.nickname);
					messageDiv += '</span> :';
					messageDiv += '</div>';
					messageDiv += '<div>';
					messageDiv += message.instant;
					messageDiv += '</div>';
					messageDiv += '</div>';
					messageDiv += '<div class="messageText">';
					messageDiv += improveReadability(toHtmlEquivalent(message.text));
					messageDiv += '</div>';
					messageDiv += '</div>';
					
					current.find("#messages").append(messageDiv);
					
					borderTiles();
					messagesDiv = document.getElementById("messages");
					messagesDiv.scrollTop = messagesDiv.scrollHeight;

				}
			};

		});
