<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<fmt:setBundle basename="resources.Community" var="communityBundle" />
</head>
<body>
	<fmt:message key="Community" bundle="${ communityBundle }" var="communityLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ communityLabel }" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<c:forEach items="${ requestScope.users }" var="showedUser">
					<c:if test="${showedUser != requestScope.user}">

						<div class="borderedTile">

							<div class="dynamicMenuTrigger padded">
								<c:out value="${ showedUser.nickname }" />
								<div class="dynamicMenu">
									<c:url value="/Profile" var="profileUrl">
										<c:param name="userId" value="${showedUser.id}" />
									</c:url>
									<a class="dynamicMenuItem" href="${profileUrl}"> Profile </a>
									<c:url value="/Conversations" var="conversationsUrl">
										<c:param name="userId" value="${showedUser.id}" />
									</c:url>
									<a class="dynamicMenuItem" href="${conversationsUrl}"> Conversation </a>
								</div>
							</div>

						</div>

					</c:if>
				</c:forEach>

			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>