<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="error.googleAuthException.title" /></title>
</head>

<body>
  <header>
    <h1><g:message code="error.googleAuthException.title" /></h1>
  </header>
  <div id="content">
    <p><g:message code="error.googleAuthException.description" /></p>

    <h2><g:message code="error.googleAuthException.reason" /></h2>
    <p><g:message code="${exception.message}"
      default="${exception.message}" /></p>
    <div class="buttons">
      <g:button controller="user" action="settingsGoogleAuth" color="green"
        icon="key" message="user.settings.googleAuth.title" />
    </div>
  </div>
</body>
</html>
