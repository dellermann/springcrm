<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="error.ldap.title" /></title>
    <meta name="caption" content="${message(code: 'error.ldap.title')}" />
  </head>

  <body>
    ${raw(msg)}

    <g:unless test="${admin}">
    <p><g:message code="error.ldap.contactAdmin" /></p>
    </g:unless>

    <div class="buttons">
      <g:if test="${admin}">
      <g:button controller="config" action="show"
        params="[page: 'ldap', returnUrl: url()]"
        color="primary" icon="cog" message="error.ldap.btn.settings" />
      </g:if>
      <g:button url="${retryUrl}" color="success" icon="repeat"
        message="error.ldap.btn.retry" />
      <g:if test="${backUrl}">
      <g:button url="${backUrl}" color="default" icon="arrow-left"
        message="default.button.back.label" />
      </g:if>
      <g:button url="${listUrl}" color="default" icon="list"
        message="default.button.list.label" />
    </div>
  </body>
</html>
