<%-- 
	params
		formKey : the key to get the form in the requestScope
		fieldName : the name of the field in the form
 --%>
<c:set var="form" value="${ requestScope[param.formKey] }" scope="page" />
<c:set var="fieldValueProblems" value="${ pageScope.form.getFieldValueProblems(param.fieldName) }" scope="page" />

<c:if test="${ fn:length(pageScope.fieldValueProblems) ne 0}">
	<ul class="formErrors">
		<c:forEach items="${ pageScope.fieldValueProblems }" var="problem" varStatus="status">
			<li><c:out value="${ problem }" /></li>
		</c:forEach>
	</ul>
</c:if>
