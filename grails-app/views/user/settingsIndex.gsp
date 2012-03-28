<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="user.settings.title" default="User settings" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="user.settings.title" default="User settings" /></h2>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div class="multicol-content configuration-overview">
      <div class="col col-l">
        <dl id="configuration-language">
          <dt><g:link action="settingsLanguage"><g:message code="user.settings.language.title" default="Language" /></g:link></dt>
          <dd><g:message code="user.settings.language.description" /></dd>
        </dl>
      </div>
      <div class="col col-r">
        <%--
        <dl id="configuration-google-auth">
          <dt><g:link action="settingsGoogleAuth"><g:message code="user.settings.googleAuth.title" default="Authorize at Google" /></g:link></dt>
          <dd><g:message code="user.settings.googleAuth.description" /></dd>
        </dl>
        --%>
      </div>
    </div>
  </section>
</body>
</html>
