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

	let messagesDiv = document.getElementById("messages");
	messagesDiv.scrollTop = messagesDiv.scrollHeight;

	let form = $('#messageEditorForm');
	let textarea = $('#conversations #current #messageEditor textarea');
	let emojis = $('#conversations #current #messageEditor #sepcialChars span');
	let button = $('#conversations #current #messageEditor [type="button"]');

	let postMessage = function() {
		if (textarea.val() != "") {
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

	textarea.keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13' && !event.shiftKey) {
			event.preventDefault();
			postMessage();
		}
	});

	emojis.on('click', function(e) {
		textarea.val(textarea.val() + $(this).text());
		textarea.focus();
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

					current.find("#messages").append(
							'<div class="borderedTile fullWidth" data-messageId="' + message.id + '">'
							+ '<div class="messageMetaData">'
							+ '<div>'
							+ '<span style="color: ' + message.user.defaultColor + '">'
							+ toHtmlEquivalent(message.user.nickname)
							+ '</span> :'
							+ '</div>'
							+ '<div>'
							+ message.instant
							+'</div>'
							+ '</div>'
							+ '<div class="messageText">'
							+ toHtmlEquivalent(message.text)
							+ '</div>'
							+ '</div>');
	
					borderTiles();
					messagesDiv = document.getElementById("messages");
					messagesDiv.scrollTop = messagesDiv.scrollHeight;

				}
			};

		});
