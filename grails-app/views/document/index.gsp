<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="document.plural" default="Documents" /></title>
  <meta name="stylesheet" content="document" />
</head>

<body>
  <header>
    <h1><g:message code="document.plural" default="Documents" /></h1>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <div class="document-list" data-list="document"
      data-list-url="${createLink(controller: 'document', action: 'list')}"
      ></div>
  </div>
  <content tag="scripts">
    <asset:javascript src="document" />
  </content>
</body>
</html>
