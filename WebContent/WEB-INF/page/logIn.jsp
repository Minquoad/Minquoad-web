<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/includable/mainHeadContent.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Log In" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer">
				<form method="post" action="LogIn" accept-charset="UTF-8">
					<p>
						<label for="nickname">Nickname : </label> <input type="text"
							name="nickname" id="nickname"
							<c:if test="${not empty prefilledNickname}">
								value="<c:out value="${ prefilledNickname }" />"
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
					<a href="SignUp">New user? Create an account.</a>
				</p>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/includable/footer.jsp"%>
</body>
</html>