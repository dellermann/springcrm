<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="user.settings.title" default="User settings" /></title>
  <meta name="stylesheet" content="config" />
</head>

<body>
  <header>
    <h1><g:message code="user.settings.title" default="User settings" /></h1>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <div class="multicol-content configuration-overview">
      <div class="col col-l">
        <dl id="configuration-language">
          <dt><g:link action="settingsLanguage"><g:message code="user.settings.language.title" default="Language" /></g:link></dt>
          <dd><g:message code="user.settings.language.description" /></dd>
        </dl>
      </div>
      <div class="col col-r">
        <dl id="configuration-google-auth">
          <dt><g:link action="settingsGoogleAuth"><g:message code="user.settings.googleAuth.title" default="Authorize at Google" /></g:link></dt>
          <dd><g:message code="user.settings.googleAuth.description" /></dd>
        </dl>
      </div>
    </div>
  </div>
</body>
</html>
