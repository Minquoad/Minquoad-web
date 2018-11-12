<div id="header">
	<div id="siteNameHeader">
		<div class="centererContainer">
			<div class="totallyCenteredContainer">
				<a href="Home">Minquoad.com</a>
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
					<a href="Test" class="headerItem">Test</a>
					<a href="Home" class="headerItem">👥 Community</a>
					<c:if test="${not empty sessionUser}">
						<div class="headerItem dynamicMenuTrigger">
							▼
							<c:out value="${ sessionUser.nickname }" />
							<div class="dynamicMenu">
								<a class="dynamicMenuItem" href="AccountManagement"> 👤
									Account management </a>
								<a class="dynamicMenuItem" href="LogOut"> ✖ Log out </a>
							</div>
						</div>
					</c:if>
					<c:if test="${empty sessionUser}">
						<a href="LogIn" class="headerItem">👤 Log in</a>
					</c:if>
				</div>
			</div>
		</div>
	</div>

</div>
