<%-- 
	params
		messages : the key to get the messages in the requestScope
 --%>
<c:set var="messages" value="${requestScope[param.messages]}" scope="page" />

<fmt:setBundle basename="resources.MessageTiles" var="messageTilesBundle" />

<c:forEach items="${ pageScope.messages }" var="message">
	<div class="borderedTile fullWidth" data-messageId="${ message.id }" <c:if test="${ message.edited }">
	title="<fmt:message key="OriginalMessage" bundle="${ messageTilesBundle }" /> : <c:out value="${ message.text }" />"
	</c:if>>
		<div class="messageMetaData">
			<div>
				<span style="color: ${ message.user.getDefaultColorAsHtmlValue() };"><c:out value="${ message.user.nickname }" /></span>
				:
			</div>
			<div>
				<c:out value="${ message.instant }" />
			</div>
		</div>
		<div class="messageText">
			<c:set var="text" scope="page">
				<c:if test="${ not message.edited }">
					<c:out value="${ message.text }" />
				</c:if>
				<c:if test="${ message.edited }">
					<c:out value="${ message.editedText }" />
				</c:if>
			</c:set>
			<fmt:message key="newLineChar" bundle="${ messageTilesBundle }" var="newLineChar" />
			${fn:replace(text, pageScope.newLineChar, '<br/>')}
		</div>
	</div>
</c:forEach>
