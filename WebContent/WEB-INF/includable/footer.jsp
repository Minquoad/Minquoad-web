<fmt:setBundle basename="resources.Footer" var="footerBundle" />

<div id="footer">

	<div id="leftFooter">

		<c:if test="${not empty requestScope.user}">

			<form id="improvementSuggestionAdditionForm"
				action="<c:url value="/ImprovementSuggestionAddition" />" class="fullSize">
				<textarea name="text"
					placeholder="<fmt:message key="improvementSuggestionAdditionFormPlaceholder" bundle="${ footerBundle }" />"></textarea>
			</form>

		</c:if>

	</div>

	<div id="rightFooter">

		<c:if test="${not empty requestScope.user && requestScope.user.admin}">
			<a href="<c:url value="/Administration" />">
				<fmt:message key="Administration" bundle="${ footerBundle }" />
			</a>
		</c:if>

		<c:if test="${not empty requestScope.controllingAdmin}">
			<a href="<c:url value="/Unpossession" />">
				<fmt:message key="Unpossess" bundle="${ footerBundle }" />
			</a>
		</c:if>

	</div>

</div>
