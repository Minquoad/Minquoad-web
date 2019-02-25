function detectConversationTriggers() {

	onversationTriggers = $(".conversationTrigger");

	onversationTriggers.off("click");

	onversationTriggers.on("click", function(e) {
		$("body").append("<div class='movableDiv'>"+$(this).attr("data-userId")+"</div>");
	});

}
