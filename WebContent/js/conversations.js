function detectConversationResumes() {

	let current = $("#conversations #current");
	let conversationResumeTiles = $("#conversations #list .borderedTile");

	conversationResumeTiles.on("click", function(e) {

		let conversationResumeTile = $(this);

		conversationResumeTiles.find(".resume").removeClass("selectedConversation");
		displayLoading(current);
		conversationResumeTile.find(".resume").addClass("selectedConversation");

		$.ajax({
			type : "GET",
			url : conversationResumeTile.attr("data-currentConversationUrl"),
			dataType : "html",
			success : function(data) {
				current.empty();
				current.append(data);
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
		textarea.val(textarea.val()+$(this).text());
		textarea.focus();
	});
}

$(document).ready(function() {

	detectConversationResumes();
	$("#conversations #list .borderedTile .selectedConversation").trigger("click");

	setInterval(function() {
		let messages = $("#messages");
		if (messages) {
			$.ajax({
				type : "GET",
				url : messages.attr("data-unseenMessagesUrl"),
				dataType : "html",
				success : function(data) {
					if (data.trim() != "") {
						messages.append(data);
						borderTiles();

						let messagesDiv = document.getElementById("messages");
						messagesDiv.scrollTop = messagesDiv.scrollHeight;
					}
				},
				error : function(err) {
					handleAjaxError(err);
				}
			});
		}
	}, 3500);

});
