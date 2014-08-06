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
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <li>
        <g:button action="upload" color="white" size="medium"
          message="document.button.upload" />
      </li>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2><g:message code="document.allDocuments.label" default="All documents" /></h2>
    <div class="document-list lg" data-list="document"
      data-list-url="${createLink(controller: 'document', action: 'dir')}"
      data-download-url="${createLink(controller: 'document', action: 'download')}"
      ></div>
  </div>
  <content tag="scripts">
    <asset:javascript src="document" />
  </content>
</body>
</html>
