<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="com.minquoad.entity.User"%>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<fmt:setBundle basename="resources.UpSigning" var="upSigningBundle" />
</head>
<body>
	<fmt:message key="SignUp" bundle="${ upSigningBundle }" var="upSigningLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ upSigningLabel }" />
	</jsp:include>

	<div id="mainContainer" class="scrollableContainer blockFlex">
		<div class="totallyCenteredContainer tileContainer">
			<div class="borderedTile">
				<form class="padded" method="post" action="<c:url value="/UpSigning" />"
					accept-charset="UTF-8">
					<p>
						<label for="mailAddress">
							<fmt:message key="MailAddress" bundle="${ upSigningBundle }" />
							:
						</label>
						<input type="email" name="mailAddress" id="mailAddress"
							maxlength="${ User.MAIL_ADDRESS_MAX_LENGTH }"
							value="<c:out value="${ requestScope.form.fields.mailAddress.value }" />" />
						<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
							<jsp:param name="formKey" value="form" />
							<jsp:param name="fieldName" value="mailAddress" />
						</jsp:include>
					</p>
					<p>
						<label for="nickname">
							<fmt:message key="Nickname" bundle="${ upSigningBundle }" />
							:
						</label>
						<input type="text" name="nickname" id="nickname"
							maxlength="${ User.NICKNAME_MAX_LENGTH }"
							value="<c:out value="${ requestScope.form.fields.nickname.value }" />" />
						<c:if test="${ not empty requestScope.form }">
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="nickname" />
							</jsp:include>
						</c:if>
					</p>
					<p>
						<label for="password">
							<fmt:message key="Password" bundle="${ upSigningBundle }" />
							:
						</label>
						<input type="password" name="password" id="password"
							value="<c:out value="${ requestScope.form.fields.password.value }" />" />
						<c:if test="${ not empty requestScope.form }">
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="password" />
							</jsp:include>
						</c:if>
					</p>
					<p>
						<label for="passwordConfirmation">
							<fmt:message key="PasswordConfirmation" bundle="${ upSigningBundle }" />
							:
						</label>
						<input type="password" name="passwordConfirmation"
							id="passwordConfirmation"
							value="<c:out value="${ requestScope.form.fields.passwordConfirmation.value }" />" />
						<c:if test="${ not empty requestScope.form }">
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="passwordConfirmation" />
							</jsp:include>
						</c:if>
					</p>
					<p>
						<label for="upSigningCode">
							<fmt:message key="UpSigningCode" bundle="${ upSigningBundle }" />
							:
						</label>
						<input type="password" name="upSigningCode" id="upSigningCode"
							value="<c:out value="${ requestScope.form.fields.upSigningCode.value }" />" />
						<c:if test="${ not empty requestScope.form }">
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="upSigningCode" />
							</jsp:include>
						</c:if>
					</p>
					<div>
						<input type="submit"
							value="<fmt:message key="SignUp" bundle="${ upSigningBundle }" />" />
					</div>
					<p>
						<a href="<c:url value="/InLoging" />">
							<fmt:message key="logInLink" bundle="${ upSigningBundle }" />
						</a>
					</p>
				</form>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>