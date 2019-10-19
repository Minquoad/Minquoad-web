<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<meta charset="UTF-8">

<title>Minquoad</title>

<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/css/select2.min.css"/>

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
	let currentContext = '<c:url value="/" />';
	let language = "${ sessionScope.locale.toLanguageTag() }";
	let readabilityImprovementActivated = ${ empty requestScope.user ? true : requestScope.user.readabilityImprovementActivated };
	let typingAssistanceActivated = ${ empty requestScope.user ? true : requestScope.user.typingAssistanceActivated };
	let userId = ${ empty requestScope.user ? "null" : "\"".concat(requestScope.user.id.toString()).concat("\"") };
	let loadingImgUrl = '<c:url value="/img/loading.gif" />';
	let fileDownloadUrl = "<c:url value="/FileDownload" />";
	let imageDownloadUrl = "<c:url value="/ImageDownload" />";
</script>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/js/select2.min.js"></script>

<script type="text/javascript" src="<c:url value="/js/ext/jquery.dynatable.js" />"></script>

<script type="text/javascript" src="<c:url value="/js/websocketsHandle.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/utility.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/dynamicMenu.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/tile.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/ergonomics.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/form.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/movableDiv.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/improvementSuggestionAddition.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/main.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/subPageMenu.js" />"></script>
