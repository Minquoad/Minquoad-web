function detectConversationResumes() {

	let current = $("#conversations #current");

	let conversationResumeTiles = $("#conversations #list .borderedTile");

	conversationResumeTiles.on('click', function(e) {

		conversationResumeTile = $(this);

		current.empty();
		conversationResumeTiles.find(".resume").removeClass("selectedConversation");

		$.ajax({
			type : 'GET',
			url : $(this).attr("data-currentConversationUrl"),
			dataType : "html",
			success : function(data) {
				conversationResumeTile.find(".resume").addClass("selectedConversation");
				current.append(data);
				detectCurrentConversation();
				borderTiles();
			},
			error : function(jqXHR) {
				alert(jqXHR.status);
				window.location.replace("");
			}
		});
	});
}

function detectCurrentConversation() {

	let messagesDiv = document.getElementById("messages");
	messagesDiv.scrollTop = messagesDiv.scrollHeight;

	let form = $('#messageEditorForm');

	let textarea = $('#conversations #current #messageEditor textarea');
	let button = $('#conversations #current #messageEditor [type="button"]');

	button.on('click', function(e) {
		e.preventDefault();

		$.ajax({
			url : form.attr('action'),
			type : "POST",
			data : form.serialize(),
			success : function() {
				textarea.val("");
			},
			error : function(jqXHR) {
				alert(jqXHR.status);
				window.location.replace("");
			}
		});
	});

}

$(document).ready(function() {
	detectCurrentConversation();
	detectConversationResumes();
});
/*
$(document).ready(function() {
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
				error : function(jqXHR) {
					alert(jqXHR.status);
					window.location.replace("");
				}
			});
		}
	}, 3500);
});
*/