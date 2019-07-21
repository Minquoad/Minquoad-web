<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<fmt:setBundle basename="resources.Footer" var="footerBundle" />

<div id="footer" class="inlineBlockContainer">

	<div id="leftFooter" class="inlineFlex">

		<c:if test="${not empty requestScope.user}">

			<form id="improvementSuggestionAdditionForm" accept-charset="UTF-8"
				action="<c:url value="/ImprovementSuggestionAddition" />"
				class="inlineBlock">

				<textarea name="text"
					placeholder="<fmt:message key="improvementSuggestionAdditionFormPlaceholder" bundle="${ footerBundle }" />"></textarea>
			</form>

		</c:if>

	</div>

	<div id="rightFooter" class="inlineFlex">
		<div class="vertivallyCenteredContainer fullWidth inlineBlock">

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

</div>
