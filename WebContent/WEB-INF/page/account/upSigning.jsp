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
						<label for="mailAddress">Mail address : </label>
						<input type="email" name="mailAddress" id="mailAddress" maxlength="${ requestScope.mailAddressMaxlength }"
							value="<c:out value="${ requestScope.form.fields.mailAddress.value }" />" />
						<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
							<jsp:param name="formKey" value="form" />
							<jsp:param name="fieldName" value="mailAddress" />
						</jsp:include>
					</p>
					<p>
						<label for="nickname">Nickname : </label>
						<input type="text" name="nickname" id="nickname" maxlength="${ requestScope.nicknameMaxlength }"
							value="<c:out value="${ requestScope.form.fields.nickname.value }" />" />
						<c:if test="${ not empty requestScope.form }">
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="nickname" />
							</jsp:include>
						</c:if>
					</p>
					<p>
						<label for="password">Password : </label>
						<input type="password" name="password" id="password" />
						<c:if test="${ not empty requestScope.form }">
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="password" />
							</jsp:include>
						</c:if>
					</p>
					<p>
						<label for="passwordConfirmation">Password : </label>
						<input type="password" name="passwordConfirmation" id="passwordConfirmation" />
						<c:if test="${ not empty requestScope.form }">
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="passwordConfirmation" />
							</jsp:include>
						</c:if>
					</p>
					<p>
						<label for="upSigningCode">Up signing code : </label>
						<input type="password" name="upSigningCode" id="upSigningCode" />
						<c:if test="${ not empty requestScope.form }">
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="upSigningCode" />
							</jsp:include>
						</c:if>
					</p>
					<div>
						<input type="submit" value="Sign up" />
					</div>
				</form>
				<p>
					<a href="<c:url value="/InLoging" />">Already registered? Log in.</a>
				</p>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>