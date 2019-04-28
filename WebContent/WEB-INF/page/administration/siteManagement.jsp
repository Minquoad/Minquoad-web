<div class="scrollableContainer centererContainer">
	<div class="totallyCenteredContainer tileContainer">

		<div class="borderedTile">
			<div class="padded">
				<c:if test="${ applicationScope.deployment.open }">
					<h2>🔓 The site is open.</h2>
					<c:url value="/SiteStateChangement" var="siteStateChangementUrl">
						<c:param name="open" value="false" />
					</c:url>
					<input type="button" onclick="window.location.href = '${ siteStateChangementUrl }';" value="🔒 Close site" />
				</c:if>
				<c:if test="${ not applicationScope.deployment.open }">
					<h2>🔒 The site is closed.</h2>
					<c:url value="/SiteStateChangement" var="siteStateChangementUrl">
						<c:param name="open" value="true" />
					</c:url>
					<input type="button" onclick="window.location.href = '${ siteStateChangementUrl }';" value="🔓 Open site" />
				</c:if>
			</div>
		</div>
	</div>
</div>
