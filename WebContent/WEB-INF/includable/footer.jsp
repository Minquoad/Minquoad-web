<div id="footer">

	<div id="leftFooter">

		<c:if test="${not empty requestScope.user}">
		
		<form
			id="improvementSuggestionAdditionForm"
			action="<c:url value="/ImprovementSuggestionAddition" />"
			class="fullSize">
			<textarea
				name="text"
				placeholder="You can suggest here an improvement for the website
or report a bug. (Enter key to send)"></textarea>
		</form>
		
		</c:if>

	</div>

	<div id="rightFooter">

		<c:if test="${not empty requestScope.user && requestScope.user.admin}">
			<a href="<c:url value="/Administration" />">Administration</a>
		</c:if>

		<c:if test="${not empty requestScope.controllingAdmin}">
			<a href="<c:url value="/Unpossession" />">Unpossess</a>
		</c:if>

	</div>

</div>
