<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/MainHeadContent.jsp"%>
</head>
<body>
	<c:set var="pageTitle" value="Sign up" scope="request"/>
	<%@ include file="/WEB-INF/Header.jsp"%>
	
	<div id="mainContainer">
		<div class="scrollableContainer">
			<div class="centererContainer">
				<div class="totallyCenteredContainer">
					<form method="post" action="signUp" accept-charset="UTF-8">
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
						<c:set var="currentFormProblems"
							value="${ formProblems }" scope="request" />
						<%@ include file="/WEB-INF/Form/FormProblems.jsp"%>
						<div>
							<input type="submit" value="Sign up" />
						</div>
					</form>
					<p>
						<a href="logIn">Already registered? Log in.</a>
					</p>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/Footer.jsp"%>
</body>