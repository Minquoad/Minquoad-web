<fmt:setBundle basename="resources.CurrentConversation" var="currentConversationBundle" />

<div id="title" class="centererContainer">
	<div class="totallyCenteredContainer">
		<c:if test="${ requestScope.conversation.isMainBetweenTwoUsers() }">
			<c:forEach items="${ requestScope.participants }" var="loopUser">
				<c:if test="${ loopUser ne requestScope.user }">
					<fmt:message key="MainConversationWith" bundle="${ currentConversationBundle }" />
					<c:out value="${ loopUser.nickname }" />
				</c:if>
			</c:forEach>
		</c:if>
		<c:if test="${ not requestScope.conversation.isMainBetweenTwoUsers() }">
			<c:out value="${ requestScope.conversation.title }" />
		</c:if>
	</div>
</div>

<c:url value="/UnseenMessages" var="unseenMessagesUrl">
	<c:param name="conversationId" value="${ requestScope.conversation.id }" />
</c:url>
<div id="messages" class="tileContainer" data-unseenMessagesUrl="${ unseenMessagesUrl }">
	<jsp:include page="/WEB-INF/includable/messageTiles.jsp">
		<jsp:param name="messages" value="messages" />
	</jsp:include>
</div>
<div id="messageEditor">
	<form id="messageEditorForm" action="<c:url value="/MessageAddition" />">
		<input type="hidden" name="conversationId" value="${ requestScope.conversation.id }">
		<textarea name="text" placeholder="Type your message here."></textarea>
		<div id="sepcialChars">
			<div class="centererContainer dynamicMenuTrigger">
				<div class="totallyCenteredContainer centeredText">😃 😭 😍 🤬 😨 😯</div>
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
		<input type="button" value="<fmt:message key="Send" bundle="${ currentConversationBundle }" />" />
	</form>
</div>