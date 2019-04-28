<div class="scrollableContainer centererContainer">
	<div class="totallyCenteredContainer tileContainer">

		<div class="borderedTile">
			<div class="padded">
				<table class="table">
					<thead>
						<tr>
							<th>Id</th>
							<th>Nickname</th>
							<th>Registration date</th>
							<th>LastActivity date</th>
							<th title="Possession">🕹️</th>
							<th>Admin level</th>
							<th title="Block">🛂</th>
							<th>Unblock date</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ requestScope.users }" var="loopUser">
							<c:if test="${loopUser != requestScope.user}">
								<tr>
									<td>
										<c:out value="${ loopUser.id }" />
									</td>
									<td>
										<c:out value="${ loopUser.nickname }" />
									</td>
									<td>
										<c:out value="${ loopUser.registrationInstant }" />
									</td>
									<td>
										<c:out value="${ loopUser.lastActivityInstant }" />
									</td>
									<td>
										<c:if test="${ requestScope.user.canAdminister(loopUser) }">
											<c:url value="/Possession" var="possessUrl">
												<c:param name="targetId" value="${ loopUser.id }" />
											</c:url>
											<a title="Possession" href="${possessUrl}"> 🕹️ </a>
										</c:if>
									</td>
									<td>
										<c:out value="${ loopUser.adminLevel }" />
									</td>
									<td>
										<c:if test="${ requestScope.user.canAdminister(loopUser) }">
											<div title="Block" class="dynamicMenuTrigger" id="blockingFormTrigger">

												<c:if test="${ loopUser.blocked }">
															⛔
														</c:if>
												<c:if test="${ not loopUser.blocked }">
															🆗
														</c:if>

												<div class="dynamicMenu">
													<c:if test="${ loopUser.blocked }">
														<div class="dynamicMenuItem">
															<c:url value="/Unblocking" var="unblockUrl">
																<c:param name="targetId" value="${ loopUser.id }" />
															</c:url>
															<input type="button" onclick="window.location.href = '${ unblockUrl }';" value="🆗 Remove blocking date" />
														</div>
													</c:if>
													<form method="post" class="dynamicMenuItem" action="<c:url value="/Blocking" />" accept-charset="UTF-8">
														<label for="unblockDate"> Set unblocking date :</label>
														<input type="hidden" name="targetId" value="${ loopUser.id }" />
														<input type="date" name="date" />
														<input type="submit" value="⛔ Block" />
													</form>
												</div>
											</div>
										</c:if>
									</td>
									<td>
										<c:out value="${ loopUser.unblockInstant }" />
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