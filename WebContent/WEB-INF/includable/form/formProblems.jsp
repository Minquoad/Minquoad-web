<c:set var="formProblems" value="${requestScope[param.formProblems]}"
	scope="page" />

<c:if test="${not empty pageScope.formProblems}">
	<c:if test="${fn:length(pageScope.formProblems) == 1}">
		<p class="formErrors">
			<c:out value="${ pageScope.formProblems.get(0) }" />
		</p>
	</c:if>
	<c:if test="${fn:length(pageScope.formProblems) gt 1}">
		<ul class="formErrors">
			<c:forEach items="${ pageScope.formProblems }" var="formProblem"
				varStatus="status">
				<li><c:out value="${ formProblem }" /></li>
			</c:forEach>
		</ul>
	</c:if>
</c:if>
