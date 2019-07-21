<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<fmt:setBundle basename="resources.Conversations" var="conversationsBundle" />

<div id="current" data-conversationId="${ requestScope.conversation.id }">
	<div id="topBar">
		<div class="title">
			<div class="centererContainer">
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
		</div>
	</div>

	<div id="messages" class="tileContainer">
		<jsp:include page="/WEB-INF/includable/messageTiles.jsp">
			<jsp:param name="messages" value="messages" />
		</jsp:include>
	</div>
	<div id="messageEditor">
		<form id="messageEditorForm" action="<c:url value="/MessageAddition" />"
			method="post" accept-charset="UTF-8" enctype="multipart/form-data">
			<input type="hidden" name="conversationId"
				value="${ requestScope.conversation.id }">
			<input type="button" value="⟰" />
			<textarea class="improvedTextField" name="text"
				placeholder="<fmt:message key="TypeYourMessageHere" bundle="${ conversationsBundle }" />"></textarea>
			<div id="sepcialChars">
				<div class="centererContainer dynamicMenuTrigger">
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
			</div>
			<div class="inputFileTrigger">
				<div class="centererContainer visibleWhenEmpty">
					<div class="totallyCenteredContainer">📁∅</div>
				</div>
				<div class="centererContainer visibleWhenFull">
					<div class="totallyCenteredContainer">📁+</div>
				</div>
				<input type="file" name="file" />
			</div>
		</form>
	</div>
</div>