<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="com.minquoad.entity.User"%>

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

	<div id="mainContainer" class="scrollableContainer blockFlex">
		<div class="totallyCenteredContainer tileContainer">

			<div class="borderedTile">
				<div class="padded">
					<h2>
						<fmt:message key="AccountProperties" bundle="${ accountManagementBundle }" />
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
								maxlength="${ User.MAIL_ADDRESS_MAX_LENGTH }"
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
								maxlength="${ User.NICKNAME_MAX_LENGTH }"
								value="<c:out value="${ requestScope.userParametersAlteration.fields.nickname.value }" />" />
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="userParametersAlteration" />
								<jsp:param name="fieldName" value="nickname" />
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
							<label for="readabilityImprovementActivated">
								<fmt:message key="readabilityImprovement"
									bundle="${ accountManagementBundle }" />
								:
							</label>
							<input type="checkbox" name="readabilityImprovementActivated"
								id="readabilityImprovementActivated"
								<c:if test="${ requestScope.userParametersAlteration.fields.readabilityImprovementActivated.value }">
										checked
									</c:if> />
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="userParametersAlteration" />
								<jsp:param name="fieldName" value="readabilityImprovementActivated" />
							</jsp:include>
						</p>
						<p>
							<label for="typingAssistanceActivated">
								<fmt:message key="typingAssistance"
									bundle="${ accountManagementBundle }" />
								:
							</label>
							<input type="checkbox" name="typingAssistanceActivated"
								id="typingAssistanceActivated"
								<c:if test="${ requestScope.userParametersAlteration.fields.typingAssistanceActivated.value }">
										checked
									</c:if> />
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="userParametersAlteration" />
								<jsp:param name="fieldName" value="typingAssistanceActivated" />
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
								<fmt:message key="ConfirmNewPassword"
									bundle="${ accountManagementBundle }" />
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
							<input type="submit"
								value="<fmt:message key="Change" bundle="${ accountManagementBundle }" />" />
						</div>
					</form>
				</div>
			</div>

		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>