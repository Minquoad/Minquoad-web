<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="com.minquoad.entity.User"%>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/profile.css" />" />
<fmt:setBundle basename="resources.ProfileEdition" var="profileEditionBundle" />
</head>
<body>
	<fmt:message key="ProfileEdition" bundle="${ profileEditionBundle }"
		var="profileEditionLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ profileEditionLabel }" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer">

				<form method="post" action="<c:url value="/ProfileEdition" />"
					accept-charset="UTF-8" enctype="multipart/form-data">

					<div id="mainTitle">
						<div class="inlineBlock">
							<c:set var="userProfileImage"
								value="${ requestScope.daoFactory.userProfileImageDao.getUserUserProfileImage(requestScope.user) }"
								scope="page" />

							<div class="userProfileImageContainer">
								<c:if test="${not empty userProfileImage }">
									<c:url value="/ImageDownload" var="imageDownloadUrl">
										<c:param name="id" value="${ userProfileImage.id }" />
									</c:url>
									<img src="${ imageDownloadUrl }" class="userProfileImage">
								</c:if>
							</div>

						</div>

						<div id="nickname" class="inlineBlock name"
							style="color: ${ user.getDefaultColorAsHtmlValue() };">
							<c:out value="${ requestScope.user.nickname }" />
						</div>

						<p>
							<label for="nickname">
								<fmt:message key="Nickname" bundle="${ profileEditionBundle }" />
								:
							</label>
							<input type="text" name="nickname"
								maxlength="${ User.NICKNAME_MAX_LENGTH }"
								value="<c:out value="${ requestScope.form.fields.nickname.value }" />" />
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="nickname" />
							</jsp:include>
						</p>

						<p>
							<label for="picture">
								<fmt:message key="UploadANewPicture" bundle="${ profileEditionBundle }" />
								:
							</label>
							<input type="file" name="picture" />
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="picture" />
							</jsp:include>
						</p>

						<c:if test="${not empty userProfileImage }">
							<p>
								<label for="pictureReset">
									<fmt:message key="pictureReset" bundle="${ profileEditionBundle }" />
									:
								</label>
								<input type="checkbox" name="pictureReset"
									<c:if test="${ requestScope.form.fields.pictureReset.checked }">
										checked
									</c:if> />
								<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
									<jsp:param name="formKey" value="form" />
									<jsp:param name="fieldName" value="pictureReset" />
								</jsp:include>
							</p>
						</c:if>

						<p>
							<label for="defaultColor">
								<fmt:message key="DefaultAccountColor"
									bundle="${ profileEditionBundle }" />
								:
							</label>
							<input type="color" name="defaultColor"
								value="${ requestScope.user.defaultColorAsHtmlValue }" />
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="defaultColor" />
							</jsp:include>
						</p>

						<div>
							<input type="submit"
								value="<fmt:message key="Save" bundle="${ profileEditionBundle }" />" />
						</div>

					</div>

				</form>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>