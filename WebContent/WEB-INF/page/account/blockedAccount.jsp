<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<fmt:setBundle basename="resources.BlockedAccount" var="blockedAccountBundle" />
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer padded centeredText redText">
				<div>⛔</div>
				<div class="padded">
					<fmt:message key="AccountBlocked" bundle="${ blockedAccountBundle }" />
				</div>
				<div>
					<fmt:message key="YourAccountWillBeUnlockedOnThe"
						bundle="${ blockedAccountBundle }" />
					<span class="dateToFormat" data-format="1"><c:out
							value="${ requestScope.user.unblockInstant }" /></span>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>