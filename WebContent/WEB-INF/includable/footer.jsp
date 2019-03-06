<div id="footer">

	<c:if test="${not empty requestScope.user && requestScope.user.admin}">
		<a href="<c:url value="/Administration" />">Administration</a>
	</c:if>

	<c:if test="${not empty requestScope.controllingAdmin}">
		<a href="<c:url value="/Unpossession" />">Unpossess</a>
	</c:if>

</div>
