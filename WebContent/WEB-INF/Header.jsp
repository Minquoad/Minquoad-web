<div id="header">
	<div id="siteNameHeader">
		<div class="centererContainer">
			<div class="totallyCenteredContainer">
				<a href="home">Minquoad.com</a>
			</div>
		</div>
	</div>

	<div id="pageTitleHeader">
		<div class="centererContainer">
			<div class="vertivallyCenteredContainer fullWidth">
				<div id="pageTitle">
					<a href="">
						<c:out value="${ pageTitle }" />
					</a>
				</div>
			</div>
		</div>
	</div>

	<div id="rightHeader">
		<div class="centererContainer">
			<div class="vertivallyCenteredContainer fullWidth">
				<div class="horizontallyPadded">
					<a href="test" class="headerItem">Test</a>
					<a href="home" class="headerItem">👥 Community</a>
					<c:if test="${not empty sessionUser}">
						<div class="headerItem dynamicMenuTrigger">
							▼
							<c:out value="${ sessionUser.nickname }" />
							<div class="dynamicMenu">
								<a class="dynamicMenuItem" href="accountManagement"> 👤
									Account management </a>
								<a class="dynamicMenuItem" href="logOut"> ✖ Log out </a>
							</div>
						</div>
					</c:if>
					<c:if test="${empty sessionUser}">
						<a href="logIn" class="headerItem">👤 Log in</a>
					</c:if>
				</div>
			</div>
		</div>
	</div>

</div>
