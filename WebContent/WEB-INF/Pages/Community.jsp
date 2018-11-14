<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/Includables/MainHeadContent.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/Includables/Header.jsp">
		<jsp:param name="pageTitle" value="Community" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<c:forEach items="${ users }" var="user" varStatus="status">
					<div class="borderedTile padded">
						<c:out value="${ user.nickname }" />
					</div>
				</c:forEach>

			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/Includables/Footer.jsp"%>

</body>
</html>