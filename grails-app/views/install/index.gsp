<html>
<head>
  <meta name="layout" content="install" />
  <title><g:message code="install.title" /></title>
</head>

<body>
  <header>
    <h1><g:message code="install.welcome.title" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="installBaseData" color="green" icon="arrow-right"
            message="install.btn.next.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div class="install-description">
      <p><g:message code="install.welcome.description" /></p>
    </div>
  </div>
</body>
</html>
