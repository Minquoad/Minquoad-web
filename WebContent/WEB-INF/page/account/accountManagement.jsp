<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Account management" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<div class="borderedTile">
					<div class="padded">
						<h2>Sent a new profile picture</h2>

						<c:if test="${not empty userProfileImage}">

							<p>Actual profile picture:</p>

							<c:url value="/ImageDownload" var="imageDownloadUrl">
								<c:param name="protectedFileId" value="${userProfileImage.id}" />
							</c:url>
							<div class="userProfileImageContainer">
								<img src="${imageDownloadUrl}" class="userProfileImage">
							</div>

						</c:if>

						<form method="post" action="<c:url value="/AccountManagement" />" accept-charset="UTF-8" enctype="multipart/form-data">

							<input type="hidden" name="formId" value="userPictureAlteration" />

							<p>
								<input type="file" name="userPicture" />
								<c:if test="${ not empty requestScope.userPictureAlterationForm }">
									<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
										<jsp:param name="formKey" value="userPictureAlterationForm" />
										<jsp:param name="fieldName" value="userPicture" />
									</jsp:include>
								</c:if>
							</p>
							<p>Submit without file to return back to the default picture.</p>
							<div>
								<input type="submit" value="Submit" />
							</div>
						</form>
					</div>
				</div>

				<div class="borderedTile">
					<div class="padded">
						<h2>Change password</h2>
						<form method="post" action="<c:url value="/AccountManagement" />" accept-charset="UTF-8">

							<input type="hidden" name="formId" value="userPasswordAlteration" />

							<p>
								<label for="oldPassowrd">Old Password : </label>
								<input type="password" name="oldPassowrd" id="oldPassowrd" />
								<c:if test="${ not empty requestScope.userPasswordAlterationForm }">
									<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
										<jsp:param name="formKey" value="userPasswordAlterationForm" />
										<jsp:param name="fieldName" value="oldPassowrd" />
									</jsp:include>
								</c:if>
							</p>
							<p>
								<label for="newPassword">New password : </label>
								<input type="password" name="newPassword" id="newPassword" />
								<c:if test="${ not empty requestScope.userPasswordAlterationForm }">
									<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
										<jsp:param name="formKey" value="userPasswordAlterationForm" />
										<jsp:param name="fieldName" value="newPassword" />
									</jsp:include>
								</c:if>
							</p>
							<p>
								<label for="newPasswordConfirmation">Confirm new password : </label>
								<input type="password" name="newPasswordConfirmation" id="newPasswordConfirmation" />
								<c:if test="${ not empty requestScope.userPasswordAlterationForm }">
									<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
										<jsp:param name="formKey" value="userPasswordAlterationForm" />
										<jsp:param name="fieldName" value="newPasswordConfirmation" />
									</jsp:include>
								</c:if>
							</p>
							<div>
								<input type="submit" value="Change" />
							</div>
						</form>
					</div>
				</div>

				<div class="borderedTile">
					<div class="padded">
						<h2>Default account color</h2>
						<form method="post" action="<c:url value="/AccountManagement" />" accept-charset="UTF-8">

							<input type="hidden" name="formId" value="userParametersAlteration" />

							<p>
								<input type="color" name="defaultColor" id="defaultColor"  value="#${ requestScope.user.getDefaultColorAsHexString() }"/>
								<c:if test="${ not empty requestScope.userParametersAlteration }">
									<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
										<jsp:param name="formKey" value="userParametersAlteration" />
										<jsp:param name="fieldName" value="defaultColor" />
									</jsp:include>
								</c:if>
							</p>
							<div>
								<input type="submit" value="Change" />
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