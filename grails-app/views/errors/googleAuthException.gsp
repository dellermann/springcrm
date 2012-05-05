<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="error.googleAuthException.title" default="Google authentication error" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="error.googleAuthException.title" default="Google authentication error" /></h2>
  </div>
  <section id="content">
    <p><g:message code="error.googleAuthException.description" /></p>
    <h3><g:message code="error.googleAuthException.reason" default="Reason" /></h3>
    <p><g:message code="${exception.message}" default="${exception.message}" /></p>
    <div class="buttons"><g:link controller="user" action="settingsGoogleAuth" class="button green"><g:message code="user.settings.googleAuth.title" default="Authorize at Google" /></g:link></div>
  </section>
</body>
</html>
