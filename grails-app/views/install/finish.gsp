<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="install" />
  <title><g:message code="install.title" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="install.finish.title" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="installBaseData" class="white"><g:message code="install.btn.previous.label" /></g:link></li>
        <li><g:link action="finishSave" class="green"><g:message code="install.btn.finish.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div class="install-description">
      <p><g:message code="install.finish.description" /></p>
    </div>
  </section>
</body>
</html>
