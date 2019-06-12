<meta charset="UTF-8">

<title>Minquoad</title>

<style>
	:root {
		<c:if test="${ empty requestScope.user }">

		</c:if>
		<c:if test="${ not empty requestScope.user }">

		</c:if>
	}
</style>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/ext/jquery.dynatable.css" />" />

<link rel="stylesheet" type="text/css" href="<c:url value="/css/variable.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/utilityClasse.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/tile.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/mainContainer.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/header.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/footer.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/dynamicMenu.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/movableDiv.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/account.css" />" />

<script>
	let currentContext = "<c:url value="/" />";
	let language = "${ sessionScope.locale.toLanguageTag() }";
	let readabilityImprovementActivated = ${ empty requestScope.user ? true : requestScope.user.readabilityImprovementActivated };
	let typingAssistanceActivated = ${ empty requestScope.user ? true : requestScope.user.typingAssistanceActivated };
	let userId = ${ empty requestScope.user ? null : requestScope.user.id };
</script>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>

<script type="text/javascript" src="<c:url value="/js/ext/jquery.dynatable.js" />"></script>

<script type="text/javascript" src="<c:url value="/js/utility.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/dynamicMenu.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/tile.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/improvementSuggestionAddition.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/dateFormatter.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/movableDiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/conversationDiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/main.js" />"></script>
