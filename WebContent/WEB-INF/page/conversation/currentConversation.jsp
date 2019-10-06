<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<fmt:setBundle basename="resources.Conversations" var="conversationsBundle" />

<div id="current" class="fullSize"
	data-conversationId="${ requestScope.conversation.id }">
	<div id="topBar" class="inlineBlockContainer">

		<div class="inlineFlex fullHeigth titleSide">
			<div class="totallyCenteredContainer participants">
				<c:set var="first" value="${ true }" scope="page" />
				<c:forEach items="${ requestScope.participants }" var="loopUser">
					<c:if test="${ loopUser ne requestScope.user }">
						<c:url value="/Profile" var="profileUrl">
							<c:param name="targetUserId" value="${ loopUser.id }" />
						</c:url>
						<a href="${ profileUrl }" class="undecorated">
							${ first?"":"," }
							<c:set var="first" value="${ false }" scope="page" />
							<c:out value="${ loopUser.nickname }" />
						</a>
					</c:if>
				</c:forEach>
			</div>
		</div>

		<div class="title inlineFlex fullHeigth scrollableContainer">
			<div class="totallyCenteredContainer">
				<c:if test="${ requestScope.conversation.isMainBetweenTwoUsers() }">
					<c:forEach items="${ requestScope.participants }" var="loopUser">
						<c:if test="${ loopUser ne requestScope.user }">
							<fmt:message key="MainConversationWith" bundle="${ conversationsBundle }" />
							<c:out value="${ loopUser.nickname }" />
						</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${ not requestScope.conversation.isMainBetweenTwoUsers() }">
					<c:out value="${ requestScope.conversation.title }" />
				</c:if>
			</div>
		</div>

		<div class="inlineFlex fullHeigth titleSide">
			<c:if test="${ requestScope.conversation.createdByUser }">

				<form method="POST" action="<c:url value="/ConversationLeaving" />"
					class="inlineFlex">
					<input type="hidden" name="conversationId"
						value="${ requestScope.conversation.id }" />
					<input type="submit" value="leave" />
				</form>

			</c:if>
			<c:if test="${ requestScope.conversationAccess.administrator }">

				<form method="POST" action="<c:url value="/ConversationAccessGranting" />"
					class="inlineFlex">
					<input type="hidden" name="conversationId"
						value="${ requestScope.conversation.id }" />

					<select name="targets" multiple="multiple">
						<c:forEach items="${ requestScope.daoFactory.userDao.all }" var="loopUser">
							<c:if test="${ not requestScope.participants.contains(loopUser) }">

								<option value="${ loopUser.id }"><c:out
										value="${ loopUser.nickname }" /></option>

							</c:if>
						</c:forEach>
					</select>

					<input type="submit" value="add">
				</form>

			</c:if>
		</div>

	</div>

	<div id="messages" class="tileContainer scrollableContainer">
		<jsp:include page="/WEB-INF/includable/messageTiles.jsp">
			<jsp:param name="messages" value="messages" />
		</jsp:include>
	</div>
	<div id="messageEditor">
		<form id="messageEditorForm" action="<c:url value="/MessageAddition" />"
			method="post" accept-charset="UTF-8" enctype="multipart/form-data"
			class="inlineBlockContainer">

			<input type="hidden" name="conversationId"
				value="${ requestScope.conversation.id }">

			<input type="button" value="⟰" />

			<textarea class="improvedTextField inlineBlock" name="text"
				placeholder="<fmt:message key="TypeYourMessageHere" bundle="${ conversationsBundle }" />"></textarea>

			<div id="sepcialChars" class="inlineFlex dynamicMenuTrigger">
				<div class="totallyCenteredContainer centeredText">
					<span>😃</span> <span>😭</span> <span>😍</span> <span>🤬</span> <span>😨</span>
					<span>😯</span>
				</div>
				<div class="dynamicMenu">
					<div class="dynamicMenuItem">
						<span>😃</span><span>😄</span><span>😅</span><span>😆</span><span>😊</span><span>😎</span><span>😇</span><span>😈</span><span>😋</span><span>😏</span><span>😌</span><span>😁</span><span>😀</span><span>😂</span><span>🤣</span><span>🤠</span><span>🤡</span>
						<br />
						<span>🤑</span><span>🤩</span><span>🤪</span>
						<br />
						<span>😳</span><span>😉</span><span>😗</span><span>😚</span><span>😘</span><span>😙</span><span>😍</span><span>🤤</span><span>🤗</span><span>😛</span><span>😜</span><span>😝</span>
						<br />
						<span>😶</span><span>🙃</span><span>😐</span><span>😑</span><span>🤔</span><span>🙄</span><span>😮</span><span>😔</span><span>😖</span><span>😕</span><span>🤨</span><span>🤯</span><span>🤭</span><span>🧐</span>
						<br />
						<span>🤫</span><span>😯</span><span>🤐</span>
						<br />
						<span>😩</span><span>😫</span><span>😪</span><span>😴</span><span>😵</span>
						<br />
						<span>😦</span><span>😢</span><span>😭</span>
						<br />
						<span>🤢</span><span>🤮</span><span>😷</span><span>🤒</span><span>🤕</span>
						<br />
						<span>😒</span><span>😠</span><span>😡</span><span>😤</span><span>😣</span><span>😧</span><span>🤬</span>
						<br />
						<span>😬</span><span>😓</span><span>😰</span><span>😨</span><span>😱</span><span>😲</span><span>😞</span><span>😥</span><span>😟</span>
						<br />
						<span>🤥</span><span>🤓</span><span>🤖</span>
						<br />
						<span>😸</span><span>😹</span><span>😺</span><span>😻</span><span>😼</span><span>😽</span><span>😾</span><span>😿</span><span>🙀</span><span>🙈</span><span>🙉</span><span>🙊</span>
						<br />
						<span>🤦</span><span>🤷</span><span>🙅</span><span>🙆</span><span>🙋</span><span>🙌</span><span>🙍</span><span>🙎</span><span>🙇</span><span>🙏</span><span>🤳</span>
						<br />
						<span>💃</span><span>🕺</span><span>💆</span><span>💇</span><span>💈</span><span>🧖</span><span>🧘</span><span>👰</span><span>🤰</span><span>🤱</span><span>👯</span>
						<br />
						<span>👶</span><span>🧒</span><span>👦</span><span>👩</span><span>👨</span><span>👧</span><span>🧔</span><span>🧑</span><span>🧓</span><span>👴</span><span>👵</span>
						<br />
						<span>👪</span><span>👫</span><span>👬</span><span>👭</span>
						<br />
						<span>👲</span><span>👳</span><span>🧕</span><span>👱</span><span>🤴</span><span>👸</span><span>🤵</span><span>🎅</span><span>🤶</span><span>👮</span><span>👷</span><span>💁</span><span>💂</span><span>🕴</span><span>🕵</span>
						<br />
						<span>👼</span><span>👻</span><span>🧙</span><span>🧚</span><span>🧛</span><span>🧜</span><span>🧝</span><span>🧞</span><span>🧟</span><span>👿</span><span>💀</span><span>☠</span><span>🕱</span><span>🕲</span><span>👽</span><span>👾</span><span>🛸</span><span>👹</span><span>👺</span>
						<br />
						<span>🧠</span><span>👁</span><span>👀</span><span>👂</span><span>👃</span><span>👄</span><span>🗢</span><span>👅</span>
						<br />
						<span>💢</span><span>💦</span><span>💧</span><span>💫</span><span>💤</span><span>💥</span><span>💨</span><span>💪</span><span>🗲</span><span>🔥</span><span>💡</span><span>💩</span>
						<br />
					</div>
				</div>
			</div>

			<div class="inputFileTrigger inlineBlock">
				<div class="blockFlex fullSize visibleWhenEmpty">
					<div class="totallyCenteredContainer">📁∅</div>
				</div>
				<div class="blockFlex fullSize visibleWhenFull">
					<div class="totallyCenteredContainer">📁+</div>
				</div>
				<input type="file" name="file" />
			</div>

		</form>
	</div>
</div>