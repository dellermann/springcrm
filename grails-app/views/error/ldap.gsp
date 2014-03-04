<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="error.ldap.title" /></title>
</head>

<body>
  <header>
    <h1><g:message code="error.ldap.title" /></h1>
  </header>
  <div id="content">
    ${raw(msg)}
    <g:unless test="${admin}">
    <p><g:message code="error.ldap.contactAdmin" /></p>
    </g:unless>
    <div class="buttons">
      <g:if test="${admin}">
      <g:button controller="config" action="show" params="[page: 'ldap', returnUrl: url()]"
        color="green" icon="cog" message="error.ldap.btn.settings" />
      </g:if>
      <g:button url="${retryUrl}" color="green" icon="repeat"
        message="error.ldap.btn.retry" />
      <g:if test="${backUrl}">
      <g:button url="${backUrl}" color="white" icon="arrow-left"
        message="default.button.back.label" />
      </g:if>
      <g:button url="${listUrl}" color="white" icon="list"
        message="default.button.list.label" />
    </div>
  </div>
</body>
</html>
