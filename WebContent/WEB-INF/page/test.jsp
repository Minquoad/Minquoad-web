<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Test" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<div class="borderedTile">

					<form method="post" action="<c:url value="/Test" />" accept-charset="UTF-8" enctype="multipart/form-data" class="padded">

						<h2>Uploader un fichier :</h2>

						<c:url value="/FileDownload" var="url">
							<c:param name="id" value="${ 0 }" />
						</c:url>
						<a href="${url}">Télécharger le dernier fichier uploadé</a>

						<p>
							<input type="file" name="file" id="file" />
						</p>

						<div>
							<input type="submit" value="Ajouter" />
						</div>
					</form>

				</div>
				<div class="borderedTile">

					<form method="post" action="<c:url value="/Test" />" accept-charset="UTF-8" enctype="multipart/form-data" class="padded">

						<h2>Modifier la table :</h2>

						<p>
							<label for="description">Id (non-existing id for a new) : </label> <input type="number" name="id" id="id" value="0" />
						</p>
						<p>
							<label for="description">Description : </label> <input type="text" name="description" id="description" />
						</p>
						<div>
							<input type="submit" value="Ajouter" />
						</div>
					</form>

				</div>
				<div class="borderedTile">

					<div class="padded">
						<h2>Table :</h2>
						<ul>
							<c:forEach items="${ requestScope.things }" var="thing" varStatus="status">
								<li><c:out value="${ thing.id }" /> : <c:out value="${ thing.description }" /> <c:if test="${not empty thing.owner}"> (<c:out value="${ thing.owner.nickname }" />)
								</c:if></li>
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