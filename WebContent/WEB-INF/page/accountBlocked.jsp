<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/includable/mainHeadContent.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer padded centeredText redText">
				Account blocked
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/includable/footer.jsp"%>
</body>
</html>