<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/administration.css" />" />
<script type="text/javascript" src="<c:url value="/js/administration.js" />"></script>
<fmt:setBundle basename="resources.Administration" var="administrationBundle" />
</head>
<body>
	<fmt:message key="Administration" bundle="${ administrationBundle }"
		var="administrationLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ administrationLabel }" />
	</jsp:include>

	<div id="mainContainer">

		<div id="administration" class="fullSize inlineBlockContainer">
			<div id="nav">
				<div data-administrationSubPageUrl="<c:url value="/UsersManagement" />"
					data-administrationSubPageName="UsersManagement">
					<fmt:message key="UsersManagement" bundle="${ administrationBundle }" />
				</div>
				<div data-administrationSubPageUrl="<c:url value="/SiteManagement" />"
					data-administrationSubPageName="SiteManagement">
					<fmt:message key="SiteManagement" bundle="${ administrationBundle }" />
				</div>
				<div
					data-administrationSubPageUrl="<c:url value="/ImprovementSuggestionsConsulting" />"
					data-administrationSubPageName="ImprovementSuggestionsConsulting">
					<fmt:message key="ImprovementSuggestions" bundle="${ administrationBundle }" />
				</div>
			</div>

			<div id="currentSubPage"></div>
		</div>

	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />
	<jsp:include page="/WEB-INF/includable/utilityDiv.jsp" />

</body>
</html>