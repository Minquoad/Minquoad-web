<div id="header">
	<div id="siteNameHeader">
		<div class="centererContainer">
			<div class="totallyCenteredContainer">
				<a href="<c:url value="/Home" />" class="undecorated">Minquoad.com</a>
			</div>
		</div>
	</div>

	<div id="pageTitleHeader">
		<div class="centererContainer">
			<div class="vertivallyCenteredContainer fullWidth">
				<div id="pageTitle">
					<a href="">
						<c:out value="${ param.pageTitle }" />
					</a>
				</div>
			</div>
		</div>
	</div>

	<div id="rightHeader">
		<div class="centererContainer">
			<div class="vertivallyCenteredContainer fullWidth">
				<div class="horizontallyPadded">
					<div class="headerItem dynamicMenuTrigger">
						▼ Activities
						<div class="dynamicMenu">
							<a class="dynamicMenuItem" href="<c:url value="/Test" />">Test</a>
						</div>
					</div>
					<c:if test="${ not empty requestScope.user }">
						<a href="<c:url value="/Conversations" />" class="headerItem">🗪 Conversation</a>
						<a href="<c:url value="/Community" />" class="headerItem">👥 Community</a>
						<div class="headerItem dynamicMenuTrigger">
							▼
							<c:out value="${ requestScope.user.nickname }" />
							<div class="dynamicMenu">
								<a class="dynamicMenuItem" href="<c:url value="/AccountManagement" />">👤 Account management </a>
								<a class="dynamicMenuItem" href="<c:url value="/OutLoging" />">✖ Log out</a>
							</div>
						</div>
					</c:if>
					<c:if test="${ empty requestScope.user }">
						<a href="<c:url value="/InLoging" />" class="headerItem">👤 Log in</a>
						<div class="headerItem dynamicMenuTrigger">
							<img class="flag" src="<c:url value="/img/languageFlag/${ sessionScope.locale.language }.png" />" />
							<div class="dynamicMenu">

								<c:forEach items="en,fr" var="language">
									<c:if test="${ language ne sessionScope.locale.language }">
										<c:url value="/LanguageChangement" var="languageChangementUrl">
											<c:param name="language" value="${ language }" />
										</c:url>
										<a class="dynamicMenuItem" href="${ languageChangementUrl }">
											<img class="flag" src="<c:url value="/img/languageFlag/${ language }.png" />" />
										</a>
									</c:if>
								</c:forEach>

							</div>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>

</div>
