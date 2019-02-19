<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/includable/mainHeadContent.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Profile" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer">

				<c:out value="${ user.nickname }" />

			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/includable/footer.jsp"%>

</body>
</html>