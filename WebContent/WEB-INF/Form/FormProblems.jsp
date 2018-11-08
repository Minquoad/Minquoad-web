<c:if test="${not empty currentFormProblems}">
	<c:if test="${fn:length(currentFormProblems) == 1}">
		<p class="formErrors">
			<c:out value="${ currentFormProblems.get(0) }" />
		</p>
	</c:if>
	<c:if test="${fn:length(currentFormProblems) gt 1}">
		<ul class="formErrors">
			<c:forEach items="${ currentFormProblems }" var="formProblem"
				varStatus="status">
				<li><c:out value="${ currentFormProblems }" /></li>
			</c:forEach>
		</ul>
	</c:if>
</c:if>
