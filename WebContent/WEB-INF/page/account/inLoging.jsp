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
						<label for="mailAddress">Mail address : </label>
						<input type="text" name="mailAddress" id="nickname" value="<c:out value="${ requestScope.form.fields.mailAddress.value }" />" />
						<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
							<jsp:param name="formKey" value="form" />
							<jsp:param name="fieldName" value="mailAddress" />
						</jsp:include>
					</p>
					<p>
						<label for="password">Password : </label>
						<input type="password" name="password" id="password" />
						<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
							<jsp:param name="formKey" value="form" />
							<jsp:param name="fieldName" value="password" />
						</jsp:include>
					</p>
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