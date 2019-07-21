<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/conversations.css" />" />
<fmt:setBundle basename="resources.Conversations" var="conversationsBundle" />
<script>
	let originalMessageLabel = "<fmt:message key="jsOriginalMessage" bundle="${ conversationsBundle }" />";
	let messageEditionUrl = "<c:url value="/MessageEdition" />";
	let updateButtonTitle = "<fmt:message key="updateButtonTitle" bundle="${ conversationsBundle }" />";
	let cancelButtonTitle = "<fmt:message key="cancelButtonTitle" bundle="${ conversationsBundle }" />";
	let profileUrl = "<c:url value="/Profile" />";
</script>
<script type="text/javascript" src="<c:url value="/js/conversations.js" />"></script>
</head>
<body>
	<fmt:message key="Conversation" bundle="${ conversationsBundle }"
		var="conversationsLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ conversationsLabel }" />
	</jsp:include>

	<div id="mainContainer" class="conversations inlineBlockContainer">

		<div id="list" class="inlineBlock tileContainer scrollableContainer">
			<c:forEach items="${ requestScope.conversationResumes }"
				var="conversationResume">

				<c:url value="/CurrentConversation" var="currentConversationUrl">
					<c:param name="conversationId"
						value="${ conversationResume.conversation.id }" />
				</c:url>
				<div class="borderedTile" data-subPageUrl="${ currentConversationUrl }"
					data-subPageKey="${ conversationResume.conversation.id }">
					<div class="fullSize resume">
					
						<div class="conversationTitle">

							<c:if
								test="${ not conversationResume.conversation.isMainBetweenTwoUsers() }">
								<c:out value="${ conversationResume.conversation.title }" />
							</c:if>
							<c:if test="${ conversationResume.conversation.isMainBetweenTwoUsers() }">
								<c:forEach items="${ conversationResume.participants }"
									var="participant">
									<c:if test="${participant ne requestScope.user}">
										<c:out value="${ participant.nickname }" />
									</c:if>
								</c:forEach>
							</c:if>
						</div>

						<div class="participants">
							<c:if
								test="${ not conversationResume.conversation.isMainBetweenTwoUsers() }">
								<c:forEach items="${ conversationResume.participants }"
									var="participant">
									<c:if test="${participant ne requestScope.user}">
										<c:out value="${ participant.nickname }" />
									</c:if>
								</c:forEach>
							</c:if>
						</div>
						
					</div>
				</div>
				
			</c:forEach>

			<div class="borderedTile"
				data-subPageUrl="<c:url value="/ConversationCreation" />"
				data-subPageKey="createConversation">
				<div class="fullSize resume">
					<div class="conversationTitle newConversationResume">
						+ <fmt:message key="NewConversation" bundle="${ conversationsBundle }" />
					</div>
					<div class="participants"></div>
				</div>
			</div>


		</div>

		<div id="currentContainer"></div>

	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>
