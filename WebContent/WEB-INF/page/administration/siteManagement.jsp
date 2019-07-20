<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<fmt:setBundle basename="resources.Administration" var="administrationBundle" />

<c:set var="deployment"
	value="${ applicationScope['com.minquoad.service.Deployment'] }" scope="page" />

<div class="scrollableContainer centererContainer">
	<div class="totallyCenteredContainer tileContainer">
		<div class="borderedTile">
			<div class="padded">
				<ul>
					<li>
						<fmt:message key="Version" bundle="${ administrationBundle }" />
						: ${ deployment.version }
					</li>
					<li>
						<form method="POST" action="<c:url value="SiteManagement" />"
							accept-charset="UTF-8">
							<c:if test="${ deployment.open }">
								🔓 <fmt:message key="openSite" bundle="${ administrationBundle }" />
								<input type="hidden" name="open" value="false" />
								<input type="submit"
									value="🔒 <fmt:message key="CoseSite" bundle="${ administrationBundle }" />" />
							</c:if>
							<c:if test="${ not deployment.open }">
								🔒 <fmt:message key="siteClosed" bundle="${ administrationBundle }" />
								<input type="hidden" name="open" value="true" />
								<input type="submit"
									value="🔓 <fmt:message key="OpenSite" bundle="${ administrationBundle }" />" />
							</c:if>
						</form>
					</li>
					<li>
						<fmt:message key="WebsocketSessionNumber" bundle="${ administrationBundle }" />
						: ${ applicationScope['com.minquoad.service.SessionManager'].improvedEndpointsNumber }
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
