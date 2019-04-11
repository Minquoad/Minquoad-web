<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/includable/mainHeadContent.jsp" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/administration.css" />" />
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
								<th title="Block">🚫</th>
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
											<c:out value="${ user.lastActivityInstant }" />
										</td>
										<td>
											<c:if test="${ user.canAdminister(requestScope.user) }">
												<c:url value="/Possession" var="possessUrl">
													<c:param name="userId" value="${user.id}" />
												</c:url>
												<a title="Possession" href="${possessUrl}"> 🕹️ </a>
											</c:if>
										</td>
										<td>
											<c:out value="${ user.adminLevel }" />
										</td>
										<td>
											<c:if test="${ user.canAdminister(requestScope.user) }">
												<div title="Block" class="dynamicMenuTrigger" id="blockingFormTrigger">
													🚫
													<div class="dynamicMenu">
														<div class="dynamicMenuItem">
															<c:url value="/Unblocking" var="unblockUrl">
																<c:param name="userId" value="${ user.id }" />
															</c:url>
															<input type="button" onclick="window.location.href = '${ unblockUrl }';" value="Remove blocking date" />
														</div>
														<form method="post" class="dynamicMenuItem" action="<c:url value="/Blocking" />" accept-charset="UTF-8">
															<label for="unblockDate">Set unblocking date :</label>
															<input type="hidden" name="targetId" value="${ user.id }" />
															<input type="date" name="unblockDate" />
															<input type="submit" value="Block" />
														</form>
													</div>
												</div>
											</c:if>
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