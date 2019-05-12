<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/conversations.css" />" />
<script type="text/javascript" src="<c:url value="/js/conversations.js" />"></script>
<fmt:setBundle basename="resources.Conversations" var="conversationsBundle" />
</head>
<body>
	<fmt:message key="Conversation" bundle="${ conversationsBundle }" var="conversationsLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ conversationsLabel }" />
	</jsp:include>

	<div id="mainContainer">
		<div id="conversations" class="fullSize inlineBlockContainer">

			<div id="list">
				<div class="fullSize tileContainer">
					<c:forEach items="${ requestScope.conversationResumes }" var="conversationResume">

						<c:url value="/CurrentConversation" var="currentConversationUrl">
							<c:param name="conversationId" value="${ conversationResume.conversation.id }" />
						</c:url>
						<div class="borderedTile" data-currentConversationUrl="${ currentConversationUrl }">
							<div
								class="
							fullSize resume
							${ conversationResume.conversation eq requestScope.selectedConversation ? 'selectedConversation' : '' }
							">
								<div class="conversationTitle">

									<c:if test="${ not conversationResume.conversation.isMainBetweenTwoUsers() }">
										<c:out value="${ conversationResume.conversation.title }" />
									</c:if>
									<c:if test="${ conversationResume.conversation.isMainBetweenTwoUsers() }">
										<c:forEach items="${ conversationResume.participants }" var="participant">
											<c:if test="${participant ne requestScope.user}">
												<c:out value="${ participant.nickname }" />
											</c:if>
										</c:forEach>
									</c:if>
								</div>

								<div class="participants">
									<c:if test="${ not conversationResume.conversation.isMainBetweenTwoUsers() }">
										<c:forEach items="${ conversationResume.participants }" var="participant">
											<c:if test="${participant ne requestScope.user}">
												<c:out value="${ participant.nickname }" />
											</c:if>
										</c:forEach>
									</c:if>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>

			<div id="currentContainer"></div>

		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />
	<jsp:include page="/WEB-INF/includable/utilityDiv.jsp" />

</body>
</html>
