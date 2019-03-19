<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
</head>
<body>
	<jsp:include page="/WEB-INF/includable/header.jsp">
		<jsp:param name="pageTitle" value="Administration" />
	</jsp:include>

	<div id="mainContainer">
		<div class="scrollableContainer centererContainer">
			<div class="totallyCenteredContainer tileContainer">

				<div class="borderedTile">
					<table class="table">
						<thead>
							<tr>
								<th>Id</th>
								<th>Nickname</th>
								<th>Registration date</th>
								<th>LastActivity date</th>
								<th title="Possession">🕹️</th>
								<th>Admin level</th>
								<th>Unblock date</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ requestScope.users }" var="user" varStatus="status">
								<c:if test="${user != requestScope.user}">
									<tr>
										<td>
											<c:out value="${ user.id }" />
										</td>
										<td>
											<c:out value="${ user.nickname }" />
										</td>
										<td>
											<c:out value="${ user.registrationInstant }" />
										</td>
										<td>
											<c:out value="${ user.registrationInstant }" />
										</td>
										<td>
											<c:if test="${user.canAdminister(requestScope.user)}">
												<c:url value="/Possession" var="possessUrl">
													<c:param name="userId" value="${user.id}" />
												</c:url>
												<a href="${possessUrl}"> 🕹️ </a>
											</c:if>
										</td>
										<td>
											<c:out value="${ user.adminLevel }" />
										</td>
										<td>
											<c:out value="${ user.unblockInstant }" />
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
				</div>

			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/includable/footer.jsp" />

</body>
</html>