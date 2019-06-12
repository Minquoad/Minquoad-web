<%-- 
	params
		messages : the key to get the messages in the requestScope
 --%>
<c:set var="messages" value="${ requestScope[param.messages] }" scope="page" />

<fmt:setBundle basename="resources.Conversations" var="conversationsBundle" />

<c:forEach items="${ pageScope.messages }" var="message">
	<div class="borderedTile fullWidth" data-messageId="${ message.id }"
		data-messageUserId="${ message.user.id }"
		<c:if test="${ message.edited }">
	title="<fmt:message key="OriginalMessage" bundle="${ conversationsBundle }" /><c:out value="${ message.text }" />"
	</c:if>>
		<div class="messageMetaData">
			<div class="name">
				<span style="color: ${ message.user.getDefaultColorAsHtmlValue() };"><c:out
						value="${ message.user.nickname }" /></span> :
				<c:if test="${ message.edited }">
					🖉
				</c:if>
			</div>
			<div>
				<c:if test="${ message.user eq requestScope.user }">
					<span class="messageEditionButton">🖉</span>
				</c:if>
				<span class="dateToFormat"
					data-format="1"> <c:out value="${ message.instant }" />
				</span>
			</div>
		</div>
		<div class="messageText">
			<c:if test="${ not message.edited }">
				<c:out value="${ message.text }" />
			</c:if>
			<c:if test="${ message.edited }">
				<c:out value="${ message.editedText }" />
			</c:if>
		</div>
	</div>
</c:forEach>
