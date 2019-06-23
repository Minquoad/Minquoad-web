<fmt:setBundle basename="resources.Administration" var="administrationBundle" />

<div class="scrollableContainer centererContainer">
	<div class="totallyCenteredContainer tileContainer">

		<div class="borderedTile">
			<div class="padded">
				<table class="table">
					<thead>
						<tr>
							<th><fmt:message key="Id" bundle="${ administrationBundle }" /></th>
							<th><fmt:message key="Nickname" bundle="${ administrationBundle }" /></th>
							<th><fmt:message key="Registrationdate"
									bundle="${ administrationBundle }" /></th>
							<th><fmt:message key="LastActivityDate"
									bundle="${ administrationBundle }" /></th>
							<th
								title="<fmt:message key="Possession" bundle="${ administrationBundle }" />">🕹️</th>
							<th><fmt:message key="AdminLevel" bundle="${ administrationBundle }" /></th>
							<th
								title="<fmt:message key="Block" bundle="${ administrationBundle }" />">🛂</th>
							<th><fmt:message key="UnblockDate"
									bundle="${ administrationBundle }" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ requestScope.users }" var="loopUser">
							<tr>
								<td>
									<c:out value="${ loopUser.id }" />
								</td>
								<td>
									<c:out value="${ loopUser.nickname }" />
								</td>
								<td>
									<span class="dateToFormat" data-format="1"> <c:out
											value="${ loopUser.registrationInstant }" />
									</span>
								</td>
								<td>
									<span class="dateToFormat" data-format="1"> <c:out
											value="${ loopUser.lastActivityInstant }" />
									</span>
								</td>
								<td>
									<c:if test="${ requestScope.user.canAdminister(loopUser) }">
										<c:url value="/Possession" var="possessUrl">
											<c:param name="targetId" value="${ loopUser.id }" />
										</c:url>
										<a
											title="<fmt:message key="Possession" bundle="${ administrationBundle }" />"
											href="${possessUrl}"> 🕹️ </a>
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
														<input type="button"
															onclick="window.location.href = '${ unblockUrl }';"
															value="🆗 <fmt:message key="RemoveBlockingDate" bundle="${ administrationBundle }" />" />
													</div>
												</c:if>
												<form method="post" class="dynamicMenuItem"
													action="<c:url value="/Blocking" />" accept-charset="UTF-8">
													<label for="unblockDate">
														<fmt:message key="SetUnblockingDate"
															bundle="${ administrationBundle }" />
														:
													</label>
													<input type="hidden" name="targetId" value="${ loopUser.id }" />
													<input type="date" name="date" />
													<input type="submit" value="⛔ Block" />
												</form>
											</div>
										</div>
									</c:if>
								</td>
								<td class="dateToFormat" data-format="1">
									<c:out value="${ loopUser.unblockInstant }" />
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>