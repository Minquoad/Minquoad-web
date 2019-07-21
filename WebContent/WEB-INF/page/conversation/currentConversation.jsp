<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<fmt:setBundle basename="resources.Conversations" var="conversationsBundle" />

<div id="current" class="fullSize"
	data-conversationId="${ requestScope.conversation.id }">
	<div id="topBar" class="inlineBlockContainer">
	
		<div class="inlineFlex fullHeigth titleSide">
		</div>
	
		<div class="title inlineFlex fullHeigth">
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