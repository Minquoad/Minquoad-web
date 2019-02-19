<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/includable/mainHeadContent.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Community" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<c:forEach items="${ users }" var="user" varStatus="status">
					<c:if test="${user != sessionUser}">

						<c:url value="Profile" var="profileUrl">
							<c:param name="userId" value="${user.id}" />
						</c:url>
						<a class="borderedTile padded"
							href="<c:out value="${profileUrl}" />">
							<span><c:out value="${ user.nickname }" /></span>
						</a>

					</c:if>
				</c:forEach>

			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/includable/footer.jsp"%>

</body>
</html>