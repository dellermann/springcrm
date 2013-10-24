<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="user.settings.googleAuth.title" default="Authorize at Google" /></title>
  <r:require module="settingsGoogleAuth" />
</head>

<body>
  <header>
    <h1><g:message code="user.settings.googleAuth.title" default="Authorize at Google" /></h1>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:if test="${authorized}">
      <p><g:message code="user.settings.googleAuth.alreadyAuthorized" /></p>
    </g:if>
    <g:else>
      <p><g:message code="user.settings.googleAuth.hints" /></p>
    </g:else>
    <p>
      <g:button action="settingsGoogleAuthRequest" color="green"
        icon="signin" message="user.settings.googleAuth.authorize" />
      <g:if test="${authorized}">
      <g:button elementId="google-auth-revoke" color="red" icon="signout"
        message="user.settings.googleAuth.revoke" />
      </g:if>
      <g:button action="settingsIndex" color="white" icon="remove-circle"
        message="default.btn.cancel" />
    </p>
    <div id="dialog-confirm" class="dialog"
      title="${message(code: 'user.settings.googleAuth.revoke.confirm.title')}"
      data-submit-url="${createLink(action: 'settingsGoogleAuthRevoke')}">
      <div class="dialog-with-icon">
        <i class="icon-warning-sign"></i>
        <div>
          <p><g:message code="user.settings.googleAuth.revoke.confirm.message" /></p>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
