<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<fmt:setBundle basename="resources.AccountManagement"
	var="accountManagementBundle" />
</head>
<body>
	<fmt:message key="AccountManagement" bundle="${ accountManagementBundle }"
		var="accountManagementLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ accountManagementLabel }" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<div class="borderedTile">
					<div class="padded">
						<h2>
							<fmt:message key="AccountProperties"
								bundle="${ accountManagementBundle }" />
						</h2>
						<form method="post" action="<c:url value="/AccountManagement" />"
							accept-charset="UTF-8">

							<input type="hidden" name="formId" value="userParametersAlteration" />
							<p>
								<label for="mailAddress">
									<fmt:message key="MailAddress" bundle="${ accountManagementBundle }" />
									:
								</label>
								<input type="email" name="mailAddress" id="mailAddress"
									maxlength="${ requestScope.mailAddressMaxlength }"
									value="<c:out value="${ requestScope.userParametersAlteration.fields.mailAddress.value }" />" />
								<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
									<jsp:param name="formKey" value="userParametersAlteration" />
									<jsp:param name="fieldName" value="mailAddress" />
								</jsp:include>
							</p>
							<p>
								<label for="nickname">
									<fmt:message key="Nickname" bundle="${ accountManagementBundle }" />
									:
								</label>
								<input type="text" name="nickname" id="nickname"
									maxlength="${ requestScope.nicknameMaxlength }"
									value="<c:out value="${ requestScope.userParametersAlteration.fields.nickname.value }" />" />
								<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
									<jsp:param name="formKey" value="userParametersAlteration" />
									<jsp:param name="fieldName" value="nickname" />
								</jsp:include>
							</p>
							<p>
								<label for="language">
									<fmt:message key="Language" bundle="${ accountManagementBundle }" />
									:
								</label>
								<select id="language" name="language">
									<option value="en"
										<c:if test="${ requestScope.userParametersAlteration.fields.language.value eq 'en' }">
										selected
									</c:if>>English</option>
									<option value="fr"
										<c:if test="${ requestScope.userParametersAlteration.fields.language.value eq 'fr' }">
										selected
									</c:if>>Français</option>
								</select>
								<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
									<jsp:param name="formKey" value="userParametersAlteration" />
									<jsp:param name="fieldName" value="language" />
								</jsp:include>
							</p>
							<p>
								<label for="defaultColor">
									<fmt:message key="DefaultAccountColor"
										bundle="${ accountManagementBundle }" />
									:
								</label>
								<input type="color" name="defaultColor" id="defaultColor"
									value="${ requestScope.user.defaultColorAsHtmlValue }" />
								<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
									<jsp:param name="formKey" value="userParametersAlteration" />
									<jsp:param name="fieldName" value="defaultColor" />
								</jsp:include>
							</p>
							<div>
								<input type="submit"
									value="<fmt:message key="Save" bundle="${ accountManagementBundle }" />" />
							</div>
						</form>
					</div>
				</div>

				<div class="borderedTile">
					<div class="padded">
						<h2>
							<fmt:message key="SendANewProfilePicture"
								bundle="${ accountManagementBundle }" />
						</h2>

						<c:if test="${not empty userProfileImage}">

							<p>
								<fmt:message key="ActualProfilePicture"
									bundle="${ accountManagementBundle }" />
								:
							</p>

							<c:url value="/ImageDownload" var="imageDownloadUrl">
								<c:param name="id" value="${userProfileImage.id}" />
							</c:url>
							<div class="userProfileImageContainer">
								<img src="${imageDownloadUrl}" class="userProfileImage">
							</div>

						</c:if>

						<form method="post" action="<c:url value="/AccountManagement" />"
							accept-charset="UTF-8" enctype="multipart/form-data">

							<input type="hidden" name="formId" value="userPictureAlteration" />

							<p>
								<input type="file" name="userPicture" />
								<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
									<jsp:param name="formKey" value="userPictureAlterationForm" />
									<jsp:param name="fieldName" value="userPicture" />
								</jsp:include>
							</p>
							<p>
								<fmt:message key="removeProfilePictureInstruction"
									bundle="${ accountManagementBundle }" />
							</p>
							<div>
								<input type="submit"
									value="<fmt:message key="Submit" bundle="${ accountManagementBundle }" />" />
							</div>
						</form>
					</div>
				</div>

				<div class="borderedTile">
					<div class="padded">
						<h2>
							<fmt:message key="ChangePassword" bundle="${ accountManagementBundle }" />
						</h2>
						<form method="post" action="<c:url value="/AccountManagement" />"
							accept-charset="UTF-8">

							<input type="hidden" name="formId" value="userPasswordAlteration" />

							<p>
								<label for="oldPassowrd">
									<fmt:message key="OldPassword" bundle="${ accountManagementBundle }" />
									:
								</label>
								<input type="password" name="oldPassowrd" id="oldPassowrd" />
								<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
									<jsp:param name="formKey" value="userPasswordAlterationForm" />
									<jsp:param name="fieldName" value="oldPassowrd" />
								</jsp:include>
							</p>
							<p>
								<label for="newPassword">
									<fmt:message key="NewPassword" bundle="${ accountManagementBundle }" />
									:
								</label>
								<input type="password" name="newPassword" id="newPassword" />
								<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
									<jsp:param name="formKey" value="userPasswordAlterationForm" />
									<jsp:param name="fieldName" value="newPassword" />
								</jsp:include>
							</p>
							<p>
								<label for="newPasswordConfirmation">
									<fmt:message key="ConfirmNewPassword" bundle="${ accountManagementBundle }" />
									:
								</label>
								<input type="password" name="newPasswordConfirmation"
									id="newPasswordConfirmation" />
								<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
									<jsp:param name="formKey" value="userPasswordAlterationForm" />
									<jsp:param name="fieldName" value="newPasswordConfirmation" />
								</jsp:include>
							</p>
							<div>
								<input type="submit" value="<fmt:message key="Change" bundle="${ accountManagementBundle }" />" />
							</div>
						</form>
					</div>
				</div>

			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>