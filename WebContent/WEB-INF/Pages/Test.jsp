<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/Includables/MainHeadContent.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/Includables/Header.jsp">
		<jsp:param name="pageTitle" value="Test" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<div class="borderedTile padded">

					<form method="post" action="Test" accept-charset="UTF-8"
						enctype="multipart/form-data">

						<h2>Uploader un fichier :</h2>

						<p>
							<input type="file" name="file" id="file" />
						</p>

						<div>
							<input type="submit" value="Ajouter" />
						</div>
					</form>

				</div>
				<div class="borderedTile padded">

					<form method="post" action="Test" accept-charset="UTF-8"
						enctype="multipart/form-data">

						<h2>Modifier la table :</h2>

						<p>
							<label for="description">Id (non-existing id for a new) :
							</label> <input type="number" name="id" id="id" value="0" />
						</p>
						<p>
							<label for="description">Description : </label> <input
								type="text" name="description" id="description" />
						</p>
						<div>
							<input type="submit" value="Ajouter" />
						</div>
					</form>

				</div>
				<div class="borderedTile padded">

					<h2>Table :</h2>
					<ul>
						<c:forEach items="${ things }" var="thing" varStatus="status">
							<li><c:out value="${ thing.id }" /> : <c:out
									value="${ thing.description }" /> <c:if
									test="${not empty thing.owner}"> (<c:out
										value="${ thing.owner.nickname }" />)
								</c:if></li>
						</c:forEach>
					</ul>

				</div>

			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/Includables/Footer.jsp"%>

</body>
</html>