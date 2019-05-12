<fmt:setBundle basename="resources.Administration" var="administrationBundle" />

<c:set var="deployment" value="${ applicationScope['com.minquoad.service.Deployment'] }" scope="page" />

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
						<c:if test="${ deployment.open }">
						🔓
						<fmt:message key="openSite" bundle="${ administrationBundle }" />
							<c:url value="/SiteStateChangement" var="siteStateChangementUrl">
								<c:param name="open" value="false" />
							</c:url>
							<input type="button"
								onclick="window.location.href = '${ siteStateChangementUrl }';"
								value="🔒 <fmt:message key="CoseSite" bundle="${ administrationBundle }" />" />
						</c:if>
						<c:if test="${ not deployment.open }">
						🔒
						<fmt:message key="siteClosed" bundle="${ administrationBundle }" />
							<c:url value="/SiteStateChangement" var="siteStateChangementUrl">
								<c:param name="open" value="true" />
							</c:url>
							<input type="button"
								onclick="window.location.href = '${ siteStateChangementUrl }';"
								value="🔓 <fmt:message key="OpenSite" bundle="${ administrationBundle }" />" />
						</c:if>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
