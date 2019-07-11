<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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
				<c:url value="/Profile" var="profileUrl">
					<c:param name="targetUserId" value="${ message.user.id }" />
				</c:url>
				<a href="${ profileUrl }">
					<span style="color: ${ message.user.getDefaultColorAsHtmlValue() };">
						<c:out value="${ message.user.nickname }" />
					</span> :
				</a>
				<c:if test="${ message.edited }">
					🖉
				</c:if>
			</div>
			<div>
				<c:if test="${ message.user eq requestScope.user }">
					<span class="messageEditionButton">🖉</span>
				</c:if>
				<span class="dateToFormat" data-format="1"> <c:out
						value="${ message.instant }" />
				</span>
			</div>
		</div>
		<div class="messageText">
			<c:if test="${ not message.edited }">
				<span class="keepLineBreak readabilityToImprove"><c:out value="${ message.text }" /></span>
			</c:if>
			<c:if test="${ message.edited }">
				<span class="keepLineBreak readabilityToImprove"><c:out value="${ message.editedText }" /></span>
			</c:if>
		</div>
		<c:if test="${ message.hasMessageFile() }">
			<c:if test="${ message.messageFile.isImage() }">
				<c:url value="/ImageDownload" var="messageFileUrl">
					<c:param name="id" value="${ message.messageFile.id }" />
				</c:url>
				<div class="messageImageContainer">
					<img src="${ messageFileUrl }" class="messageImage" />
				</div>
			</c:if>
			<c:if test="${ not message.messageFile.isImage() }">
				<c:url value="/FileDownload" var="messageFileUrl">
					<c:param name="id" value="${ message.messageFile.id }" />
				</c:url>
				<a class="messageFileLink" href="${ messageFileUrl }">
					📁
					<c:out value="${ message.messageFile.originalName }" />
				</a>
			</c:if>
		</c:if>
	</div>
</c:forEach>
