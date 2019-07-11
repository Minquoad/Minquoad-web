<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<fmt:setBundle basename="resources.BlockedSite" var="blockedSiteBundle" />
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer padded centeredText darkorangeText">
				<fmt:message key="BlockedSiteLine0" bundle="${ blockedSiteBundle }" />
				<br />
				🛠
				<br />
				<fmt:message key="BlockedSiteLine1" bundle="${ blockedSiteBundle }" />
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>