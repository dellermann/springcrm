<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="user.settings.googleAuth.title" default="Authorize at Google" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="user.settings.googleAuth.title" default="Authorize at Google" /></h2>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="authorized"><p><g:message code="user.settings.googleAuth.alreadyAuthorized" /></p></g:if>
    <g:else><p><g:message code="user.settings.googleAuth.hints" /></p></g:else>
    <p><g:link action="settingsGoogleAuthRequest" class="button green"><g:message code="user.settings.googleAuth.authorize" default="Authorize at Google" /></g:link> <g:link action="settingsIndex" class="button red"><g:message code="default.btn.cancel" default="Cancel" /></g:link></p>
  </section>
</body>
</html>
