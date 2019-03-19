<div id="title" class="centererContainer">
	<div class="totallyCenteredContainer">
		<c:out value="${ requestScope.conversation.title }" />
	</div>
</div>

<c:url value="/UnseenMessages" var="unseenMessagesUrl">
	<c:param name="conversationId" value="${ requestScope.conversation.id }" />
</c:url>
<div id="messages" class="tileContainer" data-unseenMessagesUrl="${ unseenMessagesUrl }">
	<jsp:include page="/WEB-INF/includable/messageTiles.jsp">
		<jsp:param name="messages" value="messages" />
	</jsp:include>
</div>
<div id="messageEditor">
	<form id="messageEditorForm" action="<c:url value="/MessageAddition" />">
		<input type="hidden" name="conversationId" value="${ requestScope.conversation.id }">
		<textarea name="text" placeholder="Type your message here."></textarea>
		<input type="button" value="Send" />
	</form>
</div>
