<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Sign up" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer">
				<form method="post" action="<c:url value="/UpSigning" />" accept-charset="UTF-8">
					<p>
						<label for="mailAddress">Mail address : </label> <input type="text" name="mailAddress" id="mailAddress" maxlength="${ requestScope.mailAddressMaxlength }"
							<c:if test="${not empty requestScope.prefilledMailAddress}">
								value="<c:out value="${ requestScope.prefilledMailAddress }" />"
								</c:if> />
					</p>
					<p>
						<label for="nickname">Nickname : </label> <input type="text" name="nickname" id="nickname" maxlength="${ requestScope.nicknameMaxlength }"
							<c:if test="${not empty requestScope.prefilledNickname}">
								value="<c:out value="${ requestScope.prefilledNickname }" />"
								</c:if> />
					</p>
					<p>
						<label for="password">Password : </label> <input type="password" name="password" id="password" />
					</p>
					<p>
						<label for="passwordConfirmation">Password : </label> <input type="password" name="passwordConfirmation" id="passwordConfirmation" />
					</p>
					<jsp:include page="/WEB-INF/includable/form/formProblems.jsp">
						<jsp:param name="formProblems" value="formProblems" />
					</jsp:include>
					<div>
						<input type="submit" value="Sign up" />
					</div>
				</form>
				<p>
					<a href="LogIn">Already registered? Log in.</a>
				</p>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>