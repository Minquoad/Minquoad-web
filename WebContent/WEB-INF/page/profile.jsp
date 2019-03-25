<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Profile" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer">

				<p>
					<c:out value="${ requestScope.showedUser.nickname }" />
				</p>

				<c:if test="${not empty userProfileImage}">

					<c:url value="/ImageDownload" var="imageDownloadUrl">
						<c:param name="protectedFileId" value="${userProfileImage.id}" />
					</c:url>
					<div class="userProfileImageContainer">
						<img src="${imageDownloadUrl}" class="userProfileImage">
					</div>

				</c:if>

			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>