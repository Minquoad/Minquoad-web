<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<fmt:setBundle basename="resources.Header" var="headerBundle" />

<div id="header" class="inlineBlockContainer">

	<a id="siteNameHeader" href="<c:url value="/Home" />" class="inlineFlex fullHeigth">
		<span class="totallyCenteredContainer">
			Minquoad.com
		</span>
	</a>

	<a id="pageTitleHeader" class="inlineFlex fullHeigth" href="">
		<span class="vertivallyCenteredContainer fullWidth">
			<c:out value="${ param.pageTitle }" />
		</span>
	</a>

	<div id="rightHeader"
		class="inlineFlex fullHeigth inlineBlockContainer">

		<c:if test="${ not empty requestScope.user }">

			<div class="headerItem fullHeigth inlineFlex dynamicMenuTrigger">
				<div class="vertivallyCenteredContainer">
					<c:out value="${ requestScope.user.nickname }" />
					▼
				</div>
				<div class="dynamicMenu">
					<c:url value="/Profile" var="profileUrl">
						<c:param name="targetUserId" value="${ requestScope.user.id }" />
					</c:url>
					<a class="dynamicMenuItem" href="${ profileUrl }">
						👤
						<fmt:message key="Profile" bundle="${ headerBundle }" />
					</a>
					<a class="dynamicMenuItem" href="<c:url value="/AccountManagement" />">
						🛠
						<fmt:message key="AccountManagement" bundle="${ headerBundle }" />
					</a>
					<a class="dynamicMenuItem" href="<c:url value="/OutLoging" />">
						✖
						<fmt:message key="LogOut" bundle="${ headerBundle }" />
					</a>
				</div>
			</div>
			
			<a href="<c:url value="/Community" />"
				class="headerItem fullHeigth inlineFlex">
				<span class="vertivallyCenteredContainer">
					👥
					<fmt:message key="Community" bundle="${ headerBundle }" />
				</span>
			</a>
			
			<a href="<c:url value="/Conversations" />"
				class="headerItem fullHeigth inlineFlex">
				<span class="vertivallyCenteredContainer">
					🗪
					<fmt:message key="Conversation" bundle="${ headerBundle }" />
				</span>
			</a>
			
		</c:if>

		<c:if test="${ empty requestScope.user }">

			<div class="headerItem fullHeigth inlineFlex dynamicMenuTrigger">
				<img class="flag vertivallyCenteredContainer"
					src="<c:url value="/img/languageFlag/${ sessionScope.locale.language }.png" />" />
				<div class="dynamicMenu">

					<c:forEach items="en,fr" var="language">
						<c:if test="${ language ne sessionScope.locale.language }">
							<c:url value="/LanguageChangement" var="languageChangementUrl">
								<c:param name="language" value="${ language }" />
							</c:url>
							<a class="dynamicMenuItem" href="${ languageChangementUrl }">
								<img class="flag"
									src="<c:url value="/img/languageFlag/${ language }.png" />" />
							</a>
						</c:if>
					</c:forEach>

				</div>
			</div>
			
			<a href="<c:url value="/InLoging" />"
				class="headerItem fullHeigth inlineFlex">
				<span class="vertivallyCenteredContainer">
					👤
					<fmt:message key="LogIn" bundle="${ headerBundle }" />
				</span>
			</a>
			
		</c:if>
		
		<div class="headerItem fullHeigth inlineFlex dynamicMenuTrigger">
			<div class="vertivallyCenteredContainer">
				<fmt:message key="Activities" bundle="${ headerBundle }" />
				▼
			</div>
			<div class="dynamicMenu">
				<a class="dynamicMenuItem" href="<c:url value="/Test" />">
					<fmt:message key="Test" bundle="${ headerBundle }" />
				</a>
			</div>
		</div>
		
	</div>

</div>
