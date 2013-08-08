<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="document.plural" default="Documents" /></title>
  <r:require modules="document" />
</head>

<body>
  <header>
    <h1><g:message code="document.plural" default="Documents" /></h1>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div id="documents" data-load-url="${createLink(controller: 'document', action: 'command')}"></div>
  </div>
</body>
</html>
