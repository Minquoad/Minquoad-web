<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/MainHeadContent.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/Header.jsp">
		<jsp:param name="pageTitle" value="Sign up" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer">
			<div class="centererContainer">
				<div class="totallyCenteredContainer">
					<form method="post" action="SignUp" accept-charset="UTF-8">
						<p>
							<label for="nickname">Nickname : </label> <input type="text"
								name="nickname" id="nickname"
								maxlength="<c:out value="${ nicknameMaxlength }" />"
								<c:if test="${not empty prefilledNickname}">
								value="<c:out value="${ prefilledNickname }" />"
								</c:if> />
						</p>
						<p>
							<label for="password">Password : </label> <input type="password"
								name="password" id="password" />
						</p>
						<p>
							<label for="passwordConfirmation">Password : </label> <input
								type="password" name="passwordConfirmation"
								id="passwordConfirmation" />
						</p>
						<jsp:include page="/WEB-INF/Form/FormProblems.jsp">
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
	</div>

	<%@ include file="/WEB-INF/Footer.jsp"%>
</body>