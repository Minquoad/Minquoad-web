<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/profile.css" />" />
<fmt:setBundle basename="resources.Profile" var="profileBundle" />
</head>
<body>
	<fmt:message key="Profile" bundle="${ profileBundle }" var="profileLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ profileLabel }" />
	</jsp:include>

	<div id="mainContainer" class="scrollableContainer blockFlex">
		<div class="totallyCenteredContainer">

			<div id="mainTitle">
				<div class="inlineBlock">
					<c:set var="userProfileImage"
						value="${ requestScope.daoFactory.userProfileImageDao.getUserUserProfileImage(requestScope.showedUser) }"
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
					style="color: ${ showedUser.getDefaultColorAsHtmlValue() };">
					<c:out value="${ requestScope.showedUser.nickname }" />
				</div>
			</div>

			<c:if test="${ requestScope.showedUser eq requestScope.user}">
				<a id="editionLink" href="<c:url value="/ProfileEdition" />">
					<fmt:message key="ProfileEdition" bundle="${ profileBundle }" />
				</a>
			</c:if>


		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>