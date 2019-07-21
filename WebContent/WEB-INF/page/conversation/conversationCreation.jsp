<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<fmt:setBundle basename="resources.Conversations" var="conversationsBundle" />

<div class="scrollableContainer centererContainer">
	<div class="totallyCenteredContainer tileContainer">
		<div class="borderedTile">
			<form class="padded" method="post" action="<c:url value="/ConversationCreation" />"
				accept-charset="UTF-8">
						<p>
							<label for="title">
								<fmt:message key="Title" bundle="${ conversationsBundle }" />
								:
							</label>
							<input type="text" name="title"
								value="<c:out value="${ requestScope.form.fields.title.value }" />" />
							<jsp:include page="/WEB-INF/includable/form/formFieldProblems.jsp">
								<jsp:param name="formKey" value="form" />
								<jsp:param name="fieldName" value="title" />
							</jsp:include>
						</p>
				<div>
					<input type="submit"
						value="<fmt:message key="Create" bundle="${ conversationsBundle }" />" />
				</div>
			</form>
		</div>
	</div>
</div>