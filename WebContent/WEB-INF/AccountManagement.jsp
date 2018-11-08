<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/MainHeadContent.jsp"%>
</head>
<body>
	<c:set var="pageTitle" value="Account management" scope="request" />
	<%@ include file="/WEB-INF/Header.jsp"%>

	<div id="mainContainer">
		<div class="scrollableContainer">
			<div class="centererContainer">
				<div class="totallyCenteredContainer">
					<div class="tileContainer">

						<div class="tile">
						
							<h2>Sent a new profile picture</h2>

							<c:if test="${not empty sessionUser.pictureName}">

								<p>Actual profile picture:</p>
								<div class="croppedResizedPicture"
									style="background-image: url('${pageContext.request.contextPath}/img/Community/${ sessionUser.pictureName}')">
								</div>
							</c:if>

							<form method="post" action="accountManagement"
								accept-charset="UTF-8" enctype="multipart/form-data">

								<input type="hidden" name="formId" value="userPictureAlteration" />

								<p>
									<input type="file" name="userPicture" />
								</p>
								<p>
									Submit without file to return back to the default picture.
								</p>
								<c:set var="currentFormProblems"
									value="${ userPictureAlterationFormProblems }" scope="request" />
								<%@ include file="/WEB-INF/Form/FormProblems.jsp"%>
								<div>
									<input type="submit" value="Submit" />
								</div>
							</form>
						</div>

						<div class="tile">
							<h2>Change password</h2>
							<form method="post" action="accountManagement"
								accept-charset="UTF-8">

								<input type="hidden" name="formId"
									value="userPasswordAlteration" />

								<p>
									<label for="oldPassowrd">Old Password : </label>
									<input type="password" name="oldPassowrd" id="oldPassowrd" />
								</p>
								<p>
									<label for="newPassword">New password : </label>
									<input type="password" name="newPassword" id="newPassword" />
								</p>
								<p>
									<label for="newPasswordConfirmation">Confirm new
										password : </label>
									<input type="password" name="newPasswordConfirmation"
										id="newPasswordConfirmation" />
								</p>
								<c:set var="currentFormProblems"
									value="${ userPasswordAlterationFormProblems }" scope="request" />
								<%@ include file="/WEB-INF/Form/FormProblems.jsp"%>
								<div>
									<input type="submit" value="Log in" />
								</div>
							</form>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/Footer.jsp"%>
</body>