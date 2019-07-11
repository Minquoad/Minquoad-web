<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<fmt:setBundle basename="resources.Test" var="testBundle" />
</head>
<body>
	<fmt:message key="Test" bundle="${ testBundle }" var="testLabel" />
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="${ testLabel }" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<div class="borderedTile">

					<form method="post" action="<c:url value="/Test" />" accept-charset="UTF-8"
						enctype="multipart/form-data" class="padded">

						<h2>
							<fmt:message key="UploadAFile" bundle="${ testBundle }" />
							:
						</h2>

						<c:url value="/FileDownload" var="url">
							<c:param name="id" value="${ 0 }" />
						</c:url>
						<a href="${url}">
							<fmt:message key="DownloadLastUploadedFile" bundle="${ testBundle }" />
						</a>

						<p>
							<input type="file" name="file" id="file" />
						</p>

						<div>
							<input type="submit" value="Ajouter" />
						</div>
					</form>

				</div>
				<div class="borderedTile">

					<form method="post" action="<c:url value="/Test" />" accept-charset="UTF-8"
						enctype="multipart/form-data" class="padded">

						<h2>
							<fmt:message key="AlterTable" bundle="${ testBundle }" />
							:
						</h2>

						<p>
							<label for="description">
								<fmt:message key="IdLabel" bundle="${ testBundle }" />
								:
							</label>
							<input type="number" name="id" id="id" value="0" />
						</p>
						<p>
							<label for="description">
								<fmt:message key="Description" bundle="${ testBundle }" />
								:
							</label>
							<input type="text" name="description" id="description" />
						</p>
						<div>
							<input type="submit"
								value="<fmt:message key="Add" bundle="${ testBundle }" />" />
						</div>
					</form>

				</div>
				<div class="borderedTile">

					<div class="padded">
						<h2>
							<fmt:message key="Table" bundle="${ testBundle }" />
							:
						</h2>
						<ul>
							<c:forEach items="${ requestScope.things }" var="thing"
								varStatus="status">
								<li>
									<c:out value="${ thing.id }" />
									:
									<c:out value="${ thing.description }" />
									<c:if test="${not empty thing.owner}"> (<c:out
											value="${ thing.owner.nickname }" />)
								</c:if>
								</li>
							</c:forEach>
						</ul>
					</div>

				</div>

			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>