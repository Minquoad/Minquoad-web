<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/MainHeadContent.jsp"%>
</head>
<body>
	<c:set var="pageTitle" value="Test" scope="request" />
	<%@ include file="/WEB-INF/Header.jsp"%>
	
	<div id="mainContainer">
		<div class="scrollableContainer">
			<div class="padded">
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

				<form method="post" action="Test" accept-charset="UTF-8"
					enctype="multipart/form-data">

					<h2>Modifier la table :</h2>

					<p>
						<label for="description">Id (non-existing id for a new) :
						</label> <input type="number" name="id" id="id" value="0" />
					</p>
					<p>
						<label for="description">Description : </label> <input type="text"
							name="description" id="description" />
					</p>
					<div>
						<input type="submit" value="Ajouter" />
					</div>
				</form>

				<h1>Table :</h1>
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

	<%@ include file="/WEB-INF/Footer.jsp"%>

</body>
</html>