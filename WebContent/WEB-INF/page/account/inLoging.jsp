<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<fmt:setBundle basename="resources.InLoging" var="inLogingBundle" />
</head>
<body>
	<fmt:message key="LogIn" bundle="${ inLogingBundle }" var="inLogingLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ inLogingLabel }" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">
				<div class="borderedTile">
					<form class="padded" method="post" action="<c:url value="/InLoging" />"
						accept-charset="UTF-8">
						<p>
							<label for="mailAddress">
								<fmt:message key="MailAddress" bundle="${ inLogingBundle }" />
								:
							</label>
							<input type="email" name="mailAddress" id="nickname"
								value="<c:out value="${ requestScope.form.fields.mailAddress.value }" />" />
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="mailAddress" />
							</jsp:include>
						</p>
						<p>
							<label for="password">
								<fmt:message key="Password" bundle="${ inLogingBundle }" />
								:
							</label>
							<input type="password" name="password" id="password" />
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="password" />
							</jsp:include>
						</p>
						<div>
							<input type="submit" value="<fmt:message key="LogIn" bundle="${ inLogingBundle }" />" />
						</div>
						<p>
							<a href="<c:url value="/UpSigning" />">
								<fmt:message key="CreateAccountLink" bundle="${ inLogingBundle }" />
							</a>
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>