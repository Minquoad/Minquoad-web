<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Log In" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer">
				<form method="post" action="<c:url value="/InLoging" />" accept-charset="UTF-8">
					<p>
						<label for="mailAddress">Mail address : </label> <input type="text"
							name="mailAddress" id="nickname"
							<c:if test="${not empty requestScope.prefilledMailAddress}">
								value="<c:out value="${ requestScope.prefilledMailAddress }" />"
								</c:if> />
					</p>
					<p>
						<label for="password">Password : </label> <input type="password"
							name="password" id="password" />
					</p>
					<jsp:include page="/WEB-INF/includable/form/formProblems.jsp">
						<jsp:param name="formProblems" value="formProblems" />
					</jsp:include>
					<div>
						<input type="submit" value="Log in" />
					</div>
				</form>
				<p>
					<a href="<c:url value="/UpSigning" />">New user? Create an account.</a>
				</p>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>