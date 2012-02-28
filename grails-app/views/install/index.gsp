<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="install" />
  <title><g:message code="install.title" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="install.welcome.title" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="installBaseData" class="green"><g:message code="install.btn.next.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div class="install-description">
      <p><g:message code="install.welcome.description" /></p>
    </div>
  </section>
</body>
</html>
