<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/includable/mainHeadContent.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Account management" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<div class="borderedTile padded">

					<h2>Sent a new profile picture</h2>

					<c:if test="${not empty sessionUser.pictureName}">

						<p>Actual profile picture:</p>
						WIP
						<!--
						<div class="croppedResizedPicture"
							style="background-image: url('${pageContext.request.contextPath}/img/Community/${ sessionUser.pictureName}')">
						</div>
						 -->
					</c:if>

					<form method="post" action="AccountManagement"
						accept-charset="UTF-8" enctype="multipart/form-data">

						<input type="hidden" name="formId" value="userPictureAlteration" />

						<p>
							<input type="file" name="userPicture" />
						</p>
						<p>Submit without file to return back to the default picture.</p>
						<jsp:include page="/WEB-INF/includable/form/formProblems.jsp">
							<jsp:param name="formProblems"
								value="userPictureAlterationFormProblems" />
						</jsp:include>
						<div>
							<input type="submit" value="Submit" />
						</div>
					</form>
				</div>

				<div class="borderedTile padded">
					<h2>Change password</h2>
					<form method="post" action="AccountManagement"
						accept-charset="UTF-8">

						<input type="hidden" name="formId" value="userPasswordAlteration" />

						<p>
							<label for="oldPassowrd">Old Password : </label> <input
								type="password" name="oldPassowrd" id="oldPassowrd" />
						</p>
						<p>
							<label for="newPassword">New password : </label> <input
								type="password" name="newPassword" id="newPassword" />
						</p>
						<p>
							<label for="newPasswordConfirmation">Confirm new password
								: </label> <input type="password" name="newPasswordConfirmation"
								id="newPasswordConfirmation" />
						</p>
						<jsp:include page="/WEB-INF/includable/form/formProblems.jsp">
							<jsp:param name="formProblems"
								value="userPasswordAlterationFormProblems" />
						</jsp:include>
						<div>
							<input type="submit" value="Change" />
						</div>
					</form>
				</div>

			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/includable/footer.jsp"%>
</body>
</html>