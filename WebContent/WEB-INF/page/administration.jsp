<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/includable/mainHeadContent.jsp"%>
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
							<c:forEach items="${ users }" var="user" varStatus="status">
								<c:if test="${user != sessionUser}">
									<tr>
										<td>
											<c:out value="${ user.id }" />
										</td>
										<td>
											<c:out value="${ user.nickname }" />
										</td>
										<td>
											<c:out value="${ user.registrationDate }" />
										</td>
										<td>
											<c:out value="${ user.registrationDate }" />
										</td>
										<td>
											<c:if test="${user.canAdminister(sessionUser)}">
												<c:url value="Possess" var="possessUrl">
													<c:param name="userId" value="${user.id}" />
												</c:url>
												<a href="<c:out value="${possessUrl}" />"> 🕹️ </a>
											</c:if>
										</td>
										<td>
											<c:out value="${ user.adminLevel }" />
										</td>
										<td>
											<c:out value="${ user.unblockDate }" />
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

	<%@ include file="/WEB-INF/includable/footer.jsp"%>
</body>
</html>