<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/conversations.css" />" />
<script type="text/javascript" src="<c:url value="/js/conversations.js" />"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Conversation" />
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
							<div class="
							fullSize resume
							${ conversationResume.conversation eq requestScope.selectedConversation ? 'selectedConversation' : '' }
							">
								<div class="conversationTitle">
									<c:out value="${ conversationResume.conversation.title }" />
								</div>

								<div class="participants">
									<c:forEach items="${ conversationResume.participants }" var="participant">
										<c:if test="${participant ne requestScope.user}">
											<c:out value="${ participant.nickname }" />
										</c:if>
									</c:forEach>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>

			<div id="current">
				<c:set var="currentConversationServletPath" value="/CurrentConversation" />
				<jsp:include page="${ currentConversationServletPath }">
					<jsp:param name="conversationId" value="${ selectedConversation.id }" />
				</jsp:include>
			</div>

		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>
