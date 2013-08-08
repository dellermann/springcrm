<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="error.googleAuthException.title" default="Google authentication error" /></title>
</head>

<body>
  <header>
    <h1><g:message code="error.googleAuthException.title" default="Google authentication error" /></h1>
  </header>
  <div id="content">
    <p><g:message code="error.googleAuthException.description" /></p>

    <h2><g:message code="error.googleAuthException.reason" default="Reason" /></h2>
    <p><g:message code="${exception.message}" default="${exception.message}" /></p>
    <div class="buttons">
      <g:button controller="user" action="settingsGoogleAuth" color="green"
        icon="key" message="user.settings.googleAuth.title"
        default="Authorize at Google" />
    </div>
  </div>
</body>
</html>
