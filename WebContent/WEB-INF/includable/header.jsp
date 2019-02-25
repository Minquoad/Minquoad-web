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
					<a href="<c:url value="/Test" />" class="headerItem">Test</a>
					<a href="<c:url value="/Community" />" class="headerItem">👥 Community</a>
					<c:if test="${not empty sessionUser}">
						<c:if test="${sessionUser.admin}">
							<a href="<c:url value="/Administration" />" class="headerItem">Administration</a>
						</c:if>
						<div class="headerItem dynamicMenuTrigger">
							▼
							<c:out value="${ sessionUser.nickname }" />
							<div class="dynamicMenu">
								<a class="dynamicMenuItem" href="<c:url value="/AccountManagement" />">👤 Account management </a>
								<a class="dynamicMenuItem" href="<c:url value="/LogOut" />">✖ Log out</a>
							</div>
						</div>
					</c:if>
					<c:if test="${empty sessionUser}">
						<a href="<c:url value="/LogIn" />" class="headerItem">👤 Log in</a>
					</c:if>
				</div>
			</div>
		</div>
	</div>

</div>
