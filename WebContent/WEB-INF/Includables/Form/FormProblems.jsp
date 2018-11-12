<c:set var="formProblems" value="${requestScope[param.formProblems]}"
	scope="page" />

<c:if test="${not empty formProblems}">
	<c:if test="${fn:length(formProblems) == 1}">
		<p class="formErrors">
			<c:out value="${ formProblems.get(0) }" />
		</p>
	</c:if>
	<c:if test="${fn:length(formProblems) gt 1}">
		<ul class="formErrors">
			<c:forEach items="${ formProblems }" var="formProblem"
				varStatus="status">
				<li><c:out value="${ formProblem }" /></li>
			</c:forEach>
		</ul>
	</c:if>
</c:if>
