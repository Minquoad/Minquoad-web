<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<fmt:setBundle basename="resources.Home" var="homeBundle" />
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="" />
	</jsp:include>

	<div id="mainContainer" class="scrollableContainer">
		<h1 class="centeredText">
			<fmt:message key="HelloWorld" bundle="${ homeBundle }" />
		</h1>

		<img>
		<div class="tileContainer">
			<div class="borderedTile">
				<img class="padded" src="<c:url value="/img/so_good.png" />" />
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>