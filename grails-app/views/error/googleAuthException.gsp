<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="error.googleAuthException.title" /></title>
    <meta name="caption"
      content="${message(code: 'error.googleAuthException.title')}" />
  </head>

  <body>
    <p><g:message code="error.googleAuthException.description" /></p>

    <h2><g:message code="error.googleAuthException.reason" /></h2>
    <p><g:message code="${exception.message}"
      default="${exception.message}" /></p>
    <div class="buttons">
      <g:button controller="user" action="settingsGoogleAuth" color="primary"
        icon="key" message="user.settings.googleAuth.title" />
    </div>
  </body>
</html>
