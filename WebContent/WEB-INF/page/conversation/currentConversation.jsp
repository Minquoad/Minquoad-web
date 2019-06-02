<fmt:setBundle basename="resources.CurrentConversation"
	var="currentConversationBundle" />

<div id="current" data-conversationId="${ requestScope.conversation.id }">
	<div id="title" class="centererContainer">
		<div class="totallyCenteredContainer">
			<c:if test="${ requestScope.conversation.isMainBetweenTwoUsers() }">
				<c:forEach items="${ requestScope.participants }" var="loopUser">
					<c:if test="${ loopUser ne requestScope.user }">
						<fmt:message key="MainConversationWith"
							bundle="${ currentConversationBundle }" />
						<c:out value="${ loopUser.nickname }" />
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${ not requestScope.conversation.isMainBetweenTwoUsers() }">
				<c:out value="${ requestScope.conversation.title }" />
			</c:if>
		</div>
	</div>

	<div id="messages" class="tileContainer">
		<div id="messageExample" class="invisible">
			<div class="borderedTile fullWidth">
				<div class="messageMetaData">
					<div>
						<span class="messageUserNickname"><c:out
								value="${ message.user.nickname }" /></span> :
					</div>
					<div class="messageInstant">
						<c:out value="${ message.instant }" />
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="/WEB-INF/includable/messageTiles.jsp">
			<jsp:param name="messages" value="messages" />
		</jsp:include>
	</div>
	<div id="messageEditor">
		<form id="messageEditorForm" action="<c:url value="/MessageAddition" />">
			<input type="hidden" name="conversationId"
				value="${ requestScope.conversation.id }">
			<input type="button" value="⟰" />
			<textarea name="text"
				placeholder="<fmt:message key="TypeYourMessageHere" bundle="${ currentConversationBundle }" />"></textarea>
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
		</form>
	</div>
</div>