<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="document.plural" default="Documents" /></title>
  <r:require modules="document" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="document.plural" default="Documents" /></h2>
    <%--
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </nav>
    --%>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div id="documents" data-load-url="${createLink(controller: 'document', action: 'command')}"></div>
  </section>
</body>
</html>
