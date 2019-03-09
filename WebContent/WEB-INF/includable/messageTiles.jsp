<c:set var="messages" value="${requestScope[param.messages]}" scope="page" />

<c:set var="newLineChar" value="
" scope="page"  />

<c:forEach items="${ pageScope.messages }" var="message">
	<div class="borderedTile fullWidth" data-messageId="${ message.id }">
		<div class="messageMetaData">
			<div>
				<c:out value="${ message.user.nickname }" />
				:
			</div>
			<div>
				<c:out value="${ message.instant }" />
			</div>
		</div>
		<div class="messageText">
			<c:set var="text" scope="page" >
				<c:out value="${ message.text }" />
			</c:set>
			${fn:replace(text, pageScope.newLineChar, '<br/>')}
		</div>
	</div>
</c:forEach>
