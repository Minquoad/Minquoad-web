<fmt:setBundle basename="resources.Administration" var="administrationBundle" />

<div class="scrollableContainer centererContainer">
	<div class="totallyCenteredContainer tileContainer">

		<div class="borderedTile">
			<div class="padded">
				<c:if test="${ applicationScope.deployment.open }">
					<h2>🔓 <fmt:message key="openSite" bundle="${ administrationBundle }" /></h2>
					<c:url value="/SiteStateChangement" var="siteStateChangementUrl">
						<c:param name="open" value="false" />
					</c:url>
					<input type="button" onclick="window.location.href = '${ siteStateChangementUrl }';" value="🔒 <fmt:message key="CoseSite" bundle="${ administrationBundle }" />" />
				</c:if>
				<c:if test="${ not applicationScope.deployment.open }">
					<h2>🔒 <fmt:message key="siteClosed" bundle="${ administrationBundle }" /></h2>
					<c:url value="/SiteStateChangement" var="siteStateChangementUrl">
						<c:param name="open" value="true" />
					</c:url>
					<input type="button" onclick="window.location.href = '${ siteStateChangementUrl }';" value="🔓 <fmt:message key="OpenSite" bundle="${ administrationBundle }" />" />
				</c:if>
			</div>
		</div>
	</div>
</div>
